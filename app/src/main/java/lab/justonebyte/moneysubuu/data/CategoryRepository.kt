package lab.justonebyte.moneysubuu.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import lab.justonebyte.moneysubuu.model.TransactionCategory
import javax.inject.Inject

interface CategoryRepository {
    fun getCategories(): Flow<List<TransactionCategory>>
    suspend fun insert(transactionCategory: TransactionCategory)
    suspend fun update(transactionCategory: TransactionCategory)
    suspend fun delete(id: Int)

}
class  CategoryRepositoryImpl @Inject constructor(val categoryDao: CategoryDao) : CategoryRepository{
    override fun getCategories(): Flow<List<TransactionCategory>> {
        return categoryDao.getCategories().map { list->list.map { TransactionCategory.Mapper.mapToDomain(it) } }
    }


    override suspend fun insert(transactionCategory: TransactionCategory) {
        return categoryDao.insert(TransactionCategory.Mapper.mapToEntity(transactionCategory))
    }

    override suspend fun update(transactionCategory: TransactionCategory) {
        val existingCategory = categoryDao.get(1)

        val categoryEntity = TransactionCategory.Mapper.mapToEntity(transactionCategory)
        if(existingCategory.version!! <= existingCategory.latest_server_version!!){
            categoryEntity.version = existingCategory.version!! +1
        }else{
            categoryEntity.version = existingCategory.version!!
        }
        categoryDao.update(categoryEntity)
    }

    override suspend fun delete(id: Int) {
        categoryDao.delete(id)
    }
}