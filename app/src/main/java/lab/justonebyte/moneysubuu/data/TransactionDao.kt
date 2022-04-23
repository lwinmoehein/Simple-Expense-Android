package lab.justonebyte.moneysubuu.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transaction_table ORDER BY created_at ASC")
    fun getTransactions(): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transactionEntity: TransactionEntity)

    @Query("DELETE FROM transaction_table")
    suspend fun deleteAll()
}