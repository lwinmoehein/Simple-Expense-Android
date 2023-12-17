package lab.justonebyte.simpleexpense.utils

sealed class TransactionGroup(val index:Int, val title:String){
    object  Daily:TransactionGroup(1,"This day")
    object Monthly:TransactionGroup(2,"This month")
    object Yearly:TransactionGroup(3,"This year")
    object Total:TransactionGroup(4,"Total")
    object Week:TransactionGroup(4,"This week")

}