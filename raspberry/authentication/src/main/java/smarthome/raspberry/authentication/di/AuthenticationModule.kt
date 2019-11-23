package smarthome.raspberry.authentication.di

import android.content.Context
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import org.koin.experimental.builder.singleBy
import smarthome.raspberry.authentication.R
import smarthome.raspberry.authentication.api.domain.GetAuthStatusUseCase
import smarthome.raspberry.authentication.api.domain.GetUserIdUseCase
import smarthome.raspberry.authentication.api.domain.GetUserInfoUseCase
import smarthome.raspberry.authentication.api.flow.SignInFlowLauncher
import smarthome.raspberry.authentication.data.AuthRepo
import smarthome.raspberry.authentication.data.AuthRepoImpl
import smarthome.raspberry.authentication.data.mapper.FirebaseUserToUserMapper
import smarthome.raspberry.authentication.data.mapper.FirebaseUserToUserMapperImpl
import smarthome.raspberry.authentication.domain.GetAuthStatusUseCaseImpl
import smarthome.raspberry.authentication.domain.GetUserIdUseCaseImpl
import smarthome.raspberry.authentication.domain.GetUserInfoUseCaseImpl
import smarthome.raspberry.authentication.flow.SignInFlowLauncherImpl
import smarthome.raspberry.util.ResourceProvider

private val domain = module {
    factoryBy<GetAuthStatusUseCase, GetAuthStatusUseCaseImpl>()
    factoryBy<GetUserIdUseCase, GetUserIdUseCaseImpl>()
    factoryBy<GetUserInfoUseCase, GetUserInfoUseCaseImpl>()
}

private val flow = module {
    factoryBy<SignInFlowLauncher, SignInFlowLauncherImpl>()
}

private val data = module {
    factoryBy<FirebaseUserToUserMapper, FirebaseUserToUserMapperImpl>()
    singleBy<AuthRepo, AuthRepoImpl>()
    factory { FirebaseAuth.getInstance() }
    factory { provideGoogleApiClient(get(), get()) }
}

private fun provideGoogleApiClient(resources: ResourceProvider, context: Context): GoogleApiClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resources.resources.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

    return GoogleApiClient.Builder(context)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
}

private val presentation = module {
    // todo - how to provide view inside presenter
}


fun authenticationModule(
        frameworkDependentModule: Module? = null
): List<Module> = listOf(
        presentation,
        domain,
        data,
        flow
).apply {
    this + frameworkDependentModule
}
