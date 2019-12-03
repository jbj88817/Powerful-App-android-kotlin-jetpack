package us.bojie.paa.di.auth

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import us.bojie.paa.api.auth.OpenApiAuthService
import us.bojie.paa.persistence.AccountPropertiesDao
import us.bojie.paa.persistence.AuthTokenDao
import us.bojie.paa.repository.auth.AuthRepository
import us.bojie.paa.session.SessionManager

@Module
class AuthModule {

    @AuthScope
    @Provides
    fun provideApiService(retrofitBuilder: Retrofit.Builder): OpenApiAuthService {
        return retrofitBuilder
            .build()
            .create(OpenApiAuthService::class.java)
    }

    @AuthScope
    @Provides
    fun provideAuthRepository(
        sessionManager: SessionManager,
        authTokenDao: AuthTokenDao,
        accountPropertiesDao: AccountPropertiesDao,
        openApiAuthService: OpenApiAuthService
    ): AuthRepository {
        return AuthRepository(
            authTokenDao,
            accountPropertiesDao,
            openApiAuthService,
            sessionManager
        )
    }
}