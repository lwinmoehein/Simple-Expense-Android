package lab.justonebyte.simpleexpense.data

import android.content.Context
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
        it.insert(CategoryEntity(name = "Food & Drinks", unique_id ="food_and_drink", transaction_type = TransactionType.Expense.value, created_at = System.currentTimeMillis(), icon_name = "food_and_drink", updated_at = System.currentTimeMillis()))
        it.insert(CategoryEntity(name = "Shopping",unique_id = "shopping", transaction_type = TransactionType.Expense.value, created_at = System.currentTimeMillis(), icon_name = "shopping", updated_at = System.currentTimeMillis()))
        it.insert(CategoryEntity(name = "Housing",unique_id = "housing", transaction_type = TransactionType.Expense.value, created_at =  System.currentTimeMillis(), icon_name = "housing", updated_at = System.currentTimeMillis()))
        it.insert(CategoryEntity(name = "Travel",unique_id = "travel", transaction_type = TransactionType.Expense.value, created_at =  System.currentTimeMillis(), icon_name = "travel", updated_at = System.currentTimeMillis()))
        it.insert(CategoryEntity(name = "Vehicles",unique_id ="vehicles", transaction_type = TransactionType.Expense.value, created_at =   System.currentTimeMillis(), icon_name = "vehicles", updated_at = System.currentTimeMillis()))
        it.insert(CategoryEntity(name = "Life & Entertainment",unique_id = "life_and_entertainment",  transaction_type = TransactionType.Income.value,created_at = System.currentTimeMillis(), icon_name = "life_and_entertainment", updated_at = System.currentTimeMillis()))
        it.insert(CategoryEntity(name = "IT Devices",unique_id = "it_devices", transaction_type = TransactionType.Expense.value, created_at = System.currentTimeMillis(), icon_name = "it_devices", updated_at = System.currentTimeMillis()))
        it.insert(CategoryEntity(name = "Health",unique_id = "health", transaction_type = TransactionType.Expense.value, created_at = System.currentTimeMillis(), icon_name = "health", updated_at = System.currentTimeMillis()))
        it.insert(CategoryEntity(name = "Donation",unique_id = "donation", transaction_type = TransactionType.Expense.value, created_at = System.currentTimeMillis(), icon_name = "donation", updated_at = System.currentTimeMillis()))
        it.insert(CategoryEntity(name = "Other",unique_id = "other", transaction_type = TransactionType.Expense.value, created_at = System.currentTimeMillis(), icon_name = "other", updated_at = System.currentTimeMillis()))

        it.insert(CategoryEntity(name = "Allowance", unique_id ="allowance", transaction_type = TransactionType.Income.value, created_at = System.currentTimeMillis(), icon_name = "allowance", updated_at = System.currentTimeMillis()))
        it.insert(CategoryEntity(name = "Salary",unique_id = "salary", transaction_type = TransactionType.Income.value, created_at = System.currentTimeMillis(), icon_name = "salary", updated_at = System.currentTimeMillis()))
        it.insert(CategoryEntity(name = "Bonus",unique_id = "bonus", transaction_type = TransactionType.Income.value, created_at =  System.currentTimeMillis(), icon_name = "bonus", updated_at = System.currentTimeMillis()))
        it.insert(CategoryEntity(name = "Other",unique_id = "other", transaction_type = TransactionType.Income.value, created_at =  System.currentTimeMillis(), icon_name = "other", updated_at = System.currentTimeMillis()))
    }
}
