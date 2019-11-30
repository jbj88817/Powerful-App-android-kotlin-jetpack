package us.bojie.paa.ui.auth

import androidx.lifecycle.ViewModel
import us.bojie.paa.repository.auth.AuthRepository

class AuthViewModel
constructor(
    val authRepository: AuthRepository
) : ViewModel() {

}