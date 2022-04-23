package lab.justonebyte.moneysubuu.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT transaction_table.id as id,transaction_table.amount as amount,transaction_table.created_at as created_at,transaction_table.type as type,transaction_table.category_id as category_id,category_table.name as category_name,category_table.created_at as category_created_at FROM transaction_table,category_table where category_table.id==transaction_table.category_id")
    fun getTransactions(): Flow<List<TransactionWithCategory>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transactionEntity: TransactionEntity):Long

    @Query("DELETE FROM transaction_table")
    suspend fun deleteAll()
}