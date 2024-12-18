package com.example.chatsphere.Screens


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatsphere.CommonDivider
import com.example.chatsphere.CommonImage
import com.example.chatsphere.CommonProgressBar
import com.example.chatsphere.DestinationScreen
import com.example.chatsphere.LCViewModel
import com.example.chatsphere.navigateToScreen
import com.example.chatsphere.ui.theme.blue
import com.example.chatsphere.ui.theme.purple

//
//@Composable
//fun ProfileScreen(navController: NavController, vm: LCViewModel) {
//    val inProgress = vm.inProcess.value
//    if (inProgress) {
//        CommonProgressBar()
//    } else {
//        Column(modifier = Modifier.fillMaxSize()
//            .wrapContentHeight()
//            .padding(16.dp)
//        ){
//            ProfileContent(
//                modifier = Modifier
//                    .weight(1f)
//                    .verticalScroll(rememberScrollState())
//                    .padding(8.dp),
//                vm = vm,
//                name="hjsd",
//                number="142",
//                onNameChange = {""},
//                onNumberChange = {""},
//                onBack = {navController.popBackStack()},
//                onSave = {},
//                onLogOut = {}
//                    )
//
//            BottomNavigationMenu(
//                selectedItem = BottomNavigationItem.PROFILE,
//                navController = navController
//            )
//        }
//    }
//}
//@Composable
//fun ProfileContent(modifier:Modifier,
//                   vm: LCViewModel,
//                   name: String,
//                   number: String,
//                   onNameChange:(String)->Unit,
//                   onNumberChange:(String)->Unit,
//                   onBack:()->Unit,
//                   onSave:()->Unit,
//                   onLogOut:()->Unit)
//{
//
//    val imageUrl = vm.userData.value?.imageUrl
//
//    Column(modifier = Modifier
//        .wrapContentHeight()
//        .padding(16.dp)) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp),
//            horizontalArrangement = Arrangement.SpaceBetween
//
//        ) {
//            Text(text = "Back", Modifier.clickable {
//                onBack.invoke()
//            })
//            Text(text = "Save",
//                Modifier.clickable {
//                    onSave.invoke()
//                })
//}
//            //we have to use divider so many times so, create divider in util
//            CommonDivider()
//            ProfileImage(imageUrl = imageUrl, vm = vm)
//
//            CommonDivider()
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(4.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(text = "name", modifier = Modifier.width(100.dp))
//                TextField(value = name, onValueChange = onNameChange,
//                    colors = TextFieldDefaults.colors(
//                        focusedTextColor = Color.Black,
//                        focusedContainerColor = Color.Transparent,
//                        unfocusedContainerColor = Color.Transparent,
//                        disabledContainerColor = Color.Transparent
//                    )
//                    )
//            }
//
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(4.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(text = "Number", modifier = Modifier.width(100.dp))
//                TextField(value = number, onValueChange = onNumberChange,
//                    colors = TextFieldDefaults.colors(
//                        focusedTextColor = Color.Black,
//                        focusedContainerColor = Color.Transparent,
//                        unfocusedContainerColor = Color.Transparent,
//                        disabledContainerColor = Color.Transparent
//                    )
//                    )
//            }
//
//            CommonDivider()
//            Row(modifier.fillMaxWidth()
//                .padding(16.dp),
//                horizontalArrangement = Arrangement.Center
//                ){
//                Text(text = "LogOut",modifier = Modifier.clickable{ onLogOut.invoke()})
//            }
//        }
//  }
//
//@Composable
//fun ProfileImage(imageUrl:String?,vm : LCViewModel){
//
//
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ){
//        uri ->
//        uri?.let{
//          //  vm.uploadProfileImage(uri)
//        }
//    }
//
//    Box(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min))
//    {
//        Column(modifier = Modifier
//            .padding(8.dp)
//            .fillMaxWidth()
//            .clickable {
//                launcher.launch("images/*")
//            },
//            horizontalAlignment = Alignment.CenterHorizontally
//         ){
//            Card(shape = CircleShape,
//                modifier = Modifier
//                    .padding(8.dp)
//                    .size(100.dp)
//                ){// just like for same purpose we make common image composable in utils.kt
//                    CommonImage(data = imageUrl)
//            }
//            Text(text = "Change Profile Picture")
//
//        }
//        if(vm.inProcess.value){
//
//        }
//    }
//}

@Composable
fun ProfileScreen(navController: NavController, vm: LCViewModel) {
    val inProgress = vm.inProcess.value
    if (inProgress) {
        CommonProgressBar()
    } else {
        val userData = vm.userData.value
        var name by rememberSaveable {
            mutableStateOf(userData?.name ?:"")
        }

        var number by rememberSaveable {
            mutableStateOf(userData?.number ?:"")
        }
        var about by rememberSaveable {
            mutableStateOf(userData?.about ?:"")
        }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                ProfileContent(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp),
                    vm = vm,
//                name = "Your Name",
//                number = "123-456-7890", after using mutable state in name and number
                    name = name,
                    number = number,
                    about = about,
                    onNameChange = { name = it },
                    onNumberChange = { number = it },
                    onAboutChange = { about = it },
                    onBack = { //navController.popBackStack()
                        navigateToScreen(
                            navController = navController,
                            route = DestinationScreen.ChatList.route
                        )
                    },
                    onSave = {
                        vm.createOrUpdateProfile(name = name, number = number, about = about)
                    },
                    navController
                )

                Spacer(modifier = Modifier.height(8.dp))

                BottomNavigationMenu(
                    selectedItem = BottomNavigationItem.PROFILE,
                    navController = navController
                )
            }


    }
}

