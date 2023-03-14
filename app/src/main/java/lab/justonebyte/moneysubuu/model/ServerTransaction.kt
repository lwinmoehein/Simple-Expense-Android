package lab.justonebyte.moneysubuu.model

data class ServerTransaction(
    val unique_id:String,
    val amount:Int,
    val note:String,
    val category_id:String,
    val version:Int,
    val type:Int
)