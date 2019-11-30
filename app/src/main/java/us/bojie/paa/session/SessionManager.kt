package us.bojie.paa.session

import android.app.Application
import us.bojie.paa.persistence.AuthTokenDao

class SessionManager
constructor(
    val authTokenDao: AuthTokenDao,
    val application: Application
) {

}