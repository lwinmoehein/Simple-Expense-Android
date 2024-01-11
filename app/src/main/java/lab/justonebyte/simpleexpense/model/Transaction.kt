package lab.justonebyte.simpleexpense.model

import lab.justonebyte.simpleexpense.data.TransactionEntity
import lab.justonebyte.simpleexpense.data.TransactionWithCategory
import lab.justonebyte.simpleexpense.utils.getTimestampFromDateTimeString

enum class TransactionType(val value:Int){
    Income(1),
    Expense(2)
}


data class Transaction(
        val unique_id:String,
        val amount:Int,
        val type: TransactionType,
        val category: TransactionCategory,
        val created_at:Long,
        val updated_at:Long,
        val note:String?
    ){
    object  Mapper {
        fun mapToDomain(transactionEntity: TransactionWithCategory):Transaction{
            return Transaction(
                unique_id=transactionEntity.unique_id,
                amount = transactionEntity.amount,
                type = if(transactionEntity.type==1) TransactionType.Income else TransactionType.Expense,
                category = TransactionCategory(
                    name = transactionEntity.category_name,
                    transaction_type = TransactionCategory.Mapper.mapTransactionType(transactionEntity.type),
                    created_at = transactionEntity.category_created_at,
                    updated_at = transactionEntity.category_updated_at,
                    unique_id = transactionEntity.category_id
                ),
                created_at = transactionEntity.created_at,
                updated_at = transactionEntity.updated_at,
                note = transactionEntity.note
            )
        }
        fun mapToEntity(transaction:Transaction):TransactionEntity{
            return TransactionEntity(
                unique_id=  transaction.unique_id,
                amount = transaction.amount,
                type = transaction.type.value,
                category_id=transaction.category.unique_id,
                created_at = transaction.created_at,
                updated_at = transaction.updated_at,
                version = 1,
                note = transaction.note
            )
        }
        fun mapToEntityFromServer(transaction:ServerTransaction):TransactionEntity{
            return TransactionEntity(
                unique_id=  transaction.unique_id,
                amount = transaction.amount,
                type = transaction.type,
                category_id=transaction.category_id,
                created_at = getTimestampFromDateTimeString(transaction.created_at),
                updated_at = getTimestampFromDateTimeString(transaction.updated_at),
                deleted_at = transaction.deleted_at?.let { getTimestampFromDateTimeString(it) },
                version = transaction.version,
                note = transaction.note
            )
        }

    }
}
