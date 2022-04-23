package lab.justonebyte.moneysubuu.model

import lab.justonebyte.moneysubuu.data.TransactionEntity
import lab.justonebyte.moneysubuu.data.TransactionWithCategory

enum class TransactionType(val value:Int){
    Income(1),
    Expense(2)
}

data class Transaction(val id:Double,val amount:Double,val type: TransactionType,val category: TransactionCategory,val created_at:Double){
    object  Mapper {
        fun mapToDomain(transactionEntity: TransactionWithCategory):Transaction{
            return Transaction(
                id = transactionEntity.id,
                amount = transactionEntity.amount,
                type = if(transactionEntity.type==1) TransactionType.Income else TransactionType.Expense,
                category = TransactionCategory(id = transactionEntity.category_id,transactionEntity.category_name),
                created_at = transactionEntity.created_at
            )
        }
    }
}