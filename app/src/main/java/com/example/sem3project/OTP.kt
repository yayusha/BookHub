package com.example.sem3project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sem3project.ui.theme.blue
import com.example.sem3project.ui.theme.Sem3ProjectTheme
import com.example.sem3project.ui.theme.White20

class OTP : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            otpbody()
        }
    }
}

@Composable
fun otpbody(){
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
                "Check your email",
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 30.dp),
                style = TextStyle(
                    textAlign = TextAlign.Left,
                    fontSize = 25.sp,
                    color = colorResource(R.color.Text),
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                "Check your email to get OTP code." +
                        "Enter your 5 digit code mentioned in the email below",

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 20.dp),
                style = TextStyle(
                    textAlign = TextAlign.Justify,
                    color = colorResource(R.color.Text)
                )
            )
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(5) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .border(
                                width = 1.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (it == 0) "" else "",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }



            Spacer(modifier = Modifier.height(40.dp))

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
                    "Reset Password",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = White20,
                    modifier = Modifier.clickable() {
                        val intent = Intent(
                            context,
                            New::class.java
                        )
                        context.startActivity(intent)
                        activity.finish()
                    }
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Haven't got the email? ",
                    color = Color.Gray.copy(0.7f)
                )

                // Clickable Text for "Sign Up"
                Text(
                    text = "Resend email",
                    color = blue,
                    fontWeight = FontWeight.Bold,

                    )
            }


        }
    }



}

