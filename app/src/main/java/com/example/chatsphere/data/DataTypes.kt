package com.example.chatsphere.data

data class UserData(
    var userId: String?="",
    var name: String?="",
    var number: String?="",
    var imageUrl: String?="",
    var about: String?=""
 //   ,var status: String?=""
) {

    fun toMap() = mapOf(
        "userId" to userId,
        "name" to name,
        "number" to number,
        "imageUrl" to imageUrl,
        "About" to about
        //      ,"status" to status
    )

}
data class ChatData(
    var chatId: String?="",
    val user1:ChatUser = ChatUser(),
    val user2:ChatUser = ChatUser()
)
data class ChatUser(
    val userId: String? = "",
    val name: String? = "",
    val imageUrl: String? = "",
    val number: String? = ""
)

data class Message(
    var sendBy: String?="",
    val message: String?="",
    val timeStamp: String?=""
)

data class status(
    val user: ChatUser = ChatUser(),
    val imageUrl: String? = "",
    val timeStamp:Long?=null

)