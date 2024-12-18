package com.example.chatsphere.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatsphere.CommonDivider
import com.example.chatsphere.CommonProgressBar
import com.example.chatsphere.CommonRow
import com.example.chatsphere.DestinationScreen
import com.example.chatsphere.LCViewModel
import com.example.chatsphere.TitleText
import com.example.chatsphere.navigateToScreen
import com.example.chatsphere.ui.theme.blue
import com.example.chatsphere.ui.theme.purple

@Composable
fun  ChatListScreen(navController: NavController,vm: LCViewModel) {
    //how to detect user for chatlist screen so, make variable like currentUser
    val inProgress = vm.inProcessChats
    if (inProgress.value) {
        CommonProgressBar()
    } else {
        val chats = vm.chats.value
        val userData = vm.userData.value
        val showDialog = remember {
            mutableStateOf(false)
        }
        val onFabClick: () -> Unit = { showDialog.value = true }
        val onDismiss: () -> Unit = { showDialog.value = false }
        val onAddChat: (String) -> Unit = { it ->
            vm.onAddChat(it)
            showDialog.value = false
        }
        Scaffold(
            floatingActionButton = {
                FAB(
                    showDialog = showDialog.value,
                    onFabClick = onFabClick,
                    onDismiss = onDismiss,
                    onAddChat = onAddChat
                )
            })
        { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)

            ) {
                TitleText(txt = "Chats")
                HorizontalDivider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier
                        .alpha(0.3f)
                        .padding(top = 0.5.dp, bottom =0.5.dp)
                )
                if(chats.isEmpty()){
                      Column(modifier = Modifier
                          .fillMaxWidth()
                          .weight(1f),
                          horizontalAlignment = Alignment.CenterHorizontally,
                          verticalArrangement = Arrangement.Center
    ) {
        Text(text = "No Chats Available")
    }
                }
                else{
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(chats){chat->
                            val chatUser = if (chat.user1.userId==userData?.userId){                        /**If user1 is the current user, then chat.user2 is assigned to chatUser. Else: If user1 is not the current user, this means user2 is the current user, and user1 is the chat partner. In this case, chat.user1 is assigned to chatUser */
                                chat.user2
                            }else{
                                chat.user1
                            }
                            CommonRow(imageUrl = chatUser.imageUrl, name = chatUser.name) {
                                chat.chatId?.let {chatid-> //to avoid the null value of chatid
                                    navigateToScreen(
                                        navController,
                                        DestinationScreen.SingleChat.createRoute(chatId = chatid))
                                }
                            }
                        }

                    }

                }
                BottomNavigationMenu(
                    selectedItem = BottomNavigationItem.CHATLIST,
                    navController = navController
                )
            }
        }
    }
}
    @Composable
    fun FAB(
        showDialog: Boolean,
        onFabClick: () -> Unit,
        onDismiss: () -> Unit,
        onAddChat: (String) -> Unit

    ) {
        val addChatNumber = remember {
            mutableStateOf("")
        }

        if (showDialog) {
            AlertDialog(onDismissRequest = {
                onDismiss.invoke()
                addChatNumber.value = ""
            },
                confirmButton = {
                    Button(onClick = { onAddChat(addChatNumber.value) } ,
                        colors= ButtonDefaults.buttonColors(
                           containerColor = purple,
                    )) {
                        Text(text = "Add chat" )
                    }
                }, title = {
                    Text(text = "Add Chat" , color = purple , fontWeight = FontWeight.Bold)
                }, text = {
                    OutlinedTextField(
                        value = addChatNumber.value,
                        onValueChange = { addChatNumber.value = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            )
        }
        FloatingActionButton(
            onClick = onFabClick,
            containerColor = purple,
            shape = CircleShape,
            modifier = Modifier.padding(bottom = 60.0.dp)
        ) {
            Icon(imageVector = Icons.Rounded.Add, contentDescription = null, tint = Color.White)
        }}
