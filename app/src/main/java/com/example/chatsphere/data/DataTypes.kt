package com.example.chatsphere.data

data class UserData(
    var userId: String?="",
    var name: String?="",
    var number: String?="",
    var imageUrl: String?=""
 //   ,var status: String?=""
){

    fun toMap()= mapOf(
        "userId" to userId,
        "name" to name,
        "number" to number,
        "imageUrl" to imageUrl
  //      ,"status" to status
    )
}