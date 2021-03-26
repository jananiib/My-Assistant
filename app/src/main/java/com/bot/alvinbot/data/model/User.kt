package com.bot.alvinbot.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val imageUrl: String = "",
    val emergencyNumber: String,
    val gender: Boolean = true,
    val profileCompleted: Int = 0
)