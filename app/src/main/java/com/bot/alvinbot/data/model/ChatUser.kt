package com.bot.alvinbot.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ChatUser(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var content: String?,
    var isMine: Boolean?
)