package lab.justonebyte.moneysubuu.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import lab.justonebyte.moneysubuu.model.ServerCategory
import lab.justonebyte.moneysubuu.model.TransactionCategory
import javax.inject.Inject

interface CategoryRepository {
    fun getCategories(): Flow<List<TransactionCategory>>
    fun getServerCategories(): List<ServerCategory>
    suspend fun insert(transactionCategory: TransactionCategory)
    suspend fun update(transactionCategory: TransactionCategory)
    suspend fun delete(id: Int)
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

    override suspend fun update(transactionCategory: TransactionCategory) {
        val existingCategory = categoryDao.get(transactionCategory.id)

        val categoryEntity = TransactionCategory.Mapper.mapToEntity(transactionCategory)
        if(existingCategory.version!! <= existingCategory.latest_server_version!!){
            categoryEntity.version = existingCategory.version!! +1
        }else{
            categoryEntity.version = existingCategory.version!!
        }
        categoryDao.update(categoryEntity)
    }

    override suspend fun delete(id: Int) {
        var existingCategory = categoryDao.get(id)
        if (existingCategory != null) {
            existingCategory.deleted_at = System.currentTimeMillis()
            categoryDao.update(category = existingCategory)
        }
    }

    override suspend fun getUniqueIdsWithVersions(): List<UniqueIdWithVersion>{
        return categoryDao.getUniqueIdsWithVersions()
    }
}