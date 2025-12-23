package com.example.sem3project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sem3project.ui.theme.Sem3ProjectTheme
import com.example.sem3project.ui.theme.White20
import com.example.sem3project.ui.theme.lightgrey

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                RegistrationBody()
        }
    }
}

@Composable
fun RegistrationBody() {

    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }


    var isPasswordFocused by remember { mutableStateOf(false) }
    var isUsernameFocused by remember { mutableStateOf(false) }

    val green = Color(0xFF4CAF50)

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .background(White20),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(color = Color.Black)) { append("Book") }
                    withStyle(SpanStyle(color = green)) { append("Hub") }
                },
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                "Sign Up",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(25.dp))


            // ---------------- FULL NAME ----------------
            Text("FULL NAME",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier  = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(7.dp))

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = { Text("John Doe", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = lightgrey,
                    focusedContainerColor = lightgrey,
                    focusedIndicatorColor = green,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))


            // ---------------- USERNAME ----------------
            Text("USERNAME",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier  = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(7.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("your_username", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = lightgrey,
                    focusedContainerColor = lightgrey,
                    focusedIndicatorColor = green,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isUsernameFocused = it.isFocused },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            if (isUsernameFocused) {
                Text(
                    "Use letters, numbers, underscores, and periods.\nMust start with a letter. Min 3 characters.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))


            // ---------------- EMAIL ----------------
            Text("EMAIL",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier  = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(7.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() },
                placeholder = { Text("example@gmail.com", color = Color.Gray) },
                isError = emailError,
                supportingText = {
                    if (emailError) {
                        Text("Please enter a valid email", color = Color.Red, fontSize = 12.sp)
                    }
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = lightgrey,
                    focusedContainerColor = lightgrey,
                    focusedIndicatorColor = green,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))


            // ---------------- PASSWORD ----------------
            Text("PASSWORD",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier  = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(7.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("••••••••") },
                visualTransformation =
                    if (passwordVisibility) VisualTransformation.None
                    else PasswordVisualTransformation(mask = '•'),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            painter = painterResource(
                                if (passwordVisibility) R.drawable.baseline_visibility_24
                                else R.drawable.baseline_visibility_off_24
                            ),
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = lightgrey,
                    focusedContainerColor = lightgrey,
                    focusedIndicatorColor = green,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isPasswordFocused = it.isFocused }
            )

            if (isPasswordFocused) {
                Text(
                    "A strong password should include:\n✔ 8+ characters\n✔ Uppercase + lowercase letters\n✔ Numbers & symbols",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))


            // ---------------- CONFIRM PASSWORD ----------------
            Text("CONFIRM PASSWORD",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier  = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start)
            Spacer(modifier = Modifier.height(7.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text("••••••••") },
                visualTransformation =
                    if (confirmPasswordVisibility) VisualTransformation.None
                    else PasswordVisualTransformation(mask = '•'),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            confirmPasswordVisibility = !confirmPasswordVisibility
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                if (confirmPasswordVisibility) R.drawable.baseline_visibility_24
                                else R.drawable.baseline_visibility_off_24
                            ),
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = lightgrey,
                    focusedContainerColor = lightgrey,
                    focusedIndicatorColor = green,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))


            // ---------------- SUBMIT BUTTON ----------------
            Button(
                onClick = { /* TODO: Registration Logic */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = green),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Register", color = Color.White, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}


