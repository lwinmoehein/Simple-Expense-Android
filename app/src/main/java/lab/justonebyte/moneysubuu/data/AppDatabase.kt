package lab.justonebyte.moneysubuu.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.utils.dateFormatter


// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(TransactionEntity::class,CategoryEntity::class), version = 1, exportSchema = false)

public abstract class AppDatabase : RoomDatabase() {

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

        private suspend fun populateCategories(categoryDao: CategoryDao) {
           val dao = this.INSTANCE?.categoryDao()
           dao?.let {
               it.insert(CategoryEntity(name = "Food", transaction_type = TransactionType.Expense.value, created_at = dateFormatter( System.currentTimeMillis())))
               it.insert(CategoryEntity(name = "Transportation", transaction_type = TransactionType.Expense.value, created_at =dateFormatter( System.currentTimeMillis())))
               it.insert(CategoryEntity(name = "Family", transaction_type = TransactionType.Expense.value, created_at = dateFormatter( System.currentTimeMillis())))
               it.insert(CategoryEntity(name = "Groceries", transaction_type = TransactionType.Income.value, created_at = dateFormatter( System.currentTimeMillis())))
               it.insert(CategoryEntity(name = "Education", transaction_type = TransactionType.Income.value, created_at = dateFormatter( System.currentTimeMillis())))
               it.insert(CategoryEntity(name = "Cafe",  transaction_type = TransactionType.Income.value,created_at =dateFormatter( System.currentTimeMillis())))
               it.insert(CategoryEntity(name = "Gifts", transaction_type = TransactionType.Income.value, created_at =dateFormatter( System.currentTimeMillis())))

           }
        }
    }


}