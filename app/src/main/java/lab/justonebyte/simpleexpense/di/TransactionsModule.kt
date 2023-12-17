package lab.justonebyte.simpleexpense.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lab.justonebyte.simpleexpense.data.TransactionRepository
import lab.justonebyte.simpleexpense.data.TransactionRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class TransactionsModule {

    @Binds
    abstract fun bindTransactionRepository(
        transactionRepository: TransactionRepositoryImpl
    ): TransactionRepository
}