package com.example.helply

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.helply.ui.theme.HelplyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelplyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        setContentView(R.layout.activity_main)


        emergencyButton = findViewById(R.id.emergencyButton)
        emergencyContactsButton = findViewById(R.id.emergencyContactsButton)

        emergencyButton.setOnClickListener {
            triggerEmergency()
        }

        emergencyContactsButton.setOnClickListener {
            openContacts()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HelplyTheme {
        Greeting("Android")
    private fun triggerEmergency() {
        val intent = Intent(this, EmergencyActivity::class.java)
        startActivity(intent)
    }

    private fun openContacts() {
        val intent = Intent(this, ContactsActivity::class.java)
        startActivity(intent)
    }
}