@Composable
fun LogoutWithConfirmation(vm: LCViewModel,navController: NavController):Boolean {
    // State to control the visibility of the AlertDialog
    var showDialog by remember { mutableStateOf(true) }

    // AlertDialog to confirm logout
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Logout" , color=purple , fontWeight = FontWeight.Medium) },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(onClick = {
                    vm.logOut()
                    navigateToScreen(navController = navController, route = DestinationScreen.Login.route)
                    showDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }
    return showDialog
}
@Composable
fun ProfileContent(
    modifier: Modifier,
    vm: LCViewModel,
    name: String,
    number: String,
    about: String,
    onNameChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
    onAboutChange : (String) -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit,
    navController: NavController
) {
    val imageUrl = vm.userData.value?.imageUrl
    var showDialog by remember { mutableStateOf(false) }
    Column(modifier = modifier
// *****       .wrapContentHeight()
        .padding(top = 6.dp, bottom = 6.dp)

    ) {
        // Top Bar with stylish font and gradient background
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top=12.dp,start=6.dp,end=6.dp,bottom=6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
         //****   verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Back",
                modifier = Modifier.clickable { onBack.invoke() }
                    .padding(top = 7.dp,bottom=3.dp,start=6.dp,end=6.dp)
                ,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = purple
            )
            Text(
                text = "Save",
                modifier = Modifier
                    .clickable { onSave.invoke() }
                    .padding(top = 7.dp,bottom=3.dp,start=6.dp,end=6.dp),
                    fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = purple
            )
        }

        CommonDivider()

        ProfileImage(imageUrl = imageUrl, vm = vm)

        CommonDivider()

       TextHolder(text = "Name",textValue = name, onChange = onNameChange)

        TextHolder(text = "Number",textValue = number, onChange = onNumberChange)

        TextHolder(text = "About",textValue = about, onChange = onAboutChange)

        CommonDivider()
        // Logout button with gradient background
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
                Button(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(
                            brush = Brush.linearGradient(
                                listOf(blue, purple)
                            )
                        ),
                ) {/*brush = Brush.radialGradient(
                                listOf(blue,purple)
            )*/
                    Text(text = "Log Out", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        if (showDialog) {
            showDialog=LogoutWithConfirmation(vm,navController)

        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextHolder(text : String,textValue  :String, onChange : (String) -> Unit){
    OutlinedTextField(
        value = textValue,
        onValueChange ={
           onChange(it)
        },
        label = {Text(text = "$text : ",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = blue

            )} ,
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        shape = RoundedCornerShape(15.dp),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = purple,
            unfocusedIndicatorColor = purple,
            cursorColor =blue,
            containerColor = Color.Transparent
        )
    )
}
@Composable
fun ProfileImage(imageUrl: String?, vm: LCViewModel) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
             vm.uploadProfileImage(uri)
        }
    }

    Box(modifier = Modifier.height(IntrinsicSize.Min)
        .fillMaxWidth()
        .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(3.dp)
                .fillMaxWidth()
                .clickable {
                    launcher.launch("image/*")
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .padding(8.dp)
                    .size(120.dp),
                border = BorderStroke(2.dp, purple)
            ) {
                CommonImage(data = imageUrl)
            }
            Text(
                text = "Change Profile Picture",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = purple,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        if (vm.inProcess.value) {
            // Optional: Loading indicator during image upload
            CommonProgressBar()
        }
    }
}
