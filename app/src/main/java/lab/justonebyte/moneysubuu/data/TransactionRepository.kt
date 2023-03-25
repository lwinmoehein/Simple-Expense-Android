package lab.justonebyte.moneysubuu.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import lab.justonebyte.moneysubuu.model.ServerCategory
import lab.justonebyte.moneysubuu.model.ServerTransaction
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.utils.getCurrentGlobalTime
import java.sql.Timestamp
import java.text.SimpleDateFormat
import javax.inject.Inject

interface TransactionRepository {
    fun getDailyTransactions(day:String): Flow<List<Transaction>>
    fun getWeeklyTransactions(week:String): Flow<List<Transaction>>
    fun getMonthlyTransactions(month:String): Flow<List<Transaction>>
    fun getYearlyTransactions(year:String): Flow<List<Transaction>>
    fun getTotalTransactions(): Flow<List<Transaction>>
    fun getServerTransactions(): List<ServerTransaction>
    suspend fun getUniqueIdsWithVersions():List<UniqueIdWithVersion>


    suspend fun insertAll(transactions: List<ServerTransaction>)
    suspend fun insert(transaction: Transaction)
    suspend fun update(transaction: Transaction)
    suspend fun delete(transaction: Transaction)
    suspend fun deleteAll()

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

    override fun getServerTransactions(): List<ServerTransaction> {
        return transactionDao.getAllTransactions()
    }

    override suspend fun getUniqueIdsWithVersions(): List<UniqueIdWithVersion> {
        return transactionDao.getUniqueIdsWithVersions()
    }

    override suspend fun insertAll(transactions: List<ServerTransaction>) {
        return transactionDao.insertAll(transactions.map { Transaction.Mapper.mapToEntityFromServer(it) })
    }

    override suspend fun insert(transaction: Transaction)  {
        val transactionEntity = Transaction.Mapper.mapToEntity(transaction = transaction)
        transactionDao.insert(transactionEntity = transactionEntity)
    }

    override suspend fun update(transaction: Transaction) {
        val existingTransaction = transactionDao.get(transaction.unique_id)

        val transactionEntity = Transaction.Mapper.mapToEntity(transaction = transaction)
        if (existingTransaction != null) {
            transactionEntity.version = existingTransaction.version!! +1
            transactionEntity.updated_at = getCurrentGlobalTime()

            transactionDao.update(transactionEntity = transactionEntity)

        }
    }

    override suspend fun delete(transaction: Transaction) {
        var existingTransaction = transactionDao.get(transaction.unique_id)
        if (existingTransaction != null) {
            existingTransaction.version = existingTransaction.version!!+1
            existingTransaction.updated_at = getCurrentGlobalTime()
            existingTransaction.deleted_at = getCurrentGlobalTime()
            transactionDao.update(transactionEntity = existingTransaction)
        }
    }

    override suspend fun deleteAll() {
        transactionDao.deleteAll()
    }

}