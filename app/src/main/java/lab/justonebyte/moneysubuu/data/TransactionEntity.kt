package lab.justonebyte.moneysubuu.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_table")
class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id:Double,
    @ColumnInfo(name = "amount") val amount: Double,
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "category_id")val category_id:Double,
    @ColumnInfo(name = "created_at") val created_at:Double
   )

data class TransactionWithCategory(
   val id:Double,
   val amount: Double,
   val type: Int,
   val category_id:Int,
   val category_name:String,
   val created_at:Double
)