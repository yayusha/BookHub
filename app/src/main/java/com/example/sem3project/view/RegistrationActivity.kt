package com.example.sem3project.view

import android.app.Activity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sem3project.R

import com.example.sem3project.model.UserModel
import com.example.sem3project.ui.theme.White20
import com.example.sem3project.ui.theme.blue20
import com.example.sem3project.ui.theme.lightgrey
import com.example.sem3project.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

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

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val authViewModel = remember { AuthViewModel() }


    

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
    val activity = context as? Activity

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
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start

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

            // -------- FULL NAME --------
            Text("FULL NAME",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(7.dp))

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = { Text("John Doe") },
                colors = textFieldColors(green),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // -------- USERNAME --------
            Text("USERNAME",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(7.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("your_username") },
                colors = textFieldColors(green),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isUsernameFocused = it.isFocused }
            )

            if (isUsernameFocused) {
                Text(
                    "Use letters, numbers, underscores, and periods.\nMin 3 characters.",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // -------- EMAIL --------
            Text("EMAIL",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(7.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = !Patterns.EMAIL_ADDRESS.matcher(it).matches()
                },
                placeholder = { Text("example@gmail.com") },
                isError = emailError,
                supportingText = {
                    if (emailError) {
                        Text("Invalid email address", color = Color.Red, fontSize = 12.sp)
                    }
                },
                colors = textFieldColors(green),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // -------- PASSWORD --------
            Text("PASSWORD",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(7.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("••••••••") },
                visualTransformation =
                    if (passwordVisibility) VisualTransformation.None
                    else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            painter = painterResource(
                                if (passwordVisibility)
                                    R.drawable.baseline_visibility_24
                                else
                                    R.drawable.baseline_visibility_off_24
                            ),
                            contentDescription = null
                        )
                    }
                },
                colors = textFieldColors(green),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isPasswordFocused = it.isFocused }
            )

            if (isPasswordFocused) {
                Text(
                    "Min 8 characters required",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // -------- CONFIRM PASSWORD --------
            Text("CONFIRM PASSWORD",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(7.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text("••••••••") },
                visualTransformation =
                    if (confirmPasswordVisibility) VisualTransformation.None
                    else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        confirmPasswordVisibility = !confirmPasswordVisibility
                    }) {
                        Icon(
                            painter = painterResource(
                                if (confirmPasswordVisibility)
                                    R.drawable.baseline_visibility_24
                                else
                                    R.drawable.baseline_visibility_off_24
                            ),
                            contentDescription = null
                        )
                    }
                },
                colors = textFieldColors(green),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            // -------- REGISTER BUTTON --------
            Button(
                onClick = {

                    when {
                        fullName.isBlank() || username.isBlank() ||
                                email.isBlank() || password.isBlank() ||
                                confirmPassword.isBlank() -> {
                            Toast.makeText(
                                context,
                                "All fields are required",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        emailError -> {
                            Toast.makeText(context, "Invalid email", Toast.LENGTH_SHORT).show()
                        }

                        password.length < 8 -> {
                            Toast.makeText(
                                context,
                                "Password must be at least 8 characters",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        password != confirmPassword -> {
                            Toast.makeText(
                                context,
                                "Passwords do not match",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            authViewModel.register(email, password) { success, msg, userId ->
                                if (success) {

                                    val user = UserModel(
                                        userId = userId,
                                        email = email,
                                        firstName = fullName.split(" ").first(),
                                        lastName = fullName.split(" ").getOrNull(1) ?: "",
                                        phoneNumber = ""
                                    )

                                    authViewModel.addUserToDatabase(userId, user) { dbSuccess, dbMsg ->
                                        if (dbSuccess) {
                                            Toast.makeText(context, "Registration successful 🎉", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, dbMsg, Toast.LENGTH_LONG).show()
                                        }
                                    }

                                } else {
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                            }


                        }
                    }
                },            colors = ButtonDefaults.buttonColors(containerColor = green),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Register", color = Color.White, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(30.dp))
            Spacer(modifier = Modifier.height(24.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Already have an account? ")
                Text(
                    text = "Login",
                    color = blue20,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable() {
                        activity?.finish()
                    }
                )
            }
        }
    }
}

@Composable
fun textFieldColors(green: Color) = TextFieldDefaults.colors(
    unfocusedContainerColor = lightgrey,
    focusedContainerColor = lightgrey,
    focusedIndicatorColor = green,
    unfocusedIndicatorColor = Color.Transparent
)
