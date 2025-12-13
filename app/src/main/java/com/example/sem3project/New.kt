package com.example.sem3project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sem3project.ui.theme.blue
import com.example.sem3project.ui.theme.Sem3ProjectTheme
import com.example.sem3project.ui.theme.Purple80
import com.example.sem3project.ui.theme.White20

class New : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Newpass()
        }
    }
}

@Composable

fun Newpass(){
    var password by remember { mutableStateOf("") }
    var Cpassword by remember { mutableStateOf("") }

    var visibility by remember { mutableStateOf(false) }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(White20)
        )
        {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                "Set new password",
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 30.dp),
                style = TextStyle(
                    textAlign = TextAlign.Left,
                    fontSize = 25.sp,
                    color = colorResource(R.color.Text),
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Create a new password. Ensure it differs from previous ones for security",
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 21.dp),
                style = TextStyle(
                    textAlign = TextAlign.Justify,
                    fontSize = 15.sp,
                    color = colorResource(R.color.Text),
//                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                "Password",
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 21.dp),
                style = TextStyle(
                    textAlign = TextAlign.Justify,
                    fontSize = 15.sp,
                    color = colorResource(R.color.Text),
                    fontWeight = FontWeight.Bold
                )
            )

            OutlinedTextField(
                value = password,
                onValueChange = { data ->
                    password = data
                },
                placeholder = {
                    Text("*******")
                },

                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(R.color.pass),
                    focusedContainerColor = colorResource(R.color.white),
                    focusedIndicatorColor = blue,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton (onClick = {
                        visibility = !visibility
                    }) {
                        Icon(
                            painter = if (visibility)
                                painterResource(R.drawable.baseline_visibility_off_24)
                            else painterResource(
                                R.drawable.baseline_visibility_24
                            ),


                            contentDescription = null
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(35.dp))
            Text(
                "Confirm Password",
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 21.dp),
                style = TextStyle(
                    textAlign = TextAlign.Justify,
                    fontSize = 15.sp,
                    color = colorResource(R.color.Text),
                    fontWeight = FontWeight.Bold
                )
            )

            OutlinedTextField(
                value = Cpassword,
                onValueChange = { data ->
                    Cpassword = data
                },
                placeholder = {
                    Text("*******")
                },

                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(R.color.pass),
                    focusedContainerColor = colorResource(R.color.white),
                    focusedIndicatorColor = blue,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton (onClick = {
                        visibility = !visibility
                    }) {
                        Icon(
                            painter = if (visibility)
                                painterResource(R.drawable.baseline_visibility_off_24)
                            else painterResource(
                                R.drawable.baseline_visibility_24
                            ),


                            contentDescription = null
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(39.dp))

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(horizontal = 15.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.Hub)),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text(
                    "Update Password",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = White20
                )
            }



        }
    }
}

