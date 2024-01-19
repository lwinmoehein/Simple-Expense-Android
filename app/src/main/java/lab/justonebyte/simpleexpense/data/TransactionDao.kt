package lab.justonebyte.simpleexpense.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import lab.justonebyte.simpleexpense.model.ServerTransaction
import lab.justonebyte.simpleexpense.utils.getCurrentMonth
import lab.justonebyte.simpleexpense.utils.getCurrentYear


const val transactionWithCategorySelectQuery = "SELECT transaction_table.unique_id as unique_id, transaction_table.amount as amount,transaction_table.created_at as created_at," +"transaction_table.updated_at as updated_at,"+
        "transaction_table.type as type,transaction_table.category_id as category_id," +
        "transaction_table.note as note,"+
        "category_table.name as category_name,category_table.created_at as category_created_at" +",category_table.updated_at as category_updated_at"+
        " FROM transaction_table,category_table "
@Dao
interface TransactionDao {

    @Query(transactionWithCategorySelectQuery+"where category_table.unique_id==transaction_table.category_id" +
            " and date(transaction_table.created_at)==:date" +
            " and transaction_table.deleted_at is  null")
    fun getTransactions(date:String): List<TransactionWithCategory>



    @Query(
        transactionWithCategorySelectQuery+"where category_table.unique_id==transaction_table.category_id" +
                " and transaction_table.created_at BETWEEN :startYearTimeStamp AND :endYearTimestamp" +
            " and transaction_table.deleted_at is  null")
    suspend fun getTransactionsByMonth(startYearTimeStamp:Long,endYearTimestamp:Long): List<TransactionWithCategory>



    @Query(
        transactionWithCategorySelectQuery+"where category_table.unique_id==transaction_table.category_id" +
            " and transaction_table.deleted_at is null" +
                " and transaction_table.created_at BETWEEN :startYearTimeStamp AND :endYearTimestamp"
    )
    fun getTransactionsByYear(startYearTimeStamp:Long,endYearTimestamp:Long): List<TransactionWithCategory>




    @Query(transactionWithCategorySelectQuery+"where category_table.unique_id==transaction_table.category_id and transaction_table.deleted_at is  null" )
    fun getTotalTransactions(): List<TransactionWithCategory>

    @Query("SELECT * FROM transaction_table")
    fun getAllTransactions():List<ServerTransaction>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transactionEntity: TransactionEntity):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactionEntity: List<TransactionEntity>)

    @Update
    suspend fun update(transactionEntity: TransactionEntity)

    @Query("DELETE FROM transaction_table")
    suspend fun deleteAll()

    @Query("DELETE FROM transaction_table where unique_id=:id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM transaction_table where unique_id=:id limit 1")
    suspend fun get(id:String): TransactionEntity

    @Query("SELECT unique_id,version,updated_at from transaction_table")
    suspend fun getUniqueIdsWithVersions(): List<UniqueIdWithVersion>
}