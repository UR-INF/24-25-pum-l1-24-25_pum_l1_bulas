package com.example.helply

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddContactActivity : AppCompatActivity() {

    private lateinit var selectContactButton: Button
    private lateinit var contactDetailsTextView: TextView
    private lateinit var saveContactButton: Button

    private val contactRequestCode = 1
    private var selectedContactName: String? = null
    private var selectedContactPhone: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        selectContactButton = findViewById(R.id.selectContactButton)
        contactDetailsTextView = findViewById(R.id.contactDetailsTextView)
        saveContactButton = findViewById(R.id.saveContactButton)

        // Obsługa przycisku wyboru kontaktu
        selectContactButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
            startActivityForResult(intent, contactRequestCode)
        }

        // Obsługa przycisku zapisu kontaktu
        saveContactButton.setOnClickListener {
            if (selectedContactName != null && selectedContactPhone != null) {
                saveContact(selectedContactName!!, selectedContactPhone!!)

                // Przekazanie wyniku do ContactsActivity
                val resultIntent = Intent()
                setResult(Activity.RESULT_OK, resultIntent) // Przekazujemy wynik
                finish() // Zakończenie aktywności i powrót do ContactsActivity
            } else {
                Toast.makeText(this, "Nie wybrano kontaktu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Funkcja obsługująca wynik wyboru kontaktu
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == contactRequestCode && resultCode == Activity.RESULT_OK) {
            val contactUri = data?.data

            // Wybieranie informacji o kontakcie
            val cursor = contentResolver.query(contactUri!!, null, null, null, null)

            cursor?.let {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    val phoneIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                    selectedContactName = it.getString(nameIndex)
                    selectedContactPhone = it.getString(phoneIndex)

                    // Wyświetlenie wybranego kontaktu
                    contactDetailsTextView.text = "Kontakt: $selectedContactName\nTelefon: $selectedContactPhone"
                }
                it.close()
            }
        }
    }

    // Funkcja zapisu kontaktu (w SharedPreferences)
    private fun saveContact(name: String, phone: String) {
        val sharedPreferences = getSharedPreferences("ContactsPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Pobranie istniejących kontaktów
        val contactList = getSavedContacts().toMutableList()

        // Sprawdzenie, czy kontakt już istnieje
        if (contactList.none { it.name == name && it.phone == phone }) {
            // Dodanie nowego kontaktu
            contactList.add(Contact(name, phone))

            // Zapisanie danych kontaktów w SharedPreferences
            val contactNames = contactList.joinToString(",") { it.name }
            val contactPhones = contactList.joinToString(",") { it.phone }

            editor.putString("contact_names", contactNames)
            editor.putString("contact_phones", contactPhones)
            editor.apply()

            // Powiadomienie użytkownika, że kontakt został zapisany
            Toast.makeText(this, "Kontakt zapisany: $name - $phone", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Ten kontakt już jest zapisany.", Toast.LENGTH_SHORT).show()
        }
    }

    // Funkcja do pobrania zapisanych kontaktów
    private fun getSavedContacts(): List<Contact> {
        val sharedPreferences = getSharedPreferences("ContactsPrefs", MODE_PRIVATE)
        val contactNames = sharedPreferences.getString("contact_names", "")
        val contactPhones = sharedPreferences.getString("contact_phones", "")

        val contacts = mutableListOf<Contact>()

        contactNames?.let {
            val names = it.split(",")
            val phones = contactPhones?.split(",") ?: emptyList()

            for (i in names.indices) {
                if (i < phones.size) {
                    contacts.add(Contact(names[i], phones[i]))
                }
            }
        }

        return contacts
    }
}