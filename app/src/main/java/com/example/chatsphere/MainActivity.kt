package com.example.chatsphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatsphere.Screens.ChatListScreen
import com.example.chatsphere.Screens.LoginScreen
import com.example.chatsphere.Screens.ProfileScreen
import com.example.chatsphere.Screens.SignUpScreen
import com.example.chatsphere.Screens.StatusScreen
import com.example.chatsphere.ui.theme.ChatSphereTheme
import dagger.hilt.android.AndroidEntryPoint


sealed class DestinationScreen(var route:String){
        object SignUp :DestinationScreen("signup")
        object Login :DestinationScreen("login")
        object Profile :DestinationScreen("profile")
        object ChatList :DestinationScreen("chatList")
        object SingleChat :DestinationScreen("singleChat/{chatId}"){
            fun createRoute(chatId:String) = "singl eChat/$chatId"
        }
        object StatusList :DestinationScreen("statusList")
        object SingleStatus :DestinationScreen("singleStatus/{userId}"){
            fun createRoute(userId:String) = "singleStatus/$userId"
        }

    }



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatSphereTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var vm = hiltViewModel<LCViewModel>()
    NavHost(navController = navController, startDestination=DestinationScreen.SignUp.route){

        composable(DestinationScreen.SignUp.route){
            SignUpScreen(navController =navController,vm= vm)
        }
        composable(DestinationScreen.Login.route){
            LoginScreen(navController =navController,vm= vm)
        }
        composable(DestinationScreen.ChatList.route){
            ChatListScreen(navController =navController,vm= vm)
        }
        composable(DestinationScreen.StatusList.route){
            StatusScreen(navController =navController,vm= vm)
        }
        composable(DestinationScreen.Profile.route){
            ProfileScreen(navController =navController,vm= vm)
        }
    }

}
