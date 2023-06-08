package com.example.mymessenger.models

data class UserModel(
    val id: String = "",
    var login: String = "",
    var username: String = "",
    var bio: String = "",
    var state: String = "",
    var photoUrl: String = "empty"
)