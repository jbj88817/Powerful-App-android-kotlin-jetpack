package us.bojie.paa.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.codingwithmitch.openapi.api.auth.network_responses.LoginResponse
import us.bojie.paa.repository.auth.AuthRepository
import us.bojie.paa.util.GenericApiResponse
import javax.inject.Inject

class AuthViewModel
@Inject
constructor(
    val authRepository: AuthRepository
) : ViewModel() {
    fun testLogin(): LiveData<GenericApiResponse<LoginResponse>> {
        return authRepository.testLoginRequest(
            "test123@gmail.com",
            "123456"
        )
    }

}