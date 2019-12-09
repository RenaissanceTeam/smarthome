package smarthome.raspberry.external.di

import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import smarthome.library.datalibrary.FirestoreHomesReferencesStorage
import smarthome.library.datalibrary.FirestoreInstanceTokenStorage
import smarthome.library.datalibrary.FirestoreMessageQueue
import smarthome.library.datalibrary.FirestoreSmartHomeStorage
import smarthome.library.datalibrary.api.HomesReferencesStorage
import smarthome.library.datalibrary.api.InstanceTokenStorage
import smarthome.library.datalibrary.api.MessageQueue
import smarthome.library.datalibrary.api.SmartHomeStorage

// todo maybe move it to datalibrary module, but only if use koin of client side as well..
val externalModule = listOf(
        module {
            factory { FirebaseFirestore.getInstance() }
            factoryBy<HomesReferencesStorage, FirestoreHomesReferencesStorage>()
            factoryBy<MessageQueue, FirestoreMessageQueue>()
            factoryBy<SmartHomeStorage, FirestoreSmartHomeStorage>()
            factoryBy<InstanceTokenStorage, FirestoreInstanceTokenStorage>()
        }
)