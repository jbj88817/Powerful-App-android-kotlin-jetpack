package us.bojie.paa.ui.main.account

import android.content.Context
import android.util.Log
import dagger.android.support.DaggerFragment
import us.bojie.paa.ui.DataStateChangeListener

abstract class BaseAccountFragment : DaggerFragment() {

    val TAG: String = "AppDebug"

    lateinit var stateChangeListener: DataStateChangeListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            stateChangeListener = context as DataStateChangeListener
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement DataStateChangeListener")
        }
    }
}
















