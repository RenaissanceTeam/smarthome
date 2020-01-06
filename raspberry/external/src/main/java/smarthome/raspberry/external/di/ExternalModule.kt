package smarthome.raspberry.external.di

import org.koin.dsl.module


// todo maybe move it to datalibrary module, but only if use koin of client side as well..
val externalModule = listOf(
        module {
//            factory { FirebaseFirestore.getInstance() }
//            factoryBy<HomesReferencesStorage, FirestoreHomesReferencesStorage>()
//            factoryBy<MessageQueue, FirestoreMessageQueue>()
//            factoryBy<SmartHomeStorage, FirestoreSmartHomeStorage>()
//            factoryBy<InstanceTokenStorage, FirestoreInstanceTokenStorage>()
        }
)