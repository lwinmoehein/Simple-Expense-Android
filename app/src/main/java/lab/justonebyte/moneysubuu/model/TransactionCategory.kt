package lab.justonebyte.moneysubuu.model

import lab.justonebyte.moneysubuu.data.CategoryEntity
import lab.justonebyte.moneysubuu.data.TransactionEntity
import lab.justonebyte.moneysubuu.data.TransactionWithCategory

data class TransactionCategory(

    val id:Int,
    val name:String,
    val transaction_type:TransactionType,
    val created_at:String

)
{
    object  Mapper {
        fun mapToDomain(categoryEntity: CategoryEntity):TransactionCategory{
            return TransactionCategory(
                id = categoryEntity.id?:0,
                name = categoryEntity.name,
                transaction_type = mapTransactionType(categoryEntity.transaction_type),
                created_at = categoryEntity.created_at
            )
        }
        fun mapToEntity(transactionCategory: TransactionCategory): CategoryEntity {
            return CategoryEntity(
                id = transactionCategory.id,
                name = transactionCategory.name,
                created_at = transactionCategory.created_at,
                transaction_type = transactionCategory.transaction_type.value
            )
        }
        fun mapTransactionType(type:Int) = if(type==TransactionType.Income.value) TransactionType.Income else TransactionType.Expense
    }
}