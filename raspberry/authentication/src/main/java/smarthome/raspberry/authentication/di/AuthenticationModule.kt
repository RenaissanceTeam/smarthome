package smarthome.raspberry.authentication.di

import android.content.Context
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import org.koin.experimental.builder.singleBy
import smarthome.library.datalibrary.api.boundary.UserIdHolder
import smarthome.raspberry.authentication.R
import smarthome.raspberry.authentication.api.domain.GetAuthStatusUseCase
import smarthome.raspberry.authentication.api.domain.GetUserIdUseCase
import smarthome.raspberry.authentication.api.domain.GetUserInfoUseCase
import smarthome.raspberry.authentication.api.flow.SignInFlowLauncher
import smarthome.raspberry.authentication.data.AuthRepo
import smarthome.raspberry.authentication.data.AuthRepoImpl
import smarthome.raspberry.authentication.data.UserIdHolderImpl
import smarthome.raspberry.authentication.data.command.SignInCommand
import smarthome.raspberry.authentication.data.command.SignInCommandImpl
import smarthome.raspberry.authentication.data.command.SignOutCommand
import smarthome.raspberry.authentication.data.command.SignOutCommandImpl
import smarthome.raspberry.authentication.data.mapper.*
import smarthome.raspberry.authentication.data.query.GetUserQuery
import smarthome.raspberry.authentication.data.query.GetUserQueryImpl
import smarthome.raspberry.authentication.domain.GetAuthStatusUseCaseImpl
import smarthome.raspberry.authentication.domain.GetUserIdUseCaseImpl
import smarthome.raspberry.authentication.domain.GetUserInfoUseCaseImpl
import smarthome.raspberry.authentication.flow.SignInFlowLauncherImpl
import smarthome.raspberry.authentication.presentation.AuthenticationActivity
import smarthome.raspberry.authentication.presentation.AuthenticationPresenter
import smarthome.raspberry.authentication.presentation.AuthenticationPresenterImpl
import smarthome.raspberry.authentication.presentation.AuthenticationView
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
    singleBy<UserIdHolder, UserIdHolderImpl>()
    factoryBy<SignInCommand, SignInCommandImpl>()
    factoryBy<SignOutCommand, SignOutCommandImpl>()
    factoryBy<GetUserQuery, GetUserQueryImpl>()
    factoryBy<CredentialsToAuthCredentialsMapper, CredentialsToAuthCredentialsMapperImpl>()
    factoryBy<GoogleSignInAccountToCredentialsMapper, GoogleSignInAccountToCredentialsMapperImpl>()
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
    scope(named<AuthenticationActivity>()) {
        scoped<AuthenticationPresenter> { (view: AuthenticationView) ->
            AuthenticationPresenterImpl(
                    view = view,
                    authenticateWithCredentialsUseCase = get(),
                    signOutOfFirebaseUseCase = get(),
                    mainFlowLauncher = get(),
                    clearHomeInfoUseCase = get(),
                    getAuthStatusUseCase = get(),
                    getUserInfoUseCase = get()
            )
        }
    }
}

val authenticationModule: List<Module> = listOf(
        presentation,
        domain,
        data,
        flow
)