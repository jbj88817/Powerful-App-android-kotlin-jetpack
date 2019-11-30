package us.bojie.paa.repository.auth

import us.bojie.paa.api.auth.OpenApiAuthService
import us.bojie.paa.persistence.AccountPropertiesDao
import us.bojie.paa.persistence.AuthTokenDao
import us.bojie.paa.session.SessionManager
import javax.inject.Inject

class AuthRepository
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val openApiAuthService: OpenApiAuthService,
    val sessionManager: SessionManager
)