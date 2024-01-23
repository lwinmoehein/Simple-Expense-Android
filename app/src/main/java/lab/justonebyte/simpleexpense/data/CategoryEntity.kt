package lab.justonebyte.simpleexpense.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
class CategoryEntity(
    @PrimaryKey()
    @ColumnInfo(name = "unique_id") var unique_id:String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "transaction_type") val transaction_type:Int,
    @ColumnInfo(name = "icon_name") val icon_name: String,
    @ColumnInfo(name = "version") var version:Int?=1,
    @ColumnInfo(name = "latest_server_version") val latest_server_version:Int?=1,
    @ColumnInfo(name="created_at") val created_at:Long,
    @ColumnInfo(name="updated_at") val updated_at:Long,
    @ColumnInfo(name = "deleted_at") var deleted_at:Long?=null
)