package com.pam.Dictionary

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pam.Dictionary.R
import com.pam.Dictionary.ui.theme.DictionaryAppTheme
import kotlinx.coroutines.delay

class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DictionaryAppTheme {
                SplashScreen { navigateToMainActivity() }
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close SplashScreenActivity
    }
}

@Composable
fun SplashScreen(onFinish: () -> Unit) {
    val isVisible = remember { mutableStateOf(false) }

    // Control visibility of elements
    LaunchedEffect(Unit) {
        isVisible.value = true
        delay(3000L) // Duration for splash screen
        isVisible.value = false
        delay(500L) // Add a smooth fade-out effect
        onFinish()
    }

    // UI
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = isVisible.value,
                enter = fadeIn(animationSpec = androidx.compose.animation.core.tween(800)),
                exit = fadeOut(animationSpec = androidx.compose.animation.core.tween(500))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logomobile),
                    contentDescription = "Logo",
                    modifier = Modifier.padding(bottom = 16.dp).size(150.dp)
                )
            }
            AnimatedVisibility(
                visible = isVisible.value,
                enter = fadeIn(animationSpec = androidx.compose.animation.core.tween(1000)),
                exit = fadeOut(animationSpec = androidx.compose.animation.core.tween(500))
            ) {
                Text(
                    text = "Word Definition\nEnglish",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
