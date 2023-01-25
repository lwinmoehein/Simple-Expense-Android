package lab.justonebyte.moneysubuu.di


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import lab.justonebyte.moneysubuu.data.SettingPrefRepository
import lab.justonebyte.moneysubuu.data.SettingPreferencesRepositoryImpl

import javax.inject.Singleton

private const val SETTING_PREFERENCES_NAME = "setting_preferences"

@Module
@InstallIn(SingletonComponent::class)
class SharedPreferencesModule {
    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext context:Context):DataStore<Preferences>{
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences()}
            ),
            migrations = listOf(SharedPreferencesMigration(context, SETTING_PREFERENCES_NAME)),
            scope = CoroutineScope(Dispatchers.IO+ SupervisorJob()),
            produceFile = {context.preferencesDataStoreFile(SETTING_PREFERENCES_NAME)}
        )
    }
}
@Module
@InstallIn(SingletonComponent::class)
abstract class SharedPrefBindingsModule {
    @Binds
    abstract fun provideSettingPreferenceRepo(impl: SettingPreferencesRepositoryImpl): SettingPrefRepository
}