package us.bojie.paa.ui.auth.state

import us.bojie.paa.model.AuthToken


data class AuthViewState(
    var registrationFields: RegistrationFields? = RegistrationFields(),
    var loginFields: LoginFields? = LoginFields(),
    var authToken: AuthToken? = null
)

data class RegistrationFields(
    var registrationEmail: String? = null,
    var registrationUsername: String? = null,
    var registrationPassword: String? = null,
    var registrationConfirmPassword: String? = null
) {
    class RegistrationError {
        companion object {
            fun mustFillAllFields(): String {
                return "All fields are required."
            }

            fun passwordsDoNotMatch(): String {
                return "Passwords must match."
            }

            fun none(): String {
                return "None"
            }
        }
    }

    fun isValidForRegistration(): String {
        if (registrationEmail.isNullOrEmpty() || registrationUsername.isNullOrEmpty()
            || registrationPassword.isNullOrEmpty() || registrationConfirmPassword.isNullOrEmpty()
        ) {
            return RegistrationError.mustFillAllFields()
        }

        if (!registrationPassword.equals(registrationConfirmPassword)) {
            return RegistrationError.passwordsDoNotMatch()
        }

        return RegistrationError.none()
    }
}

data class LoginFields(
    var login_email: String? = null,
    var login_password: String? = null
) {
    class LoginError {

        companion object {

            fun mustFillAllFields(): String {
                return "You can't login without an email and password."
            }

            fun none(): String {
                return "None"
            }

        }
    }

    fun isValidForLogin(): String {

        if (login_email.isNullOrEmpty()
            || login_password.isNullOrEmpty()
        ) {
            return LoginError.mustFillAllFields()
        }
        return LoginError.none()
    }
}