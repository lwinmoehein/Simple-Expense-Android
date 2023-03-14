package lab.justonebyte.moneysubuu.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import lab.justonebyte.moneysubuu.model.ServerCategory

data class UniqueIdWithVersion(val unique_id:String?,val version:Int?)
@Dao
interface CategoryDao {
    @Query("SELECT * FROM category_table where category_table.deleted_at is null ORDER BY created_at DESC")
    fun getCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM category_table where category_table.deleted_at")
     fun getAll(): List<ServerCategory>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(category: CategoryEntity)

    @Update
    suspend fun update(category: CategoryEntity)

    @Query("DELETE FROM category_table where id=:id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM category_table where id=:id limit 1")
    suspend fun get(id:Int): CategoryEntity

    @Query("SELECT unique_id,version from category_table")
    suspend fun getUniqueIdsWithVersions(): List<UniqueIdWithVersion>
}