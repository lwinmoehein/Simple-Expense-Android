package lab.justonebyte.moneysubuu.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_table")
class TransactionEntity(
    @PrimaryKey()
    @ColumnInfo(name = "unique_id") val unique_id:String,
    @ColumnInfo(name = "amount") val amount: Int,
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "category_id")val category_id:String,
    @ColumnInfo(name = "created_at") val created_at:String,
    @ColumnInfo(name = "version") var version:Int?=1,
    @ColumnInfo(name = "deleted_at") var deleted_at:String?=null,
    @ColumnInfo(name = "note") val note: String?,
    @ColumnInfo(name = "updated_at") var updated_at:String
    )

data class TransactionWithCategory(
   val unique_id: String,
   val amount: Int,
   val type: Int,
   val category_id:String,
   val category_name:String,
   val category_created_at:String,
   val created_at:String,
   val note:String?
)
