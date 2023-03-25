package lab.justonebyte.moneysubuu.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import lab.justonebyte.moneysubuu.model.ServerCategory

data class UniqueIdWithVersion(val unique_id:String?,val version:Int?,val updated_at:String)
@Dao
interface CategoryDao {
    @Query("SELECT * FROM category_table where category_table.deleted_at is null ORDER BY created_at DESC")
    fun getCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM category_table")
     fun getAll(): List<ServerCategory>


    @Query("DELETE FROM category_table")
    suspend fun deleteAll()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoryEntity>)

    @Update
    suspend fun update(category: CategoryEntity)

    @Query("DELETE FROM category_table where unique_id=:id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM category_table where unique_id=:id limit 1")
    suspend fun get(id:String): CategoryEntity

    @Query("SELECT unique_id,version,updated_at from category_table")
    suspend fun getUniqueIdsWithVersions(): List<UniqueIdWithVersion>
}