package lab.justonebyte.simpleexpense.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import lab.justonebyte.simpleexpense.model.ServerCategory
import lab.justonebyte.simpleexpense.model.TransactionCategory
import lab.justonebyte.simpleexpense.utils.getCurrentGlobalTime
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
    suspend fun populateCategories()
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
    override suspend fun insertAll(categoriesList:List<ServerCategory>) {
        return categoryDao.insertAll(categoriesList.map { TransactionCategory.Mapper.mapToEntityFromServer(it) })
    }

    override suspend fun update(transactionCategory: TransactionCategory) {
        val existingCategory = categoryDao.get(transactionCategory.unique_id)

        val categoryEntity = TransactionCategory.Mapper.mapToEntity(transactionCategory)
        if(existingCategory.version!! <= existingCategory.latest_server_version!!){
            categoryEntity.version = existingCategory.version!! +1
            categoryEntity.updated_at = getCurrentGlobalTime()
        }else{
            categoryEntity.version = existingCategory.version!!
        }
        categoryEntity.unique_id = existingCategory.unique_id

        categoryDao.update(categoryEntity)
    }

    override suspend fun delete(id: String) {
        var existingCategory = categoryDao.get(id)
        if (existingCategory != null) {
            existingCategory.deleted_at = getCurrentGlobalTime()
            existingCategory.version = existingCategory.version!!+1;
            existingCategory.updated_at = getCurrentGlobalTime()

            categoryDao.update(category = existingCategory)
        }
    }

    override suspend fun deleteAll() {
       categoryDao.deleteAll()
    }

    override suspend fun getUniqueIdsWithVersions(): List<UniqueIdWithVersion>{
        return categoryDao.getUniqueIdsWithVersions()
    }

    override suspend fun populateCategories() {
       populateCategories(categoryDao)
    }
}