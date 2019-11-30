package us.bojie.paa.ui.auth

import androidx.lifecycle.ViewModel
import us.bojie.paa.repository.auth.AuthRepository
import javax.inject.Inject

class AuthViewModel
@Inject
constructor(
    val authRepository: AuthRepository
) : ViewModel() {

}