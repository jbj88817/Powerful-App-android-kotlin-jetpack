package us.bojie.paa.ui.auth


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import us.bojie.paa.R
import us.bojie.paa.ui.DataState
import us.bojie.paa.ui.DataStateChangeListener
import us.bojie.paa.ui.Response
import us.bojie.paa.ui.ResponseType
import us.bojie.paa.util.Constants


class ForgotPasswordFragment : BaseAuthFragment() {

    private lateinit var webView: WebView
    lateinit var stateChangeListener: DataStateChangeListener

    private val webInteractionCallback: WebAppInterface.OnWebInteractionCallback =
        object : WebAppInterface.OnWebInteractionCallback {
            override fun onSuccess(email: String) {
                Log.d("ForgotPasswordFragment", "onSuccess (line 26): ")
                onPasswordResetLinkSent()
            }

            override fun onError(errorMessage: String) {
                Log.d("ForgotPasswordFragment", "onError (line 31): ")
                val dataState = DataState.error<Any>(
                    response = Response(errorMessage, responseType = ResponseType.Dialog())
                )
                stateChangeListener.onDataStateChange(
                    dataState = dataState
                )
            }

            override fun onLoading(isLoading: Boolean) {
                Log.d("ForgotPasswordFragment", "onLoading (line 43): ")
                GlobalScope.launch(Main) {
                    stateChangeListener.onDataStateChange(
                        DataState.loading(isLoading = isLoading, cachedData = null)
                    )
                }
            }
        }

    private fun onPasswordResetLinkSent() {
        GlobalScope.launch(Main) {
            parent_view.removeView(webView)
            webView.destroy()

            val animation = TranslateAnimation(
                password_reset_done_container.width.toFloat(),
                0f,
                0f,
                0f
            )
            animation.duration = 500
            password_reset_done_container.startAnimation(animation)
            password_reset_done_container.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView = view.findViewById(R.id.webview)
        loadWebView()

        return_to_launcher_fragment.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebView() {
        stateChangeListener.onDataStateChange(
            DataState.loading(isLoading = true, cachedData = null)
        )

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                stateChangeListener.onDataStateChange(
                    DataState.loading(isLoading = false, cachedData = null)
                )
            }
        }

        webView.loadUrl(Constants.PASSWORD_RESET_URL)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(
            WebAppInterface(webInteractionCallback),
            "AndroidTextListener"
        )
    }

    class WebAppInterface
    constructor(
        private val callback: OnWebInteractionCallback
    ) {

        @JavascriptInterface
        fun onSuccess(email: String) {
            callback.onSuccess(email)
        }

        @JavascriptInterface
        fun onError(errorMessage: String) {
            callback.onError(errorMessage)
        }

        @JavascriptInterface
        fun onLoading(isLoading: Boolean) {
            callback.onLoading(isLoading)
        }

        interface OnWebInteractionCallback {
            fun onSuccess(email: String)
            fun onError(errorMessage: String)
            fun onLoading(isLoading: Boolean)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            stateChangeListener = context as DataStateChangeListener
        } catch (e: ClassCastException) {
            Log.e(
                "ForgotPasswordFragment",
                "onAttach (line 38): $context must implement DataStateChangeListener"
            )
        }
    }
}
