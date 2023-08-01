package es.juntadeandalucia.msspa.saludandalucia.data.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import es.juntadeandalucia.msspa.saludandalucia.data.entities.WalletData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.NotificationData

@Database(entities = [NotificationData::class, WalletData::class], version = 2)
abstract class AppDataBase : RoomDatabase() {

    abstract fun notificationDao(): NotificationDao
    abstract fun covidCertificatesDao(): CovidCertificateDao

    companion object {

        private val migration1to2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS certificates (
                    id TEXT PRIMARY KEY NOT NULL,
                    type INTEGER NOT NULL,
                    jwt TEXT NOT NULL
                    )
                """.trimIndent())
            }
        }

        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java, "AppDataBase.db"
            ).addMigrations(migration1to2)
                .build()
    }
}
