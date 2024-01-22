package lab.justonebyte.simpleexpense.data

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import lab.justonebyte.simpleexpense.model.ServerCategory
import lab.justonebyte.simpleexpense.model.TransactionCategory
import javax.inject.Inject

interface CategoryRepository {
    fun getCategories(): Flow<List<TransactionCategory>>
    fun getServerCategories(): List<ServerCategory>
    suspend fun insert(transactionCategory: TransactionCategory)
    suspend fun insertAll(transactions: List<ServerCategory>)
    suspend fun update(transactionCategory: TransactionCategory)
    suspend fun delete(id: String)
    suspend fun deleteAll()
    suspend fun getUniqueIdsWithVersions():List<UniqueIdWithVersion>
}
class  CategoryRepositoryImpl @Inject constructor(val categoryDao: CategoryDao) : CategoryRepository{

    override fun getCategories(): Flow<List<TransactionCategory>> {
        return categoryDao.getCategories().map { list->list.map { TransactionCategory.Mapper.mapToDomain(it) } }
    }

    override fun getServerCategories(): List<ServerCategory> {
        return categoryDao.getAll()
    }


    override suspend fun insert(transactionCategory: TransactionCategory) {
        return categoryDao.insert(TransactionCategory.Mapper.mapToEntity(transactionCategory))
    }
    override suspend fun insertAll(transactions:List<ServerCategory>) {
        return categoryDao.insertAll(transactions.map { TransactionCategory.Mapper.mapToEntityFromServer(it) })
    }

    override suspend fun update(transactionCategory: TransactionCategory) {
        val existingCategory = categoryDao.get(transactionCategory.unique_id)

        val categoryEntity = TransactionCategory.Mapper.mapToEntity(transactionCategory)
        if(existingCategory.version!! <= existingCategory.latest_server_version!!){
            categoryEntity.version = existingCategory.version!! +1
        }else{
            categoryEntity.version = existingCategory.version!!
        }
        categoryEntity.unique_id = existingCategory.unique_id

        categoryDao.update(categoryEntity)
    }

    override suspend fun delete(id: String) {
        val existingCategory = categoryDao.get(id)
        existingCategory.deleted_at = System.currentTimeMillis()
        existingCategory.version = existingCategory.version!!+1;

        categoryDao.update(category = existingCategory)
    }

    override suspend fun deleteAll() {
       categoryDao.deleteAll()
    }

    override suspend fun getUniqueIdsWithVersions(): List<UniqueIdWithVersion>{
        return categoryDao.getUniqueIdsWithVersions()
    }

}