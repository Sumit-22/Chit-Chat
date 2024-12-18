package com.example.chatsphere

import android.net.Uri
import android.net.http.UrlRequest
import android.util.Log
import androidx.collection.emptyLongSet
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.example.chatsphere.data.CHATS
import com.example.chatsphere.data.ChatData
import com.example.chatsphere.data.ChatUser
import com.example.chatsphere.data.Event
import com.example.chatsphere.data.MESSAGE
import com.example.chatsphere.data.Message
import com.example.chatsphere.data.STATUS
import com.example.chatsphere.data.USER_NODE
import com.example.chatsphere.data.UserData
import com.example.chatsphere.data.status
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LCViewModel @Inject constructor(
    val auth : FirebaseAuth,
    var db: FirebaseFirestore,
    val storage : FirebaseStorage

): ViewModel() {
    var inProcess = mutableStateOf<Boolean>(false)

    var inProcessChats = mutableStateOf<Boolean>(false)
    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    // why we use this  variable thinnk about this right :-- it's for line 45 , think about constructor of userData
    val userData = mutableStateOf<UserData?>(null)
    val chats = mutableStateOf<List<ChatData>>(listOf())

    val chatMessages = mutableStateOf<List<Message>>(listOf())
    val inProgressChatMessage = mutableStateOf(false)
    var currentChatMessageListener :ListenerRegistration?= null //why we use this

    val status = mutableStateOf<List<status>>(listOf())
    val inProgressStatus = mutableStateOf(false)
    init{
// before declaring chatlist screen we left this block empty as app requires this , but now we have to make currentUser for chatlist screen
        val currentUser =auth.currentUser
        currentUser?.uid?.let{
            getUserData(it)
        }
    }

fun populateMessages(chatId:String){
    inProgressChatMessage.value = true
    currentChatMessageListener = db.collection(CHATS).document(chatId).collection(MESSAGE).addSnapshotListener{ value,error ->
        if(error!= null){
            handleException(error)
        }
        if(value != null){
            chatMessages.value= value.documents.mapNotNull {
                it.toObject<Message>()
            }.sortedBy { it.timeStamp }
            inProgressChatMessage.value = false

        }
    }
}
    fun depopulateMessage(){
        chatMessages.value = listOf()
        currentChatMessageListener = null
    }

    fun populateChat(){ //call this method where we get the userdata
        inProcessChats.value= true
        db.collection(CHATS).where(
            Filter.or(
                Filter.equalTo("user1.userId",userData.value?.userId),
                Filter.equalTo("user2.userId",userData.value?.userId),
            )
        ).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error)
            }
                if(value!= null){
                    chats.value = value.documents.mapNotNull {
                        it.toObject<ChatData>()
                    }
                    inProcessChats.value=false
                }
        }

    }


