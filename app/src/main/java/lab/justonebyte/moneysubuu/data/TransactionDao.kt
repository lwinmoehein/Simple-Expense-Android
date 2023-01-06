package lab.justonebyte.moneysubuu.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import lab.justonebyte.moneysubuu.utils.dateFormatter
import lab.justonebyte.moneysubuu.utils.monthFormatter
import lab.justonebyte.moneysubuu.utils.yearFormatter

@Dao
interface TransactionDao {

    @Query("SELECT transaction_table.id as id," +
            "transaction_table.amount as amount,transaction_table.created_at as created_at," +
            "transaction_table.type as type,transaction_table.category_id as category_id," +
            "transaction_table.created_timestamp as created_timestamp,"+
            "category_table.name as category_name,category_table.created_at as category_created_at" +
            " FROM transaction_table,category_table where category_table.id==transaction_table.category_id" +
            " and date(transaction_table.created_at)==:date" +
            " order by created_timestamp desc")
    fun getTransactions(date:String): Flow<List<TransactionWithCategory>>

    @Query("SELECT transaction_table.id as id," +
            "transaction_table.amount as amount,transaction_table.created_at as created_at," +
            "transaction_table.type as type,transaction_table.category_id as category_id," +
            "transaction_table.created_timestamp as created_timestamp,"+
            "category_table.name as category_name,category_table.created_at as category_created_at" +
            " FROM transaction_table,category_table where category_table.id==transaction_table.category_id" +
            " and  strftime('%Y-%m', transaction_table.created_at)==:month" +
            " order by created_timestamp desc")
    fun getTransactionsByMonth(month:String = monthFormatter(System.currentTimeMillis())): Flow<List<TransactionWithCategory>>

    @Query("SELECT transaction_table.id as id," +
            "transaction_table.amount as amount,transaction_table.created_at as created_at," +
            "transaction_table.created_timestamp as created_timestamp,"+
            "transaction_table.type as type,transaction_table.category_id as category_id," +
            "category_table.name as category_name,category_table.created_at as category_created_at" +
            " FROM transaction_table,category_table where category_table.id==transaction_table.category_id" +
            " and  strftime('%Y', transaction_table.created_at)==:year" +
            " order by created_timestamp desc")
    fun getTransactionsByYear(year:String = yearFormatter(System.currentTimeMillis())): Flow<List<TransactionWithCategory>>

    @Query("SELECT transaction_table.id as id," +
            "transaction_table.amount as amount,transaction_table.created_at as created_at," +
            "transaction_table.created_timestamp as created_timestamp,"+
            "transaction_table.type as type,transaction_table.category_id as category_id," +
            "category_table.name as category_name,category_table.created_at as category_created_at" +
            " FROM transaction_table,category_table where category_table.id==transaction_table.category_id" +
            " order by created_timestamp desc")
    fun getTotalTransactions(): Flow<List<TransactionWithCategory>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transactionEntity: TransactionEntity):Long

    @Update
    suspend fun update(transactionEntity: TransactionEntity)

    @Query("DELETE FROM transaction_table")
    suspend fun deleteAll()

    @Query("DELETE FROM transaction_table where id=:id")
    suspend fun delete(id: Int)
}