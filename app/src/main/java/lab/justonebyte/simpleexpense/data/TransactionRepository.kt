package lab.justonebyte.simpleexpense.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import lab.justonebyte.simpleexpense.model.ServerTransaction
import lab.justonebyte.simpleexpense.model.Transaction
import lab.justonebyte.simpleexpense.utils.getTimeStampForYearEnd
import lab.justonebyte.simpleexpense.utils.getTimeStampForYearStart
import lab.justonebyte.simpleexpense.utils.getTimestampForMonthEnd
import lab.justonebyte.simpleexpense.utils.getTimestampForMonthStart
import javax.inject.Inject

interface TransactionRepository {
    fun getDailyTransactions(day:String): Flow<List<Transaction>>
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

    override fun getMonthlyTransactions(month:String): Flow<List<Transaction>> {
        val startMonthTimeStamp = getTimestampForMonthStart(month)-1
        val endMonthTimeStamp = getTimestampForMonthEnd(month)+1

        val transactionEntities = transactionDao.getTransactionsByMonth(startMonthTimeStamp,endMonthTimeStamp)
        return transactionEntities.map { list -> list.map { Transaction.Mapper.mapToDomain(it) } }
    }

    override fun getYearlyTransactions(year:String): Flow<List<Transaction>> {
        val startYearTimeStamp = getTimeStampForYearStart(year.toInt())-1
        val endYearTimeStamp = getTimeStampForYearEnd(year.toInt())+1
        val transactionEntities = transactionDao.getTransactionsByYear(startYearTimeStamp,endYearTimeStamp)
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
        transactionEntity.version = existingTransaction.version!! +1
        transactionDao.update(transactionEntity = transactionEntity)

    }

    override suspend fun delete(transaction: Transaction) {
        val existingTransaction = transactionDao.get(transaction.unique_id)
        existingTransaction.version = existingTransaction.version!!+1
        existingTransaction.deleted_at = System.currentTimeMillis()
        transactionDao.update(transactionEntity = existingTransaction)
    }

    override suspend fun deleteAll() {
        transactionDao.deleteAll()
    }

}