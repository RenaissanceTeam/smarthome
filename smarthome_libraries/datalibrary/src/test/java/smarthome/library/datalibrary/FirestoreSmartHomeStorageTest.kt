package smarthome.library.datalibrary

import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.library.datalibrary.api.boundary.HomeIdHolder

class FirestoreSmartHomeStorageTest {

    private lateinit var storage: FirestoreSmartHomeStorage
    private val homeId = BehaviorSubject.create<String>()
    private lateinit var collection: CollectionReference
//    private lateinit var documentReference: DocumentReference
    private lateinit var task: Task<Void>
    private lateinit var db: FirebaseFirestore

    @Before
    fun setup() {
        collection = mock { }
        task = mock {
            on { addOnSuccessListener(any()) }.then {
                it.getArgument<OnSuccessListener<Any>>(0).onSuccess(Unit)
                task
            }
            on { addOnFailureListener(any()) }.then { task }
        }
        db = mock {
            on { collection(any()) }.then { collection }
        }
        val homeIdHolder = object : HomeIdHolder {
            override fun get() = homeId.value.orEmpty()
        }

        storage = FirestoreSmartHomeStorage(db, homeIdHolder)
    }

    @Test
    fun `when homeid holder changes state should change references`() {
        val documentReference = mock<DocumentReference> {
            on { set(any()) }.then { task }
        }
        val otherDocumentReference = mock<DocumentReference> {
            on { set(any()) }.then { task }
        }
        whenever(collection.document("1")).then { documentReference }
        whenever(collection.document("2")).then { otherDocumentReference }

        runBlocking {
            homeId.onNext("1")
            storage.createSmartHome()
            verify(documentReference).set(any())

            homeId.onNext("2")
            storage.createSmartHome()
            verify(otherDocumentReference).set(any())

        }
    }
}