package com.easyhi.manage.data

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.easyhi.manage.BuildConfig
import com.easyhi.manage.data.bean.DeviceInfo
import com.easyhi.manage.data.bean.TestBean
import com.easyhi.manage.util.PasswordGenerator
import com.easyhi.manage.util.SpUtil
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(
    entities = [TestBean::class, DeviceInfo::class],
    version = 1, exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun deviceDao(): DeviceDao


    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            if (BuildConfig.BUILD_TYPE == "prod" || BuildConfig.BUILD_TYPE == "release") {
                val password: String = if (SpUtil.getSp().contains("password")) {
                    SpUtil.getSp().getString("password", "")!!
                } else {
                    val p = PasswordGenerator.getRandomSpecialChars(6)
                    SpUtil.getSp().edit().putString("password", p).apply()
                    p
                }

                val passPhrase = SQLiteDatabase.getBytes(password.toCharArray())

                return Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "zhsh-user-data-encrypt"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                        }
                    })
                    .openHelperFactory(SupportFactory(passPhrase))
                    .build()
            } else {

                return Room.databaseBuilder(context, AppDatabase::class.java, "zhsh-user-data")
                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_3_4)
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                        }
                    })
                    .build()
            }

        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("alter table AccountInfo add Column familyNums TEXT NOT NULL DEFAULT ''")
            }
        }

    }
}
