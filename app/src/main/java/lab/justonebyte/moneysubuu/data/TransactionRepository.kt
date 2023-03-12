package lab.justonebyte.moneysubuu.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import lab.justonebyte.moneysubuu.model.Transaction
import java.sql.Timestamp
import javax.inject.Inject

interface TransactionRepository {
    fun getDailyTransactions(day:String): Flow<List<Transaction>>
    fun getWeeklyTransactions(week:String): Flow<List<Transaction>>
    fun getMonthlyTransactions(month:String): Flow<List<Transaction>>
    fun getYearlyTransactions(year:String): Flow<List<Transaction>>
    fun getTotalTransactions(): Flow<List<Transaction>>

    suspend fun insert(transaction: Transaction)
    suspend fun update(transaction: Transaction)
    suspend fun delete(transaction: Transaction)

}
class TransactionRepositoryImpl @Inject constructor(val transactionDao: TransactionDao):TransactionRepository {
    override fun getDailyTransactions(day:String): Flow<List<Transaction>> {
        val transactionEntities = transactionDao.getTransactions(day)
        return transactionEntities.map { list -> list.map { Transaction.Mapper.mapToDomain(it) } }
    }

    override fun getWeeklyTransactions(week: String): Flow<List<Transaction>> {
        println("week:$week")
        val transactionEntities = transactionDao.getTransactionsByWeek(week)
        return transactionEntities.map { list -> list.map { Transaction.Mapper.mapToDomain(it) } }
    }

    override fun getMonthlyTransactions(month:String): Flow<List<Transaction>> {
        val transactionEntities = transactionDao.getTransactionsByMonth(month)
        return transactionEntities.map { list -> list.map { Transaction.Mapper.mapToDomain(it) } }
    }

    override fun getYearlyTransactions(year:String): Flow<List<Transaction>> {
        val transactionEntities = transactionDao.getTransactionsByYear(year)
        return transactionEntities.map { list -> list.map { Transaction.Mapper.mapToDomain(it) } }
    }

    override fun getTotalTransactions(): Flow<List<Transaction>> {
        val transactionEntities = transactionDao.getTotalTransactions()
        return transactionEntities.map { list -> list.map { Transaction.Mapper.mapToDomain(it) } }
    }

    override suspend fun insert(transaction: Transaction)  {
        val transactionEntity = Transaction.Mapper.mapToEntity(transaction = transaction)
        transactionDao.insert(transactionEntity = transactionEntity)
    }

    override suspend fun update(transaction: Transaction) {
        val existingTransaction = transaction.id?.let { transactionDao.get(it) }

        val transactionEntity = Transaction.Mapper.mapToEntity(transaction = transaction)
        if (existingTransaction != null) {
            if(existingTransaction.version!! <= existingTransaction.latest_server_version!!){
                transactionEntity.version = existingTransaction.version!! +1
            }else{
                transactionEntity.version = existingTransaction.version!!
            }
        }
        transactionDao.update(transactionEntity = transactionEntity)
    }

    override suspend fun delete(transaction: Transaction) {
        var existingTransaction = transaction.id?.let { transactionDao.get(it) }
        if (existingTransaction != null) {
            existingTransaction.deleted_at = System.currentTimeMillis()
            transactionDao.update(transactionEntity = existingTransaction)
        }


//        transaction.id?.let { transactionDao.delete(it) }
    }

}