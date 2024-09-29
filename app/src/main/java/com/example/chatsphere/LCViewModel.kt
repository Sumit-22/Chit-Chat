package com.example.chatsphere

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.chatsphere.data.Event
import com.example.chatsphere.data.USER_NODE
import com.example.chatsphere.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LCViewModel @Inject constructor(
    val auth : FirebaseAuth,
    var db: FirebaseFirestore,
    val storage : FirebaseStorage

): ViewModel() {
    var inProcess = mutableStateOf<Boolean>(false)
    init{
// before declaring chatlist screen we left this block empty as app requires this , but now we have to make currentUser for chatlist screen
        val currentUser =auth.currentUser
        currentUser?.uid?.let{
            getUserData(it)
        }
    }

    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)


// why we use this  variable thinnk about this right :-- it's for line 45 , think about constructor of userData
    val userData = mutableStateOf<UserData?>(null)



    fun signUp(name:String, number:String, email:String, password:String){
        inProcess .value = true
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
                }
            }else{

                handleException(customMessage = " number Already Exists")
                inProcess.value =false
            }
        }

    }

    fun logIn(email:String,password:String){
        if(email.isEmpty() or password.isEmpty()){
            handleException(customMessage = " Please Fill the all Fields")
            return
        }else{
            inProcess.value = true
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        signIn.value = true
                        inProcess.value = false
                        auth.currentUser?.uid?.let{
                            getUserData(it)
                        }
                    }
                }
        }
    }

    fun uploadProfileImage(uri: Uri, onSuccess:(Uri)->Unit){
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

     fun createOrUpdateProfile(name:String? = null, number: String?= null,imageurl:String?=null) {
         var uid = auth.currentUser?.uid
        val userData=UserData(
            userId = uid,
            name = name?: userData.value?.name ,//********
            number = number?: userData.value?.number,     //********
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
                inProcess.value=false
                return@addSnapshotListener
            }
            if (value != null && value.exists()) {
                var user = value.toObject<UserData>()
                if (user != null) {
                    userData.value = user
                }
                else{userData.value = null
                    Log.e("LCViewModel", "User data is null after conversion.")
                }
            } else {userData.value = null
                Log.e("LCViewModel", "User document does not exist or is null.")
            }
                inProcess.value = false
        }
    }

    fun handleException(exception: Exception?=null, customMessage : String=""){
        Log.e("Chat Sphere","live chat exception: ",exception)
        exception?.printStackTrace()
        val errorMsg= exception?.localizedMessage?:""
        val message = if(customMessage.isNullOrEmpty()) errorMsg else customMessage

        eventMutableState.value = Event(message)
        inProcess.value=false
    }
}
