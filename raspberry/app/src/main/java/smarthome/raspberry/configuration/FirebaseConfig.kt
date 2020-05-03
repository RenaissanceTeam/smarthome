package smarthome.raspberry.configuration

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream

@Configuration
open class FirebaseConfig {

    @Value("\${firebaseDatabaseUrl}")
    private lateinit var url: String

    @Bean
    open fun createFirebaseApp(): FirebaseApp {
        return FirebaseApp.initializeApp(
                FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(FileInputStream("ignored/fcmServiceKey.json")))
                        .setDatabaseUrl(url)
                        .build()
        )
    }

}