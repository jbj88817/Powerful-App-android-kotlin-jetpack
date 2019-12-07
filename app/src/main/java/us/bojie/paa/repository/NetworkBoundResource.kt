package us.bojie.paa.repository

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.*
import us.bojie.paa.ui.DataState

abstract class NetworkBoundResource<ResponseObject, ViewStateType>
    (
    isNetworkAvailable: Boolean
) {
    protected val result = MediatorLiveData<DataState<ViewStateType>>()
    protected lateinit var job: CompletableJob
    protected lateinit var coroutineScope: CoroutineScope

    @UseExperimental(InternalCoroutinesApi::class)
    private fun initNewJob():Job {
        Log.d("NetworkBoundResource", "initNewJob (line 19): ")
        job = Job()
        job.invokeOnCompletion(onCancelling = true, invokeImmediately = true, handler = object : CompletionHandler   {
            override fun invoke(cause: Throwable?) {
                if (job.isCancelled) {
                    Log.e("NetworkBoundResource", "invoke (line 23): job cancelled")
                    cause?.let { 
                        onErrorReturn(it.message, false, true)
                    }
                }
            }
        })
    }

    private fun onErrorReturn(message: String?, shouldUseDialog: Boolean, shouldUseToast: Boolean) {

    }
}