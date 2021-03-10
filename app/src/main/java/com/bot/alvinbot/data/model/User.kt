package com.bot.alvinbot.data.model

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val imageUrl: String = "",
    val emergencyNumber: String,
    val gender: Boolean = true,
    val profileCompleted: Int = 0
)