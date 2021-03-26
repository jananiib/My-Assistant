package com.bot.alvinbot.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bot.alvinbot.data.model.ChatUser

@Dao
interface IChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChat(sosListData: ChatUser)


    @Query("SELECT * FROM ChatUser")
    fun getChatListData(): LiveData<List<ChatUser>>


    @Query("DELETE FROM ChatUser")
    fun deleteChatData()

}