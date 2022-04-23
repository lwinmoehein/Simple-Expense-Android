package lab.justonebyte.moneysubuu.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import lab.justonebyte.moneysubuu.data.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext app: Context
    ) = AppDatabase.getDatabase(context = app)



}