package lab.justonebyte.simpleexpense.model

data class ServerTransaction(
    val unique_id:String,
    val amount:Int,
    val note:String?,
    val category_id:String,
    val version:Int,
    val type:Int,
    val created_at:Long,
    val updated_at:Long,
    val deleted_at:Long?
)