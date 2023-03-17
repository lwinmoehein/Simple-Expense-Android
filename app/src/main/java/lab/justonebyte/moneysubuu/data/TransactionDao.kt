package lab.justonebyte.moneysubuu.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import lab.justonebyte.moneysubuu.model.ServerTransaction
import lab.justonebyte.moneysubuu.utils.dateFormatter
import lab.justonebyte.moneysubuu.utils.monthFormatter
import lab.justonebyte.moneysubuu.utils.weekFormatter
import lab.justonebyte.moneysubuu.utils.yearFormatter

@Dao
interface TransactionDao {

    @Query("SELECT transaction_table.amount as amount,transaction_table.created_at as created_at," +
            "transaction_table.type as type,transaction_table.category_id as category_id," +
            "category_table.name as category_name,category_table.created_at as category_created_at" +
            " FROM transaction_table,category_table where category_table.unique_id==transaction_table.category_id" +
            " and date(transaction_table.created_at)==:date" +
            " and transaction_table.deleted_at is  null")
    fun getTransactions(date:String): Flow<List<TransactionWithCategory>>





    @Query("SELECT transaction_table.amount as amount,transaction_table.created_at as created_at," +
            "transaction_table.type as type,transaction_table.category_id as category_id," +
            "category_table.name as category_name,category_table.created_at as category_created_at" +
            " FROM transaction_table,category_table where category_table.unique_id==transaction_table.category_id" +
            " and  strftime('%W', transaction_table.created_at)==:dateOfTheWeek" +
            " and transaction_table.deleted_at is  null")
    fun getTransactionsByWeek(dateOfTheWeek:String = weekFormatter(System.currentTimeMillis())): Flow<List<TransactionWithCategory>>



    @Query("SELECT  transaction_table.amount as amount,transaction_table.created_at as created_at," +
            "transaction_table.type as type,transaction_table.category_id as category_id," +
            "category_table.name as category_name,category_table.created_at as category_created_at" +
            " FROM transaction_table,category_table where category_table.unique_id==transaction_table.category_id" +
            " and  strftime('%Y-%m', transaction_table.created_at)==:month" +
            " and transaction_table.deleted_at is  null")
    fun getTransactionsByMonth(month:String = monthFormatter(System.currentTimeMillis())): Flow<List<TransactionWithCategory>>




    @Query("SELECT transaction_table.amount as amount,transaction_table.created_at as created_at," +
            "transaction_table.type as type,transaction_table.category_id as category_id," +
            "category_table.name as category_name,category_table.created_at as category_created_at" +
            " FROM transaction_table,category_table where category_table.unique_id==transaction_table.category_id" +
            " and transaction_table.deleted_at is null" +
            " and  strftime('%Y', transaction_table.created_at)==:year" )
    fun getTransactionsByYear(year:String = yearFormatter(System.currentTimeMillis())): Flow<List<TransactionWithCategory>>




    @Query("SELECT transaction_table.amount as amount,transaction_table.created_at as created_at," +
            "transaction_table.type as type,transaction_table.category_id as category_id," +
            "category_table.name as category_name,category_table.created_at as category_created_at" +
            " FROM transaction_table,category_table where category_table.unique_id==transaction_table.category_id and transaction_table.deleted_at is  null" )
    fun getTotalTransactions(): Flow<List<TransactionWithCategory>>

    @Query("SELECT * FROM transaction_table")
    fun getAllTransactions():List<ServerTransaction>



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transactionEntity: TransactionEntity):Long

    @Update
    suspend fun update(transactionEntity: TransactionEntity)

    @Query("DELETE FROM transaction_table")
    suspend fun deleteAll()

    @Query("DELETE FROM transaction_table where unique_id=:id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM transaction_table where unique_id=:id limit 1")
    suspend fun get(id:String): TransactionEntity

    @Query("SELECT unique_id,version from transaction_table")
    suspend fun getUniqueIdsWithVersions(): List<UniqueIdWithVersion>
}