package com.example.helply

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener

class EmergencyActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency)
        supportActionBar?.hide();

        sharedPreferences = getSharedPreferences("ContactsPrefs", MODE_PRIVATE)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // przycisk do wysylania sms z lokalizacja
        val emergencyButton: Button = findViewById(R.id.emergencyButton)
        emergencyButton.setOnClickListener {
            checkLocationPermissionAndSendSMS()
        }
        // Przycisk do dzwonienia na pierwszy kontakt awaryjny
        val emergencyCallButton: Button = findViewById(R.id.emergencyCall)
        emergencyCallButton.setOnClickListener {
            callFirstEmergencyContact()
        }
    }

    // Funkcja sprawdzająca uprawnienia i wysyłająca SMS
    private fun checkLocationPermissionAndSendSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Uprawnienia są przyznane, pobieramy lokalizację
            getLocationAndSendSMS()
        } else {
            // Uprawnienia nie zostały przyznane, pytamy o nie
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    // Obsługa wyniku zapytania o uprawnienia
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Jeśli uprawnienie zostało przyznane, pobieramy lokalizację
                getLocationAndSendSMS()
            } else {
                // Jeśli uprawnienie nie zostało przyznane
                Toast.makeText(this, "Nie przyznano uprawnienia do lokalizacji", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Pobranie lokalizacji i wysłanie SMS-a do wszystkich kontaktów
    @SuppressLint("MissingPermission")
    fun getLocationAndSendSMS() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener(this, OnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
                    val message = "Pomoc! Moja lokalizacja: http://maps.google.com/?q=$lat,$lon"

                    sendSMSToContacts(message)
                } else {
                    Toast.makeText(this, "Nie udało się uzyskać lokalizacji", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Wysłanie SMS do każdego kontaktu
    private fun sendSMSToContacts(message: String) {
        val contacts = getSavedContacts()  // Pobierz zapisane kontakty
        for (contact in contacts) {
            val phoneNumber = contact.phone
            sendSMS(phoneNumber, message)  // Wyślij SMS do każdego kontaktu
        }
    }

    // Funkcja do wysyłania SMS
    private fun sendSMS(phoneNumber: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(this, "SMS wysłany do: $phoneNumber", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Błąd wysyłania SMS: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Dzwonienie na pierwszy kontakt awaryjny
    private fun callFirstEmergencyContact() {
        val contacts = getSavedContacts()
        if (contacts.isNotEmpty()) {
            val firstContact = contacts[0]  // Pierwszy kontakt
            val phoneNumber = firstContact.phone

            val dialIntent = Intent(Intent.ACTION_CALL)
            dialIntent.data = Uri.parse("tel:$phoneNumber")

            // Sprawdzanie uprawnienia do dzwonienia
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(dialIntent)
            } else {
                Toast.makeText(this, "Brak uprawnienia do wykonywania połączeń", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Brak zapisanych kontaktów awaryjnych", Toast.LENGTH_SHORT).show()
        }
    }

    // Pobieranie zapisanych kontaktów
    private fun getSavedContacts(): List<Contact> {
        val contactNames = sharedPreferences.getString("contact_names", "")
        val contactPhones = sharedPreferences.getString("contact_phones", "")

        val contacts = mutableListOf<Contact>()

        if (!contactNames.isNullOrEmpty() && !contactPhones.isNullOrEmpty()) {
            val names = contactNames.split(",")
            val phones = contactPhones.split(",")
            for (i in names.indices) {
                if (i < phones.size && names[i].isNotEmpty() && phones[i].isNotEmpty()) {
                    contacts.add(Contact(names[i], phones[i]))
                }
            }
        }

        return contacts
    }
}