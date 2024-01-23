package lab.justonebyte.simpleexpense.model

import lab.justonebyte.simpleexpense.data.CategoryEntity
import lab.justonebyte.simpleexpense.utils.getTimestampFromDateTimeString
import java.util.UUID

data class TransactionCategory(
    val unique_id:String,
    val name:String,
    val icon_name:String,
    val transaction_type:TransactionType,
    val created_at:Long,
    val updated_at:Long
)
{
    object  Mapper {
        fun mapToDomain(categoryEntity: CategoryEntity):TransactionCategory{
            return TransactionCategory(
                unique_id = categoryEntity.unique_id,
                name = categoryEntity.name,
                icon_name = categoryEntity.icon_name,
                transaction_type = mapTransactionType(categoryEntity.transaction_type),
                created_at = categoryEntity.created_at,
                updated_at = categoryEntity.updated_at
            )
        }

        fun mapToEntity(transactionCategory: TransactionCategory): CategoryEntity {
                return CategoryEntity(
                    unique_id =  UUID.randomUUID().toString()+"_"+System.currentTimeMillis(),
                    name = transactionCategory.name,
                    icon_name = transactionCategory.icon_name,
                    created_at = transactionCategory.created_at,
                    updated_at = transactionCategory.updated_at,
                    transaction_type = transactionCategory.transaction_type.value
                )
        }
        fun mapToEntityFromServer(transactionCategory: ServerCategory): CategoryEntity {
            return CategoryEntity(
                unique_id =  transactionCategory.unique_id,
                name = transactionCategory.name,
                icon_name = transactionCategory.icon_name,
                created_at = getTimestampFromDateTimeString(transactionCategory.created_at),
                updated_at = getTimestampFromDateTimeString(transactionCategory.updated_at),
                deleted_at = transactionCategory.deleted_at?.let { getTimestampFromDateTimeString(it) },
                version = transactionCategory.version,
                transaction_type = transactionCategory.transaction_type
            )
        }
        fun mapTransactionType(type:Int) = if(type==TransactionType.Income.value) TransactionType.Income else TransactionType.Expense
    }
}