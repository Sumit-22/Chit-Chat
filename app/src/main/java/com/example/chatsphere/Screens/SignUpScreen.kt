package com.example.chatsphere.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatsphere.CheckSignedIn
import com.example.chatsphere.CommonProgressBar
import com.example.chatsphere.DestinationScreen
import com.example.chatsphere.LCViewModel
import com.example.chatsphere.R
import com.example.chatsphere.navigateToScreen
import com.example.chatsphere.ui.theme.purple
import com.example.chatsphere.ui.theme.white

@Composable
fun SignUpScreen( navController: NavController, vm: LCViewModel) {

    CheckSignedIn(vm = vm,navController = navController)

    Box (modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .verticalScroll(
                rememberScrollState()
            )
        , horizontalAlignment = Alignment.CenterHorizontally

            ) {
            val nameState = remember{
                mutableStateOf(TextFieldValue())
            }
            val numberState = remember{
                mutableStateOf(TextFieldValue())
            }
            val emailState = remember{
                mutableStateOf(TextFieldValue())
            }
            val passwordState = remember{
                mutableStateOf(TextFieldValue())
            }
            val focus = LocalFocusManager.current
            Image(painter =
            painterResource(id = R.drawable.ic_sendlogo),
                contentDescription = "logo",
                modifier = Modifier
                    .width(200.dp)
                    .padding(top = 16.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
            Text(text = " Sign Up",
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
            OutlinedTextField(
                value = nameState.value,
                onValueChange ={
                    nameState.value=it
            },
                label = {Text(text = "Name", color = purple)},
                modifier = Modifier.padding(8.dp),

            )
            OutlinedTextField(
                value = numberState.value,
                onValueChange ={
                    numberState.value=it
            },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                label = {Text(text = "Contact", color = purple)} ,
                modifier = Modifier.padding(8.dp)
            )
            OutlinedTextField(
                value = emailState.value,
                onValueChange ={
                    emailState.value=it
                },
                label = {Text(text = "Email", color = purple)} ,
                modifier = Modifier.padding(8.dp)
            )
            OutlinedTextField(
                value = passwordState.value,
                onValueChange ={
                    passwordState.value=it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                label = {Text(text = "Password", color = purple)} ,
                modifier = Modifier.padding(8.dp)
            )
            Button(onClick={vm.signUp(
                name= nameState.value.text,
                number = numberState.value.text,
                email =emailState.value.text,
                password= passwordState.value.text

            )

            },colors = ButtonDefaults.buttonColors(
                containerColor = purple, // Background color
                contentColor = white // Text/Icon color
            ),
                modifier = Modifier.padding(8.dp)
            )
            {
                Text(text = " SIGN UP ")
            }
            Text(text = "Already a User ? Go to login - >" ,
                color = purple,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navigateToScreen(navController, DestinationScreen.Login.route)
                    }

            )

        }

    }
    if (vm.inProcess.value){
        //show progress we have to show at multiple places so, webuild  a function in util.kt
    CommonProgressBar()
    }
}