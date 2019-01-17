package smarthome.datalibrary.database.store.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import smarthome.datalibrary.database.constants.Constants.LINKED_ACCS_REF
import smarthome.datalibrary.database.store.LinkedAccountsStorage
import smarthome.datalibrary.database.store.listeners.LinkedAccountsListener
import smarthome.datalibrary.database.model.LinkedAccounts

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
