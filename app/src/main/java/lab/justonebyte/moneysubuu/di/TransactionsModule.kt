package lab.justonebyte.moneysubuu.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import lab.justonebyte.moneysubuu.data.AppDatabase
import lab.justonebyte.moneysubuu.data.TransactionDao
import lab.justonebyte.moneysubuu.data.TransactionRepository
import lab.justonebyte.moneysubuu.data.TransactionRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
abstract class TransactionsModule {
    @Singleton
    @Provides
    fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao()



    @Binds
    abstract fun bindTransactionRepository(
        transactionRepository: TransactionRepositoryImpl
    ): TransactionRepository
}