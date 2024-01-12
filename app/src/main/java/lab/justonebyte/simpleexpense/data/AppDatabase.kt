package lab.justonebyte.simpleexpense.data

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.model.TransactionType


// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(TransactionEntity::class,CategoryEntity::class), version = 1, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context,scope: CoroutineScope): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "su_buu_database"
                ).addCallback( object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                                        INSTANCE?.let { database ->
                    scope.launch {
                        populateCategories(database.categoryDao())
                    }
                }
                    }
                })
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
suspend fun populateCategories(dao: CategoryDao) {
    dao.let {
        it.insert(CategoryEntity(name = "မနက်စာ", unique_id ="one", transaction_type = TransactionType.Expense.value, created_at = System.currentTimeMillis(), updated_at = System.currentTimeMillis()))
        it.insert(CategoryEntity(name = "ကားခ",unique_id = "two", transaction_type = TransactionType.Expense.value, created_at = System.currentTimeMillis(), updated_at = System.currentTimeMillis()))
        it.insert(CategoryEntity(name = "အိမ်ပေး",unique_id = "three", transaction_type = TransactionType.Expense.value, created_at =  System.currentTimeMillis(), updated_at = System.currentTimeMillis()))
        it.insert(CategoryEntity(name = "လစာ",unique_id = "Foru", transaction_type = TransactionType.Income.value, created_at =  System.currentTimeMillis(), updated_at = System.currentTimeMillis()))
        it.insert(CategoryEntity(name = "မုန့်ဖိုး",unique_id ="five", transaction_type = TransactionType.Income.value, created_at =   System.currentTimeMillis(), updated_at = System.currentTimeMillis()))
        it.insert(CategoryEntity(name = "ဘောနပ်စ်",unique_id = "six",  transaction_type = TransactionType.Income.value,created_at = System.currentTimeMillis(), updated_at = System.currentTimeMillis()))
        it.insert(CategoryEntity(name = "လက်ဆောင်",unique_id = "seven", transaction_type = TransactionType.Income.value, created_at = System.currentTimeMillis(), updated_at = System.currentTimeMillis()))

    }
}