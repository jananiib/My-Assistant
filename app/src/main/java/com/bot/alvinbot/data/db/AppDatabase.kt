package com.bot.alvinbot.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bot.alvinbot.data.model.ChatUser

@Database(
    entities = [ChatUser::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun IChatDao(): IChatDao

}