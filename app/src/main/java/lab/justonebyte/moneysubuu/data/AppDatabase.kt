package lab.justonebyte.moneysubuu.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


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
               it.insert(CategoryEntity(name = "Food", created_at = System.currentTimeMillis().toDouble()))
               it.insert(CategoryEntity(name = "Transportation", created_at = System.currentTimeMillis().toDouble()))
               it.insert(CategoryEntity(name = "Family", created_at = System.currentTimeMillis().toDouble()))
               it.insert(CategoryEntity(name = "Groceries", created_at = System.currentTimeMillis().toDouble()))
               it.insert(CategoryEntity(name = "Education", created_at = System.currentTimeMillis().toDouble()))
               it.insert(CategoryEntity(name = "Cafe", created_at = System.currentTimeMillis().toDouble()))
               it.insert(CategoryEntity(name = "Gifts", created_at = System.currentTimeMillis().toDouble()))

           }
        }
    }


}