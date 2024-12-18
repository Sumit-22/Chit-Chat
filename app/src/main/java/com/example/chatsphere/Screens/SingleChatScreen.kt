@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatsphere.Screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatsphere.CommonDivider
import com.example.chatsphere.CommonImage
import com.example.chatsphere.CustomTextFieldShape
import com.example.chatsphere.LCViewModel
import com.example.chatsphere.R
import com.example.chatsphere.data.Message
import com.example.chatsphere.ui.theme.blue
import com.example.chatsphere.ui.theme.purple
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun  SingleChatScreen(navController: NavController, vm: LCViewModel, ChatId: String) {

    var reply by rememberSaveable {
        mutableStateOf("")
    }

    val onSendReply = {
        vm.onSendReply(ChatId, reply)
        reply = ""
    }
    var chatMessage = vm.chatMessages
    val myUser = vm.userData.value
    var currentChat = vm.chats.value.first { it.chatId == ChatId }
    val chatUser =
        if (myUser?.userId == currentChat.user1.userId) currentChat.user2 else currentChat.user1

    LaunchedEffect(key1 = Unit) {
        vm.populateMessages(ChatId)
    }
    BackHandler {
        vm.depopulateMessage()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {

        ChatHeader(name = chatUser.name ?: "", imageUrl = chatUser.imageUrl ?: "") {
            navController.popBackStack()
            vm.depopulateMessage()
        }
//        HorizontalDivider(
//            color = Color.LightGray,
//            thickness = 1.dp,
//            modifier = Modifier
//                .alpha(0.3f)
//                .padding(top = 3.dp, bottom = 3.dp)
//        )
            MessageBox(
                modifier = Modifier.imePadding().weight(1f),// Add imePadding here
                chatMessages = chatMessage.value,
                currentUserId = myUser?.userId ?: ""
            )
            //Text(text = vm.chatMessages.value.toString())
            ReplyBox(
                reply = reply,
                onReplyChange = { reply = it },
                onSendReply = onSendReply,
                modifier = Modifier.imePadding()
            )// Add imePadding here)
        }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyBox(modifier: Modifier = Modifier,reply  :String , onReplyChange:(String)->Unit , onSendReply:()->Unit){
    //Column(modifier = Modifier.fillMaxWidth()) {
     //   CommonDivider()
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(0.5.dp, top = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            TextField(
                value = reply,
                onValueChange = onReplyChange,
                maxLines = 2,
                modifier = Modifier
                    .width(300.dp)
          //          .weight(1f)
                    .padding(start = 10.dp, end = 4.dp)
                    .border(
                        1.dp, brush = Brush.horizontalGradient(
                            colors = listOf(
                                purple,
                                blue,
                                blue,
                                purple
                            ) // Fading effect from transparent to purple
                        ), shape = RoundedCornerShape(40.dp)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = blue,
                    containerColor = Color.White
                )

            )
            IconButton(onClick = { onSendReply() },
                modifier = Modifier.size(48.dp)) {
                Image( // difference between icon and image in compose
                    painter = painterResource(id = R.drawable.send),
                    contentDescription = "Send Reply",
                    modifier = Modifier.size(40.dp)
                )
        //    }

        }
    }
}

    @Composable
    fun ChatHeader(name: String, imageUrl: String, onBackClicked: () -> Unit) {
        Row(
            modifier = Modifier
                .padding(8.dp, top = 20.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Rounded.ArrowBack, contentDescription = null, modifier = Modifier
                .clickable {
                    onBackClicked.invoke()
                }
                .padding(8.dp))
            CommonImage(
                data = imageUrl, modifier = Modifier
                    .padding(8.dp)
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Text(
                text = name,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }

    @Composable
    fun MessageBox(modifier: Modifier, chatMessages: List<Message>, currentUserId: String) {

        LazyColumn(
                modifier = Modifier
                    .padding(bottom = 8.dp)
            ) {
                items(chatMessages) { msg ->
                    val alignment =
                        if (msg.sendBy == currentUserId) Alignment.End else Alignment.Start
                    val color = if (msg.sendBy == currentUserId) Color(0xFF7EAFD6) else Color(
                        0xFF8481E0
                    )
                    Column(
                        modifier = Modifier//0xFF3B9DF8,
                            .fillMaxWidth()
                            .padding(8.dp), horizontalAlignment = alignment
                    ) {
//                    Canvas(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(4.dp)
//                            .clip(RoundedCornerShape(16.dp)) // Optional rounded corners for the container
//                    ) {
//                        drawSpeechBubble(msg.message ?: "", color)
//                    }
                        Text(
                            text = msg.message ?: "",
                            modifier
                                .clip(RoundedCornerShape(40.dp))//CustomTextFieldShape())
                                .background(color)
                                .padding(
                                    top = 8.dp,
                                    start = 8.dp,
                                    bottom = 8.dp,
                                    end = 10.dp
                                ),

                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Normal
                        )
                    }

                }
            }
    }
/*
@Composable
fun SingleChatScreen(navController: NavController, vm: LCViewModel, ChatId: String) {
        var reply by rememberSaveable {
                mutableStateOf("")
        }

        val onSendReply = {
                vm.onSendReply(ChatId,reply)
                reply=""
        }
        var chatMessage= vm.chatMessages
        val myUser=vm.userData.value
        var currentChat = vm.chats.value.first{ it.chatId ==ChatId}
        val chatUser = if(myUser?.userId == currentChat.user1.userId) currentChat.user2 else currentChat.user1

        LaunchedEffect(key1 = Unit) {
                vm.populateMessages(ChatId)
        }
        BackHandler {
                vm.depopulateMessage()
        }
    Column(modifier = Modifier.fillMaxSize().padding(top = 10.dp)) {
        val chatUser =
        ChatHeader(name = chatUser.name ?: "", imageUrl = chatUser.imageUrl ?: "") {
            navController.popBackStack()
            vm.depopulateMessage()
        }

        HorizontalDivider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.alpha(0.3f).padding(top = 3.dp, bottom = 3.dp)
        )

        MessageBox(modifier = Modifier.weight(1f), chatMessages = chatMessage.value, currentUserId = myUser?.userId ?: "")

        ReplyBox(
            reply = reply,
            onReplyChange = { reply = it },
            onSendReply = onSendReply,
            modifier = Modifier.imePadding() // Adjust for keyboard visibility

        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyBox(
    modifier: Modifier = Modifier,
    reply: String,
    onReplyChange: (String) -> Unit,
    onSendReply: () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = reply,
                onValueChange = onReplyChange,
                maxLines = 3,
                modifier = Modifier.weight(1f).padding(start = 10.dp, end = 10.dp),
                shape = RoundedCornerShape(40.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Blue,
                    containerColor = Color.Transparent // Transparent background color for container
                )
            )
            IconButton(onClick = { onSendReply() }, modifier = Modifier.size(48.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.send),
                    contentDescription = "Send Reply",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Composable
fun ChatHeader(name: String, imageUrl: String, onBackClicked: () -> Unit) {
    Row(modifier = Modifier.padding(8.dp, top = 20.dp).fillMaxWidth().wrapContentHeight(), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Rounded.ArrowBack, contentDescription = null, modifier = Modifier.clickable { onBackClicked.invoke() }.padding(8.dp))
        CommonImage(data = imageUrl, modifier = Modifier.padding(8.dp).size(50.dp).clip(CircleShape))
        Text(text = name, fontWeight = FontWeight.Medium, modifier = Modifier.padding(start=8.dp))
    }
}

@Composable
fun MessageBox(modifier: Modifier, chatMessages: List<Message>, currentUserId: String) {
    LazyColumn(modifier) {
        items(chatMessages) { msg ->
            val alignment = if (msg.sendBy == currentUserId) Alignment.End else Alignment.Start

            Column(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalAlignment = alignment
            ) {
                Canvas(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                    drawSpeechBubble(msg.message ?: "", if (msg.sendBy == currentUserId) Color(0xFF3B9DF8) else Color(0xFF736FF5))
                }
            }
        }
    }
}

fun DrawScope.drawSpeechBubble(message: String, bubbleColor: Color) {
    val bubbleWidth = size.width - 16.dp.toPx() // Adjusting for padding
    val bubbleHeightBaseLineOffsetY= 20f // Offset to accommodate tail height

    // Draw the main rounded rectangle for the speech bubble body.
    drawRoundRect(
        color = bubbleColor,
        size= Size(bubbleWidth, size.height - bubbleHeightBaseLineOffsetY),
        cornerRadius= CornerRadius(16.dp.toPx())
    )

    // Draw the tail of the speech bubble.
    val path= Path().apply {
        moveTo(bubbleWidth / 2 - 10f, size.height - bubbleHeightBaseLineOffsetY)
        lineTo(bubbleWidth / 2, size.height)
        lineTo(bubbleWidth / 2 + 10f, size.height - bubbleHeightBaseLineOffsetY)
    }

    drawPath(path= path, color= bubbleColor)

    // Draw the text inside the speech bubble.
    drawContext.canvas.nativeCanvas.apply {
        drawText(
            message,
            24f, // X position for text padding from left.
            (size.height - bubbleHeightBaseLineOffsetY) / 2 + 8.dp.toPx(), // Y position for vertical centering.
            android.graphics.Paint().apply {
                color= android.graphics.Color.WHITE // Text color.
                textSize= 16.sp.toPx() // Text size.
                isAntiAlias= true // Smooth text rendering.
            }
        )
    }
}
*/