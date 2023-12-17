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
    @ColumnInfo(name="created_at") val created_at:String,
    @ColumnInfo(name = "version") var version:Int?=1,
    @ColumnInfo(name = "latest_server_version") val latest_server_version:Int?=1,
    @ColumnInfo(name = "deleted_at") var deleted_at:String?=null,
    @ColumnInfo(name = "updated_at") var updated_at:String,

    )