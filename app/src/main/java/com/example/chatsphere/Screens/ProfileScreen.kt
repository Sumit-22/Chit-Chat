package com.example.chatsphere.Screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatsphere.CommonDivider
import com.example.chatsphere.CommonImage
import com.example.chatsphere.CommonProgressBar
import com.example.chatsphere.LCViewModel

@Composable
fun ProfileScreen(navController: NavController, vm: LCViewModel) {
    val inProgress = vm.inProcess.value
    if (inProgress) {
        CommonProgressBar()
    } else {
        Column(modifier = Modifier.fillMaxSize()
            .wrapContentHeight()
            .padding(16.dp)
        ){
            ProfileContent(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp),
                vm = vm,
                name="hjsd",
                number="142",
                onNameChange = {""},
                onNumberChange = {""},
                onBack = {navController.popBackStack()},
                onSave = {},
                onLogOut = {}
                    )

            BottomNavigationMenu(
                selectedItem = BottomNavigationItem.PROFILE,
                navController = navController
            )
        }
    }
}
@Composable
fun ProfileContent(modifier:Modifier,
                   vm: LCViewModel,
                   name: String,
                   number: String,
                   onNameChange:(String)->Unit,
                   onNumberChange:(String)->Unit,
                   onBack:()->Unit,
                   onSave:()->Unit,
                   onLogOut:()->Unit)
{

    val imageUrl = vm.userData.value?.imageUrl

    Column(modifier = Modifier
        .wrapContentHeight()
        .padding(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(text = "Back", Modifier.clickable {
                onBack.invoke()
            })
            Text(text = "Save",
                Modifier.clickable {
                    onSave.invoke()
                })
}
            //we have to use divider so many times so, create divider in util
            CommonDivider()
            ProfileImage(imageUrl = imageUrl, vm = vm)

            CommonDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "name", modifier = Modifier.width(100.dp))
                TextField(value = name, onValueChange = onNameChange,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    )
                    )
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Number", modifier = Modifier.width(100.dp))
                TextField(value = number, onValueChange = onNumberChange,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    )
                    )
            }

            CommonDivider()
            Row(modifier.fillMaxWidth()
                .padding(16.dp),
                horizontalArrangement = Arrangement.Center
                ){
                Text(text = "LogOut",modifier = Modifier.clickable{ onLogOut.invoke()})
            }
        }
  }

@Composable
fun ProfileImage(imageUrl:String?,vm : LCViewModel){


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){
        uri ->
        uri?.let{
          //  vm.uploadProfileImage(uri)
        }
    }

    Box(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min))
    {
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                launcher.launch("images/*")
            },
            horizontalAlignment = Alignment.CenterHorizontally
         ){
            Card(shape = CircleShape,
                modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
                ){// just like for same purpose we make common image composable in utils.kt
                    CommonImage(data = imageUrl)
            }
            Text(text = "Change Profile Picture")

        }
        if(vm.inProcess.value){

        }
    }
}
