package lab.justonebyte.moneysubuu.model

data class ServerCategory(
    val unique_id:String,
    val name:String,
    val version:Int,
    val transaction_type:Int,
    val deleted_at:String?,
    val created_at:String
)