package lab.justonebyte.simpleexpense.model

data class ServerCategory(
    val unique_id:String,
    val name:String,
    val version:Int,
    val transaction_type:Int,
    val deleted_at:String?,
    val created_at:String,
    val updated_at:String,
    val photo_url:String?,
    val is_default:Int?,
)

