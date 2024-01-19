package lab.justonebyte.simpleexpense.data

import lab.justonebyte.simpleexpense.model.ServerTransaction
import lab.justonebyte.simpleexpense.model.Transaction
import lab.justonebyte.simpleexpense.utils.getTimeStampForYearEnd
import lab.justonebyte.simpleexpense.utils.getTimeStampForYearStart
import lab.justonebyte.simpleexpense.utils.getTimestampForMonthEnd
import lab.justonebyte.simpleexpense.utils.getTimestampForMonthStart
import javax.inject.Inject

interface TransactionRepository {
    suspend fun getDailyTransactions(day:String): List<Transaction>
    suspend fun getMonthlyTransactions(month:String): List<Transaction>
    suspend fun getYearlyTransactions(year:String): List<Transaction>
    suspend fun getTotalTransactions(): List<Transaction>
    fun getServerTransactions(): List<ServerTransaction>
    suspend fun getUniqueIdsWithVersions():List<UniqueIdWithVersion>


    suspend fun insertAll(transactions: List<ServerTransaction>)
    suspend fun insert(transaction: Transaction)
    suspend fun update(transaction: Transaction)
    suspend fun delete(transaction: Transaction)
    suspend fun deleteAll()

}
class TransactionRepositoryImpl @Inject constructor(val transactionDao: TransactionDao):TransactionRepository {

    override suspend fun getDailyTransactions(day:String): List<Transaction> {
        val transactionEntities = transactionDao.getTransactions(day)
        return transactionEntities.map { Transaction.Mapper.mapToDomain(it) }
    }

    override suspend fun getMonthlyTransactions(month:String): List<Transaction> {
        val startMonthTimeStamp = getTimestampForMonthStart(month)
        val endMonthTimeStamp = getTimestampForMonthEnd(month)

        val transactionEntities = transactionDao.getTransactionsByMonth(startMonthTimeStamp,endMonthTimeStamp)
        return transactionEntities.map { Transaction.Mapper.mapToDomain(it)  }
    }

    override suspend fun getYearlyTransactions(year:String): List<Transaction> {
        val startYearTimeStamp = getTimeStampForYearStart(year.toInt())
        val endYearTimeStamp = getTimeStampForYearEnd(year.toInt())
        val transactionEntities = transactionDao.getTransactionsByYear(startYearTimeStamp,endYearTimeStamp)
        return transactionEntities.map {  Transaction.Mapper.mapToDomain(it)  }
    }

    override suspend fun getTotalTransactions(): List<Transaction> {
        val transactionEntities = transactionDao.getTotalTransactions()
        return transactionEntities.map { Transaction.Mapper.mapToDomain(it)  }
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