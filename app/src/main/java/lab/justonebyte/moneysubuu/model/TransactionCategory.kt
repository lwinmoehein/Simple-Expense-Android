package lab.justonebyte.moneysubuu.model

import androidx.compose.ui.util.unpackInt1
import lab.justonebyte.moneysubuu.data.CategoryEntity
import java.util.*

data class TransactionCategory(
    val unique_id:String,
    val name:String,
    val transaction_type:TransactionType,
    val created_at:String,
)
{
    object  Mapper {
        fun mapToDomain(categoryEntity: CategoryEntity):TransactionCategory{
            return TransactionCategory(
                unique_id = categoryEntity.unique_id,
                name = categoryEntity.name,
                transaction_type = mapTransactionType(categoryEntity.transaction_type),
                created_at = categoryEntity.created_at
            )
        }

        fun mapToEntity(transactionCategory: TransactionCategory): CategoryEntity {
                return CategoryEntity(
                    unique_id =  UUID.randomUUID().toString()+"_"+transactionCategory.created_at,
                    name = transactionCategory.name,
                    created_at = transactionCategory.created_at,
                    transaction_type = transactionCategory.transaction_type.value
                )
        }
        fun mapTransactionType(type:Int) = if(type==TransactionType.Income.value) TransactionType.Income else TransactionType.Expense
    }
}