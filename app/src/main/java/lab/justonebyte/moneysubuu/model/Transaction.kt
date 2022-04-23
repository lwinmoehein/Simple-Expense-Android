package lab.justonebyte.moneysubuu.model

enum class TransactionType(val value:Int){
    Income(1),
    Expense(2)
}

data class Transaction(val id:Double,val amount:Double,val type: TransactionType,val category: TransactionCategory)