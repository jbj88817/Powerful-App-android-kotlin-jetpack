package us.bojie.paa.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import us.bojie.paa.model.AccountProperties
import us.bojie.paa.model.AuthToken

@Database(entities = [AuthToken::class, AccountProperties::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAuthTokenDao(): AuthTokenDao
    abstract fun getAccountPropertiesDao(): AccountPropertiesDao
    companion object {
        const val DATABASE_NAME = "app_db"
    }
}