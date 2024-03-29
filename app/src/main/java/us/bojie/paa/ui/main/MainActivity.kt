package us.bojie.paa.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import us.bojie.paa.R
import us.bojie.paa.ui.BaseActivity
import us.bojie.paa.ui.auth.AuthActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tool_bar.setOnClickListener {
            sessionManager.logout()
        }
        subscribeObservers()
    }

    fun subscribeObservers() {
        sessionManager.cachedToken.observe(this, Observer { authToken ->
            Log.d("MainActivity", "subscribeObservers (line 18): ")
            if (authToken == null || authToken.account_pk == -1 || authToken.token == null) {
                navAuthActivity()
            }
        })
    }

    private fun navAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun displayProgressBar(isDisplay: Boolean) {
        if(isDisplay) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
        }
    }
}
