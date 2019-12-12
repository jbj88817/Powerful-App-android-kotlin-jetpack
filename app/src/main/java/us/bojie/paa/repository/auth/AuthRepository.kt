package us.bojie.paa.repository.auth

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.codingwithmitch.openapi.api.auth.network_responses.LoginResponse
import com.codingwithmitch.openapi.api.auth.network_responses.RegistrationResponse
import kotlinx.coroutines.Job
import us.bojie.paa.api.auth.OpenApiAuthService
import us.bojie.paa.model.AccountProperties
import us.bojie.paa.model.AuthToken
import us.bojie.paa.persistence.AccountPropertiesDao
import us.bojie.paa.persistence.AuthTokenDao
import us.bojie.paa.repository.NetworkBoundResource
import us.bojie.paa.session.SessionManager
import us.bojie.paa.ui.DataState
import us.bojie.paa.ui.Response
import us.bojie.paa.ui.ResponseType
import us.bojie.paa.ui.auth.state.AuthViewState
import us.bojie.paa.ui.auth.state.LoginFields
import us.bojie.paa.ui.auth.state.RegistrationFields
import us.bojie.paa.util.ApiSuccessResponse
import us.bojie.paa.util.ErrorHandling.Companion.ERROR_SAVE_AUTH_TOKEN
import us.bojie.paa.util.ErrorHandling.Companion.GENERIC_AUTH_ERROR
import us.bojie.paa.util.GenericApiResponse
import us.bojie.paa.util.PreferenceKeys
import javax.inject.Inject

class AuthRepository
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val openApiAuthService: OpenApiAuthService,
    val sessionManager: SessionManager,
    val sharedPreferences: SharedPreferences,
    val sharedPrefsEditor: SharedPreferences.Editor
) {

    private var repositoryJob: Job? = null

    fun attemptLogin(email: String, password: String): LiveData<DataState<AuthViewState>> {
        val loginFieldError = LoginFields(email, password).isValidForLogin()
        if (loginFieldError != LoginFields.LoginError.none()) {
            return returnErrorResponse(loginFieldError, ResponseType.Dialog())
        }

        return object : NetworkBoundResource<LoginResponse, AuthViewState>(
            sessionManager.isConnectedToTheInternet()
        ) {
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<LoginResponse>) {
                Log.d("AuthRepository", "handleApiSuccessResponse (line 45): $response")

                // Incorrect login credentials counts as a 200 response from server
                if (GENERIC_AUTH_ERROR == response.body.response) {
                    return onErrorReturn(
                        response.body.errorMessage,
                        shouldUseDialog = true,
                        shouldUseToast = false
                    )
                }

                // Don't care about result here. Just insert if it doesn't exist b/c of foreign key relationship
                // with AuthToken
                accountPropertiesDao.insertOrIgnore(
                    AccountProperties(
                        response.body.pk,
                        response.body.email,
                        ""
                    )
                )

                // will return -1 if failure
                val result = authTokenDao.insert(
                    AuthToken(
                        response.body.pk,
                        response.body.token
                    )
                )

                if (result < 0) {
                    return onCompleteJob(
                        DataState.error(
                            Response(ERROR_SAVE_AUTH_TOKEN, ResponseType.Dialog())
                        )
                    )
                }

                saveAuthenticatedUserToPrefs(email)

                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            authToken = AuthToken(response.body.pk, response.body.token)
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<LoginResponse>> {
                return openApiAuthService.login(email, password)
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }
        }.asLiveDate()
    }

    fun saveAuthenticatedUserToPrefs(email: String) {
        sharedPrefsEditor.apply {
            putString(PreferenceKeys.PREVIOUS_AUTH_USER, email)
            apply()
        }
    }

    private fun returnErrorResponse(
        errorMessage: String,
        responseType: ResponseType
    ): LiveData<DataState<AuthViewState>> {
        return object : LiveData<DataState<AuthViewState>>() {
            override fun onActive() {
                super.onActive()
                value = DataState.error(
                    Response(
                        errorMessage,
                        responseType
                    )
                )
            }
        }
    }

    fun cancelActiveJobs() {
        Log.d("AuthRepository", "cancelActiveJobs (line 97): ")
        repositoryJob?.cancel()
    }


    fun attemptRegistration(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): LiveData<DataState<AuthViewState>> {
        val registrationFieldsErrors =
            RegistrationFields(email, username, password, confirmPassword).isValidForRegistration()
        if (registrationFieldsErrors != RegistrationFields.RegistrationError.none()) {
            return returnErrorResponse(registrationFieldsErrors, ResponseType.Dialog())
        }

        return object : NetworkBoundResource<RegistrationResponse, AuthViewState>(
            sessionManager.isConnectedToTheInternet()
        ) {
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<RegistrationResponse>) {
                Log.d("AuthRepository", "handleApiSuccessResponse (line 120): $response")
                if (GENERIC_AUTH_ERROR == response.body.response) {
                    return onErrorReturn(
                        response.body.errorMessage,
                        shouldUseDialog = true,
                        shouldUseToast = false
                    )
                }

                // Don't care about result here. Just insert if it doesn't exist b/c of foreign key relationship
                // with AuthToken
                accountPropertiesDao.insertOrIgnore(
                    AccountProperties(
                        response.body.pk,
                        response.body.email,
                        ""
                    )
                )

                // will return -1 if failure
                val result = authTokenDao.insert(
                    AuthToken(
                        response.body.pk,
                        response.body.token
                    )
                )

                if (result < 0) {
                    return onCompleteJob(
                        DataState.error(
                            Response(ERROR_SAVE_AUTH_TOKEN, ResponseType.Dialog())
                        )
                    )
                }

                saveAuthenticatedUserToPrefs(email)

                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            authToken = AuthToken(response.body.pk, response.body.token)
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<RegistrationResponse>> {
                return openApiAuthService.register(email, username, password, confirmPassword)
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }
        }.asLiveDate()
    }
}