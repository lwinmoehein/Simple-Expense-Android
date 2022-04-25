package lab.justonebyte.moneysubuu.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id:Int?=null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "transaction_type") val transaction_type:Int,
    @ColumnInfo(name="created_at") val created_at:String
)