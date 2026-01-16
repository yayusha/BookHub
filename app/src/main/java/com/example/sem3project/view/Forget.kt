package com.example.sem3project.view


import android.app.Activity
import android.content.Intent
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sem3project.ui.theme.blue
import com.example.sem3project.ui.theme.White20
import com.example.sem3project.ui.theme.box


import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import com.example.sem3project.R
import com.example.sem3project.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


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
fun Password(viewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

//    Link
    val context = LocalContext.current
    val activity = context as Activity
//    val auth = FirebaseAuth.getInstance()


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
                onValueChange = { email = it},

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

// RESET BUTTON
            Button(
                onClick = {
                    if (email.isNotEmpty()) {
                        isLoading = true
                        viewModel.forgotPassword(email.trim()) { success, message ->
                            isLoading = false
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            if (success) {
                                val intent = Intent(context, LoginActivity::class.java)
                                context.startActivity(intent)
                                activity.finish() // This was inside the Intent parentheses by mistake
                            }
                        }
                    } else {
                        Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
                    }
                },

                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(horizontal = 15.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.Hub)),
                shape = RoundedCornerShape(30.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        "Reset Password",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = White20,
                    )
                }
            }

        }
    }
}

