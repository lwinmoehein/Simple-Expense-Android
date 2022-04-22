package lab.justonebyte.moneysubuu.model

enum class TransactionType{
    Income,Expense
}
data class Transaction(val id:Double,val amount:Double,val type: TransactionType,val category: TransactionCategory)