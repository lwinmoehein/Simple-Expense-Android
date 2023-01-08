package lab.justonebyte.moneysubuu.utils

sealed class TransactionGroup(val index:Int, val title:String){
    object  Daily:TransactionGroup(1,"Daily")
    object Monthly:TransactionGroup(2,"monthly")
    object Yearly:TransactionGroup(3,"Yearly")
    object Total:TransactionGroup(4,"Total")
}