fun onSendReply(chatId:String, message:String){
    val time = Calendar.getInstance().time.toString()
val msg= Message(userData.value?.userId,message,time)
    db.collection(CHATS).document(chatId).collection(MESSAGE).document().set(msg)
}


    fun signUp(name:String, number:String, email:String, password:String){
        if(name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()){
            handleException(
                customMessage = " Please Fill All Fields"
            )
            return
        }
        inProcess.value = true

        db.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener {
            if(it.isEmpty){
                auth.createUserWithEmailAndPassword(email,password) . addOnCompleteListener {

                    if(it.isSuccessful){
                        signIn.value= true
                        createOrUpdateProfile(name=name, number = number)
                        Log.d("TAG","signUp: User Logged In")
                    }else{
                        handleException(it.exception,customMessage = "Sign Up failed")
                    }
                    inProcess.value =false
                }
            }else{

                handleException(customMessage = " number Already Exists")
                inProcess.value =false
            }
        }
            .addOnFailureListener { exception ->
                handleException(exception, "Failed to check existing number")
                inProcess.value = false
            }

    }

    fun logIn(email:String,password:String){
        if(email.isEmpty() or password.isEmpty()){
            handleException(customMessage = " Please Fill the all Fields")
            return
        }
        inProcess.value = true
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    signIn.value = true

                    auth.currentUser?.uid?.let{
                        getUserData(it)
                    }
                } else{
                    handleException(task.exception, "Login failed")
                }
                inProcess.value = false
            }
            .addOnFailureListener { exception->
                handleException(exception, "Login error")
                inProcess.value = false
            }

    }

    fun uploadProfileImage(uri: Uri, /*onSuccess:(Uri)->Unit*/){
        uploadImage(uri){
            createOrUpdateProfile(imageurl = it.toString())

        }
    }

    fun uploadImage(uri:Uri, onSuccess:(Uri)->Unit){
        inProcess.value= true
        val storageRef= storage.reference
        val uuid = UUID.randomUUID()
        val imageRef= storageRef.child("images/$uuid")
        val uploadTask = imageRef.putFile(uri)
        uploadTask.addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl

            result?.addOnSuccessListener(onSuccess)
                inProcess.value=false

        }
            .addOnFailureListener {
                handleException(it)
            }
    }

     fun createOrUpdateProfile(name:String? = null, number: String?= null,about: String?= null,imageurl:String?=null) {
         var uid = auth.currentUser?.uid
        val userData=UserData(
            userId = uid,
            name = name?: userData.value?.name ,//********
            number = number?: userData.value?.number,     //********
            about = about?: userData.value?.about,
            imageUrl =imageurl?: userData.value?.imageUrl
        )

        uid?.let{
            //profile update , we use database reference
            inProcess.value=true
         //   db.collection("users") instead we use  /-->if document was not it automatically creates
            db.collection(USER_NODE).document(uid).get().addOnSuccessListener {

                if(it.exists()){
                    //update the user
                    db.collection(USER_NODE).document(uid).set(userData)
                        .addOnSuccessListener {
                            inProcess.value = false
                            getUserData(uid) // Refresh user data after update
                        }
                        .addOnFailureListener { exception ->
                            handleException(exception, "Failed to update user profile")
                        }
                }
                else{

                    db.collection(USER_NODE).document(uid).set(userData)
                    inProcess.value= false
                    getUserData(uid)
                }
            }
                .addOnFailureListener{
                    //suppose agar koi panga ho jata h
                    handleException(it," Can't Retrieve User")

                }
        }
    }

     fun getUserData(uid:String) {
        inProcess.value=true
        db.collection(USER_NODE).document(uid).addSnapshotListener { value, error ->
            if(error!=null){
                handleException(error," Can't Retrieve User")
            }
            if (value != null ) {
                var user = value.toObject<UserData>()
                userData.value = user
                inProcess.value = false
                populateChat()
                populateStatuses()
            }
        }
    }

    fun handleException(exception: Exception?=null, customMessage : String=""){
        Log.e("Chat Sphere","live chat exception: ",exception)
        exception?.printStackTrace()
        val errorMsg= exception?.localizedMessage?:"An error occured"
        val message = if(customMessage.isNullOrEmpty()) errorMsg else customMessage

        eventMutableState.value = Event(message)
        inProcess.value=false
    }

    fun logOut() {
        auth.signOut()
        signIn.value = false
        userData.value = null
        depopulateMessage() // both work together and during logout all things related to it are deleted
        currentChatMessageListener = null
        eventMutableState.value = Event("Logged Out")
    }

    fun onAddChat(number: String) {//The function first checks if the entered phone number (number) is empty or contains non-digit characters.
        if(number.isEmpty() or !number.isDigitsOnly()){
            handleException(customMessage = "Number must be contain digits only")
        }else{//check whether number already exists or not. for that we use database ,so, we make  a path first in Constants.kt
            db.collection(CHATS).where(
                Filter.or(
                    /**
a query to check if a chat already exists between the two users:    First, it checks if there's an existing chat where user1's number is the entered number and user2's number is the current user's phone number.
If not, it checks if the reverse is true, where user1 is the current user and user2 is the entered number.Reason: This ensures that the function finds a chat regardless of who initiated it, covering both possibilities.*/
                    Filter.and(
                       Filter.equalTo("user1.number",number), //ENTRY NUMBER
                        Filter.equalTo("user2.number",userData.value?.number) // PERSONAL CONTACT NO.
                    ),
                    Filter.and(
                        Filter.equalTo("user1.number",userData.value?.number),
                        Filter.equalTo("user2.number",number)
                    )

            )).get().addOnSuccessListener {
                /**check the current data which i want to insert , IS present in database or not. vvvvi
                //clearly for that number chat exist or not */

                if(it.isEmpty){//This ensures that the function only creates a new chat if one doesn’t already exist
                    db.collection(USER_NODE).whereEqualTo("number",number).get().addOnSuccessListener {
                        //If the number is not found in the database, an error message "Number not found" is shown.
                        //Reason: This prevents the app from creating a chat with an invalid or non-existent number.
                        if (it.isEmpty){
                            handleException(customMessage = "Number not found")
                        }
                        else{
                            //now i am ready to create a chat room:--
                            val chatPartner = it.toObjects<UserData>()[0]  // clearlt it is not a single data it's a query so multiple data is coming here we don't use like collection(user_node).document, so , it is an array but why we take index 0
                            val chatid = db.collection(CHATS).document().id
                            val chat = ChatData(
                                chatId = chatid,
                                ChatUser(userData.value?.userId,  //personal caht user basiically
                                    userData.value?.name,
                                    userData.value?.imageUrl,
                                    userData.value?.number
                                    ),
                                ChatUser(chatPartner.userId,
                                    chatPartner.name,
                                    chatPartner.imageUrl,
                                    chatPartner.number)
                            )

                            db.collection(CHATS).document(chatid).set(chat)
                        }
                    }
                        .addOnFailureListener {
                            handleException(it)
                        }

                }
                else{
                    handleException(customMessage = "Chat already exists")
                }
            }
        }
    }

    fun uploadStatus(uri: Uri) {
        uploadImage(uri){
        createStatus(it.toString())
        }
    }
    fun createStatus(imageurl: String){
        val newstatus = status(
            ChatUser(
                userData.value?.userId,
                userData.value?.name,
                userData.value?.imageUrl,
                userData.value?.number
            ),
            imageurl,
            System.currentTimeMillis()
        )
        db.collection(STATUS).document().set(newstatus)
    }

    fun populateStatuses(){//call this method in getuserdata
        val timeDelta = 24L*60*60*1000
        val cutOff = System.currentTimeMillis() - timeDelta
        inProgressStatus.value=true
        db.collection(CHATS).where(
            Filter.or(
                Filter.equalTo("user1.userId",userData.value?.userId),
                Filter.equalTo("user2.userId",userData.value?.userId)
            )
        ).addSnapshotListener { value, error ->
            if (error!=null){
                handleException(error)
            }
            if (value!=null){
                val currentConnections = arrayListOf(userData.value?.userId)

                val chats = value.toObjects<ChatData>()
                chats.forEach {
                    chat->
                    if(chat.user1.userId == userData.value?.userId){
                        currentConnections.add(chat.user2.userId)
                    }
                    else
                        currentConnections.add(chat.user1.userId)
                }
                db.collection(STATUS).whereGreaterThan("timestamp",cutOff).whereIn("user.userId",currentConnections)
                    .addSnapshotListener{ value, error ->
                        if (error!=null){
                            handleException(error)
                        }
                        if (value!= null){
                            status.value = value.toObjects()
                            inProgressStatus.value = false
                        }
                    }
            }
        }
    }
}
