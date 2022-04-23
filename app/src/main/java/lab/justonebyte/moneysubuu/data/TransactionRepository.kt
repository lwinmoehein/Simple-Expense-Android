package lab.justonebyte.moneysubuu.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import lab.justonebyte.moneysubuu.model.Transaction
import javax.inject.Inject

interface TransactionRepository {
    fun getTransactions(): Flow<List<Transaction>>
}
class TransactionRepositoryImpl @Inject constructor(val transactionDao: TransactionDao):TransactionRepository {
    override fun getTransactions(): Flow<List<Transaction>> {
        val transactionEntities = transactionDao.getTransactions()
        return transactionEntities.map { list -> list.map { Transaction.Mapper.mapToDomain(it) } }
    }
}