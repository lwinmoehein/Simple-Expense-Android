package lab.justonebyte.moneysubuu.model

import lab.justonebyte.moneysubuu.data.TransactionEntity
import lab.justonebyte.moneysubuu.data.TransactionWithCategory
import lab.justonebyte.moneysubuu.utils.dateFormatter

enum class TransactionType(val value:Int){
    Income(1),
    Expense(2)
}

data class Transaction(val id:Int?=null,val amount:Int,val type: TransactionType,val category: TransactionCategory,val created_at:String){
    object  Mapper {
        fun mapToDomain(transactionEntity: TransactionWithCategory):Transaction{
            return Transaction(
                id = transactionEntity.id,
                amount = transactionEntity.amount,
                type = if(transactionEntity.type==1) TransactionType.Income else TransactionType.Expense,
                category = TransactionCategory(
                    id = transactionEntity.category_id,
                    name = transactionEntity.category_name,
                    transaction_type = TransactionCategory.Mapper.mapTransactionType(transactionEntity.type),
                    created_at = transactionEntity.category_created_at
                ),
                created_at = transactionEntity.created_at
            )
        }
        fun mapToEntity(transaction:Transaction):TransactionEntity{
            return TransactionEntity(
                amount = transaction.amount,
                type = transaction.type.value,
                category_id=transaction.category.id,
                created_at = transaction.created_at
            )
        }
    }
}