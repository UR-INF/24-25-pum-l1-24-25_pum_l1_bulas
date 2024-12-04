package com.example.helply

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ContactsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addContactsButton: Button
    private lateinit var contactListAdapter: ContactListAdapter
    private lateinit var sharedPreferences: SharedPreferences

    private val addContactRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        recyclerView = findViewById(R.id.recycleView)
        addContactsButton = findViewById(R.id.addContactsButton)
        sharedPreferences = getSharedPreferences("ContactsPrefs", MODE_PRIVATE)

        // Inicjalizacja RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Tworzymy adapter z przekazaniem funkcji usuwania kontaktu
        contactListAdapter = ContactListAdapter(getSavedContacts().toMutableList()) { contact ->
            // Usuwanie kontaktu
            deleteContact(contact)
        }

        recyclerView.adapter = contactListAdapter

        // Obsługa przycisku dodawania kontaktu
        addContactsButton.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            startActivityForResult(intent, addContactRequestCode)
        }
    }

    // Obsługa wyniku z AddContactActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == addContactRequestCode && resultCode == Activity.RESULT_OK) {
            // Odświeżenie kontaktów po dodaniu nowego
            contactListAdapter.updateData(getSavedContacts())
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

    // Funkcja do usuwania kontaktu
    private fun deleteContact(contact: Contact) {
        val sharedPreferences = getSharedPreferences("ContactsPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Pobieramy listę kontaktów
        val contactList = getSavedContacts().toMutableList()

        // Usuwamy kontakt z listy
        contactList.remove(contact)

        // Zapisujemy zaktualizowaną listę w SharedPreferences
        val contactNames = contactList.joinToString(",") { it.name }
        val contactPhones = contactList.joinToString(",") { it.phone }

        editor.putString("contact_names", contactNames)
        editor.putString("contact_phones", contactPhones)
        editor.apply()

        // Zaktualizowanie adaptera
        contactListAdapter.updateData(contactList)
    }
}