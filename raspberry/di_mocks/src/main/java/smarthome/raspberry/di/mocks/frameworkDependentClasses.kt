package smarthome.raspberry.di.mocks

import com.google.firebase.auth.FirebaseAuth
import com.nhaarman.mockitokotlin2.mock
import org.koin.dsl.module

val frameworkDependentClasses = module(override = true) {
    single { mock<FirebaseAuth> { }}
}