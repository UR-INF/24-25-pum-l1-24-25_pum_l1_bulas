package com.example.helply

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactListAdapter(private var contactList: MutableList<Contact>, private val deleteListener: (Contact) -> Unit) : RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.bind(contact)

        // Obsługa przycisku usuwania
        holder.deleteButton.setOnClickListener {
            // Wywołanie funkcji usuwania kontaktu
            deleteListener(contact)
        }
    }

    override fun getItemCount(): Int = contactList.size

    fun updateData(newContactList: List<Contact>) {
        contactList.clear()
        contactList.addAll(newContactList)
        notifyDataSetChanged() // Odświeżenie widoku
    }

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.contactName)
        private val phoneTextView: TextView = itemView.findViewById(R.id.contactPhone)
        val deleteButton: Button = itemView.findViewById(R.id.deleteContactButton)

        fun bind(contact: Contact) {
            nameTextView.text = contact.name
            phoneTextView.text = contact.phone
        }
    }
}