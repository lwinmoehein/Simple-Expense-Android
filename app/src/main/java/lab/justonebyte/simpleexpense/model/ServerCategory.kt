package lab.justonebyte.simpleexpense.model

data class ServerCategory(
    val unique_id:String,
    val name:String,
    val version:Int,
    val transaction_type:Int,
    val deleted_at:Long?,
    val created_at:Long,
    val updated_at:Long,
    val photo_url:String?
)

