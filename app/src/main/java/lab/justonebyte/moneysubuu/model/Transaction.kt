package lab.justonebyte.moneysubuu.model

import lab.justonebyte.moneysubuu.data.CategoryEntity
import lab.justonebyte.moneysubuu.data.TransactionEntity
import lab.justonebyte.moneysubuu.data.TransactionWithCategory
import lab.justonebyte.moneysubuu.utils.dateFormatter
import java.util.*

enum class TransactionType(val value:Int){
    Income(1),
    Expense(2)
}
data class TransactionDTO(
    val id: Int?,
    val global_id:String,
    val amount:Int,
    val type:Int,
    val category_id:Int,
    val created_at:String
)

data class Transaction(
        val id:Int?=null,
        val amount:Int,
        val type: TransactionType,
        val category: TransactionCategory,
        val created_at:String
    ){
    object  Mapper {
        fun mapToDomain(transactionEntity: TransactionWithCategory):Transaction{
            return Transaction(
                id = transactionEntity.id,
                amount = transactionEntity.amount,
                type = if(transactionEntity.type==1) TransactionType.Income else TransactionType.Expense,
                category = TransactionCategory(
                    id = 0,
                    name = transactionEntity.category_name,
                    transaction_type = TransactionCategory.Mapper.mapTransactionType(transactionEntity.type),
                    created_at = transactionEntity.category_created_at,
                    unique_id = transactionEntity.category_id
                ),
                created_at = transactionEntity.created_at
            )
        }
        fun mapToServer(transactionEntity: TransactionEntity):ServerTransaction{
            return ServerTransaction(
                unique_id= transactionEntity.unique_id!!,
                amount = transactionEntity.amount,
                category_id = transactionEntity.unique_id!!,
                version = transactionEntity.version!!,
                note = transactionEntity.note ?: "",
                type = transactionEntity.type,
                created_at = transactionEntity.created_at
            )
        }
        fun mapToEntity(transaction:Transaction):TransactionEntity{
            return TransactionEntity(
                id = transaction.id,
                unique_id=  UUID.randomUUID().toString()+"_"+transaction.id,
                amount = transaction.amount,
                type = transaction.type.value,
                category_id=transaction.category.unique_id,
                created_at = transaction.created_at,
                version = 1,
                note = ""
            )
        }
        fun mapToDTO(transaction: Transaction):TransactionDTO{
            return TransactionDTO(
                id = transaction.id,
                global_id =  UUID.randomUUID().toString()+"_"+transaction.id,
                amount = transaction.amount,
                type = transaction.type.value,
                category_id=transaction.category.id,
                created_at = transaction.created_at
            )
        }

    }
}
