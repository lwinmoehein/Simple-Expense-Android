package lab.justonebyte.moneysubuu.model

data class ServerTransaction(
    val unique_id:String,
    val amount:Int,
    val note:String?,
    val category_id:String,
    val version:Int,
    val type:Int,
    val created_at:String,
    val updated_at:String,
    val deleted_at:String?
)