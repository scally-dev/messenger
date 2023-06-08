package com.example.mymessenger.models

data class CommonModel(
    val id: String = "",
    var login: String = "",
    var username: String = "",
    var bio: String = "",
    var state: String = "",
    var photoUrl: String = "empty",


    var text: String = "",
    var type: String = "",
    var from: String = "",
    var timeStamp: Any = ""
)