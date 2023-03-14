package lab.justonebyte.moneysubuu.model

import io.grpc.Server
import lab.justonebyte.moneysubuu.data.CategoryEntity
import lab.justonebyte.moneysubuu.data.TransactionEntity
import lab.justonebyte.moneysubuu.data.TransactionWithCategory
import java.util.*

data class TransactionCategory(

    val id:Int,
    val name:String,
    val transaction_type:TransactionType,
    val created_at:Long

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
        fun mapToServer(categoryEntity: CategoryEntity):ServerCategory{
            return ServerCategory(
                unique_id= categoryEntity.unique_id!!,
                name = categoryEntity.name,
                transaction_type = categoryEntity.transaction_type,
                version = categoryEntity.version!!
            )
        }
        fun mapToEntity(transactionCategory: TransactionCategory): CategoryEntity {
            if(transactionCategory.id==0){
                return CategoryEntity(
                    unique_id =  UUID.randomUUID().toString()+"_"+transactionCategory.created_at,
                    name = transactionCategory.name,
                    created_at = transactionCategory.created_at,
                    transaction_type = transactionCategory.transaction_type.value
                )
            }
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