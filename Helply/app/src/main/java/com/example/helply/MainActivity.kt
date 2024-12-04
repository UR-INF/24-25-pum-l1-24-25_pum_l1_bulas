package com.example.helply

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var emergencyButton: Button
    private lateinit var emergencyContactsButton: Button

    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.SEND_SMS,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.CALL_PHONE
    )
    private val REQUEST_CODE_PERMISSIONS = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Sprawdzanie i proszenie o uprawnienia podczas pierwszego uruchomienia
        if (!hasRequiredPermissions()) {
            requestPermissions()
        }

        emergencyButton = findViewById(R.id.emergencyButton)
        emergencyContactsButton = findViewById(R.id.emergencyContactsButton)

        emergencyButton.setOnClickListener {
            triggerEmergency()
        }

        emergencyContactsButton.setOnClickListener {
            openContacts()
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        // Sprawdzamy, czy wszystkie wymagane uprawnienia zostały przyznane
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        // Prosimy użytkownika o wymagane uprawnienia
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }


    // Obsługa wyniku zapytania o uprawnienia
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Jeśli wszystkie uprawnienia zostały przyznane
                Toast.makeText(this, "Uprawnienia przyznane", Toast.LENGTH_SHORT).show()
            } else {
                // Jeśli jakiekolwiek uprawnienie zostało odrzucone
                Toast.makeText(
                    this,
                    "Uprawnienia muszą zostać przyznane, aby aplikacja mogła działać poprawnie.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun triggerEmergency() {
        val intent = Intent(this, EmergencyActivity::class.java)
        startActivity(intent)
    }

    private fun openContacts() {
        val intent = Intent(this, ContactsActivity::class.java)
        startActivity(intent)
    }
}