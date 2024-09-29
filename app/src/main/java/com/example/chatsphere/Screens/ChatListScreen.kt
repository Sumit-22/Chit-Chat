package com.example.chatsphere.Screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.chatsphere.LCViewModel

@Composable
fun  ChatListScreen(navController: NavController,vm: LCViewModel) {
    //how to detect user for chatlist screen so, make variable like currentUser
    Text(text="ChatList Screen")
    BottomNavigationMenu(selectedItem = BottomNavigationItem.CHATLIST, navController = navController )
}