package com.example.sem3project

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sem3project.View.Forget

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                LoginBody()

        }
    }
}

@Composable
fun LoginBody() {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var visibility by remember { mutableStateOf(false) }

    var isEmailFocused by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }
    val BookHubGreen = Color(0xFF4CAF50)
    val InputBackground = Color(0xFFF0F0F0)




    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(35.dp))

        // -------- APP TITLE (BookHub) --------
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Book",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
            Text(
                text = "Hub",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = BookHubGreen
                ),
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // -------- LOGIN TITLE --------
        Text(
            "Login",
            style = TextStyle(
                fontSize = 22.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(modifier = Modifier.height(40.dp))

        // -------- USERNAME/EMAIL LABEL --------
        Text(
            "USERNAME/EMAIL",
            style = TextStyle(
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // -------- EMAIL FIELD --------
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("your_username or email", color = Color.LightGray) },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isEmailFocused = it.isFocused },
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBackground,
                unfocusedContainerColor = InputBackground,
                focusedIndicatorColor = BookHubGreen,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = BookHubGreen
            ),
            textStyle = TextStyle(color = Color.Black),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        if (isEmailFocused) {
            Text(
                "Use letters, numbers, underscores, and periods.\nMust start with a letter. Min 3 characters.",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // -------- PASSWORD LABEL --------
        Text(
            "PASSWORD",
            style = TextStyle(
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // -------- PASSWORD FIELD --------
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("*********", color = Color.LightGray) },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isPasswordFocused = it.isFocused },
            shape = RoundedCornerShape(10.dp),
            visualTransformation =
                if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { visibility = !visibility }) {
                    Icon(
                        painter = painterResource(
                            if (visibility) R.drawable.baseline_visibility_24
                            else R.drawable.baseline_visibility_off_24
                        ),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBackground,
                unfocusedContainerColor = InputBackground,
                focusedIndicatorColor = BookHubGreen,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = BookHubGreen
            ),
            textStyle = TextStyle(color = Color.Black),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        if (isPasswordFocused) {
            Text(
                "A strong password should include:\n✔ 8+ characters\n✔ Uppercase & lowercase letters\n✔ Numbers & symbols",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(45.dp))

        // -------- FORGOT PASSWORD LINK --------
        Text(
            text = "Forgot Password?",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .clickable {
                    val intent = Intent(context, Forget::class.java)
                    context.startActivity(intent)
                },
            style = TextStyle(
                textAlign = TextAlign.End,
                color = BookHubGreen,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        )

        Spacer(modifier = Modifier.height(30.dp))

        // -------- SUBMIT BUTTON --------
        Button(
            onClick = {
                // TODO - Login logic
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BookHubGreen
            )
        ) {
            Text(
                "Submit",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}


