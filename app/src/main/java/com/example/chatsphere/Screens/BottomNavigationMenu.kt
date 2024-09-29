package com.example.chatsphere.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatsphere.DestinationScreen
import com.example.chatsphere.R
import com.example.chatsphere.navigateToScreen

enum class BottomNavigationItem(val icon:Int, val navDestination: DestinationScreen){
    CHATLIST(R.drawable.chat,DestinationScreen.ChatList),
    STATUSLIST(R.drawable.status,DestinationScreen.StatusList),
    PROFILE(R.drawable.user,DestinationScreen.Profile)
}



@Composable
fun BottomNavigationMenu(
    selectedItem: BottomNavigationItem,
    navController: NavController
) {
    Row(

        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
           // .padding(top = 10.dp)
            .padding(top = 25.dp, bottom= 10.dp, start = 10.dp, end = 10.dp)
            .background(Color.White)

    ){

        for(item in BottomNavigationItem.values()){
            Image(painter = painterResource(id = item.icon),contentDescription = null,
            modifier = Modifier.size(40.dp).padding(3.dp).weight(1f).clickable{
                navigateToScreen(navController,item.navDestination.route)
            }
//                .graphicsLayer {
//                    shape = RoundedCornerShape(
//                        topStart = 20.dp,
//                        topEnd = 20.dp
//                    )
//                    clip = true
//                translationY = if (item == selectedItem) -4.dp.toPx() else 0f //-4
//                scaleX = if (item == selectedItem) 1.2f else 1.2f //1.2
//                scaleY = if (item == selectedItem) 1.2f else 1.2f // we can use color filter for change in colour for selected item
//            }

            )
        }
    }
}