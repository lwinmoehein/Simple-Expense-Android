package lab.justonebyte.moneysubuu.model

import lab.justonebyte.moneysubuu.data.CategoryEntity
import lab.justonebyte.moneysubuu.utils.getCurrentGlobalTime
import java.util.*

data class TransactionCategory(
    val unique_id:String,
    val name:String,
    val transaction_type:TransactionType,
    val created_at:String,
    val updated_at:String
)
{
    object  Mapper {
        fun mapToDomain(categoryEntity: CategoryEntity):TransactionCategory{
            return TransactionCategory(
                unique_id = categoryEntity.unique_id,
                name = categoryEntity.name,
                transaction_type = mapTransactionType(categoryEntity.transaction_type),
                created_at = categoryEntity.created_at,
                updated_at = categoryEntity.updated_at
            )
        }

        fun mapToEntity(transactionCategory: TransactionCategory): CategoryEntity {
                return CategoryEntity(
                    unique_id =  UUID.randomUUID().toString()+"_"+System.currentTimeMillis(),
                    name = transactionCategory.name,
                    created_at = transactionCategory.created_at,
                    transaction_type = transactionCategory.transaction_type.value,
                    updated_at = transactionCategory.updated_at
                )
        }
        fun mapToEntityFromServer(transactionCategory: ServerCategory): CategoryEntity {
            return CategoryEntity(
                unique_id =  transactionCategory.unique_id,
                name = transactionCategory.name,
                created_at = transactionCategory.created_at,
                deleted_at = transactionCategory.deleted_at,
                version = transactionCategory.version,
                transaction_type = transactionCategory.transaction_type,
                updated_at = transactionCategory.updated_at?: getCurrentGlobalTime()
            )
        }
        fun mapTransactionType(type:Int) = if(type==TransactionType.Income.value) TransactionType.Income else TransactionType.Expense
    }
}