package com.example.mymessenger.models

data class User(
    val id: String = "",
    var login: String = "",
    var username: String = "",
    var bio: String = "",
    var status: String = "",
    var photoUrl: String = ""
)