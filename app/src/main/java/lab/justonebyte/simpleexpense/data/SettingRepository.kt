package lab.justonebyte.simpleexpense.data

import kotlinx.coroutines.flow.Flow

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.map
import lab.justonebyte.simpleexpense.data.SettingPreferencesRepositoryImpl.PreferencesKeys.ACCESS_TOKEN
import lab.justonebyte.simpleexpense.data.SettingPreferencesRepositoryImpl.PreferencesKeys.DEFAULT_BALANCE_TYPE
import lab.justonebyte.simpleexpense.data.SettingPreferencesRepositoryImpl.PreferencesKeys.DOWNLOAD_FOLDER
import lab.justonebyte.simpleexpense.data.SettingPreferencesRepositoryImpl.PreferencesKeys.IS_FIRST_TIME
import lab.justonebyte.simpleexpense.data.SettingPreferencesRepositoryImpl.PreferencesKeys.LOCALE
import lab.justonebyte.simpleexpense.data.SettingPreferencesRepositoryImpl.PreferencesKeys.SELECTED_CURRENCY
import lab.justonebyte.simpleexpense.data.SettingPreferencesRepositoryImpl.PreferencesKeys.THEME
import lab.justonebyte.simpleexpense.model.AppLocale
import lab.justonebyte.simpleexpense.model.BalanceType
import lab.justonebyte.simpleexpense.model.Currency
import lab.justonebyte.simpleexpense.model.Theme
import javax.inject.Inject



interface SettingPrefRepository {

    val selectedTheme: Flow<Int>
    val selectedLocale: Flow<String>
    val defaultBalanceType: Flow<Int>
    val selectedCurrency: Flow<Int>
    val isAppIntroduced:Flow<Boolean>
    val accessToken:Flow<String>
    val downloadFolder:Flow<String>

    suspend fun updateTheme(theme: Int)
    suspend fun updateLocale(locale:String)
    suspend fun updateBalanceType(type:Int)
    suspend fun updateIsAppIntroduced(introduced:Boolean)
    suspend fun updateSelectedCurrency(currency:Int)
    suspend fun updateToken(token: String)
    suspend fun updateDownloadFolder(folderUri: String)


}

class SettingPreferencesRepositoryImpl @Inject constructor
    (
    private val dataStore: DataStore<Preferences>
):SettingPrefRepository{
    private object PreferencesKeys {
        val THEME = intPreferencesKey("theme")
        val IS_FIRST_TIME = booleanPreferencesKey("is_first_time")
        val LOCALE = stringPreferencesKey("locale")
        val DEFAULT_BALANCE_TYPE = intPreferencesKey("balance_type")
        val SELECTED_CURRENCY = intPreferencesKey("currency")
        val ACCESS_TOKEN = stringPreferencesKey("token")
        val DOWNLOAD_FOLDER  = stringPreferencesKey("download_folder")
    }

    override val selectedTheme: Flow<Int> = dataStore.data.map { it[THEME] ?: Theme.SYSTEM.id }
    override val selectedLocale: Flow<String> = dataStore.data.map { it[LOCALE] ?: AppLocale.English.value }
    override val isAppIntroduced: Flow<Boolean> = dataStore.data.map { it[IS_FIRST_TIME]?:false }
    override val defaultBalanceType: Flow<Int> = dataStore.data.map { it[DEFAULT_BALANCE_TYPE]?:BalanceType.MONTHLY.value }
    override val selectedCurrency: Flow<Int> = dataStore.data.map { it[SELECTED_CURRENCY]?:Currency.Kyat.value }
    override val accessToken: Flow<String> = dataStore.data.map { it[ACCESS_TOKEN]?:"" }
    override val downloadFolder: Flow<String> = dataStore.data.map { it[DOWNLOAD_FOLDER]?:"" }
    override suspend fun updateTheme(theme: Int) {
        dataStore.edit { preferences ->
            preferences[THEME] = theme
        }
    }

    override suspend fun updateLocale(locale:String) {
        dataStore.edit { preferences ->
            preferences[LOCALE] = locale
        }
    }

    override suspend fun updateBalanceType(type: Int) {
        dataStore.edit { preferences ->
            preferences[DEFAULT_BALANCE_TYPE] = type
        }
    }

    override suspend fun updateIsAppIntroduced(introduced: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_TIME] = introduced
        }
    }

    override suspend fun updateSelectedCurrency(currency: Int) {
        dataStore.edit { preferences ->
            preferences[SELECTED_CURRENCY] = currency
        }
    }
    override suspend fun updateToken(token:String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = token
        }
    }

    override suspend fun updateDownloadFolder(folderUri: String) {
        dataStore.edit { preferences ->
            preferences[DOWNLOAD_FOLDER] = folderUri
        }
    }

}