package ru.smarthome.database.store.firebase

import android.util.Log

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import ru.smarthome.database.model.LinkedAccounts
import ru.smarthome.database.store.LinkedAccountsStorage
import ru.smarthome.database.store.listeners.LinkedAccountsListener

import ru.smarthome.constants.Constants.LINKED_ACCS_REF

object FirebaseLinkedAccountsStorage : LinkedAccountsStorage {

    private val ref: DatabaseReference

    init {
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser!!.uid // todo what if there is no current user?

        val database = FirebaseDatabase.getInstance()

        ref = database.reference
                .child(uid)
                .child(LINKED_ACCS_REF)
    }

    override fun postLinkedAccounts(linkedAccounts: LinkedAccounts) {
        ref.setValue(linkedAccounts)
    }

    override fun postLinkedAccounts(linkedAccounts: LinkedAccounts, listener: DatabaseReference.CompletionListener) {
        ref.setValue(linkedAccounts, listener)
    }

    override fun getLinkedAccounts(listener: LinkedAccountsListener) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val accounts: LinkedAccounts = dataSnapshot.value as LinkedAccounts
                listener.onLinkedAccountsReceived(accounts)
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}
