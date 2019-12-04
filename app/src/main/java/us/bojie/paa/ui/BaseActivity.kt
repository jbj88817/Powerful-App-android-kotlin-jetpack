package us.bojie.paa.ui

import dagger.android.support.DaggerAppCompatActivity
import us.bojie.paa.session.SessionManager
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {

    val TAG: String = "AppDebug"

    @Inject
    lateinit var sessionManager: SessionManager
}