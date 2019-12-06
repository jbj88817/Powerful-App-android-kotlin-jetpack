package us.bojie.paa.ui.auth

import androidx.lifecycle.LiveData
import us.bojie.paa.model.AuthToken
import us.bojie.paa.repository.auth.AuthRepository
import us.bojie.paa.ui.BaseViewModel
import us.bojie.paa.ui.DataState
import us.bojie.paa.ui.auth.state.AuthStateEvent
import us.bojie.paa.ui.auth.state.AuthStateEvent.*
import us.bojie.paa.ui.auth.state.AuthViewState
import us.bojie.paa.ui.auth.state.LoginFields
import us.bojie.paa.ui.auth.state.RegistrationFields
import us.bojie.paa.util.AbsentLiveData
import javax.inject.Inject

class AuthViewModel
@Inject
constructor(
    val authRepository: AuthRepository
) : BaseViewModel<AuthStateEvent, AuthViewState>() {

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    override fun handleStateEvent(stateEvent: AuthStateEvent): LiveData<DataState<AuthViewState>> {
        return when (stateEvent) {
            is LoginAttemptEvent -> {
                authRepository.attemptLogin(stateEvent.email, stateEvent.password)
            }

            is RegisterAttemptEvent -> {
                authRepository.attemptRegistration(
                    stateEvent.email, stateEvent.username,
                    stateEvent.password, stateEvent.confirmPassword
                )
            }

            is CheckPreviousAuthEvent -> {
                AbsentLiveData.create()
            }
        }
    }

    fun setRegistrationFields(registrationFields: RegistrationFields) {
        val update = getCurrentViewStateOrNew()
        if (update.registrationFields == registrationFields) {
            return
        }
        update.registrationFields = registrationFields
        setViewState(update)
    }

    fun setLoginFields(loginFields: LoginFields) {
        val update = getCurrentViewStateOrNew()
        if (update.loginFields == loginFields) {
            return
        }
        update.loginFields = loginFields
        setViewState(update)
    }

    fun setAuthToken(authToken: AuthToken) {
        val update = getCurrentViewStateOrNew()
        if (update.authToken == authToken) {
            return
        }
        update.authToken = authToken
        setViewState(update)
    }
}