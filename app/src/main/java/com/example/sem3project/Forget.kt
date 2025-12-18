package com.example.sem3project


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sem3project.ui.theme.blue
import com.example.sem3project.ui.theme.Sem3ProjectTheme
import com.example.sem3project.ui.theme.Purple80
import com.example.sem3project.ui.theme.White20
import com.example.sem3project.ui.theme.box

class Forget : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Password()
        }
    }
}

@Composable
fun Password() {
    var email by remember { mutableStateOf("") }

//    Link
    val context = LocalContext.current
    val activity = context as Activity

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
                "BookHub",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    color = colorResource(R.color.black),
                    fontWeight = FontWeight.Bold,

                    )
            )
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                "Forget Password",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    color = colorResource(R.color.black),
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                "Enter your email address to reset your password",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 20.dp),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = colorResource(R.color.Text)
                )
            )

            OutlinedTextField(
                value = email,
                onValueChange = { data ->
                    email = data
                },
                placeholder = {
                    Text("Enter your Email")
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = box,
                    focusedContainerColor = box,
                    focusedIndicatorColor = blue,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {val intent = Intent(context, UserDashboard::class.java)
                    intent.putExtra("email",email)
                    context.startActivity(intent)
                    activity.finish() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(horizontal = 15.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.Hub)),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text(
                    "Reset Password",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = White20,
                    modifier = Modifier.clickable() {
                        val intent = Intent(
                            context,
                            OTP::class.java
                        )
                        context.startActivity(intent)
                        activity.finish()
                    }
                )
            }


        }
    }
}

