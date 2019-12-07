package us.bojie.paa.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import us.bojie.paa.ui.DataState
import us.bojie.paa.ui.Response
import us.bojie.paa.ui.ResponseType
import us.bojie.paa.util.*
import us.bojie.paa.util.Constants.Companion.NETWORK_TIMEOUT
import us.bojie.paa.util.Constants.Companion.TESTING_NETWORK_DELAY
import us.bojie.paa.util.ErrorHandling.Companion.ERROR_CHECK_NETWORK_CONNECTION
import us.bojie.paa.util.ErrorHandling.Companion.ERROR_UNKNOWN
import us.bojie.paa.util.ErrorHandling.Companion.UNABLE_TODO_OPERATION_WO_INTERNET
import us.bojie.paa.util.ErrorHandling.Companion.UNABLE_TO_RESOLVE_HOST

abstract class NetworkBoundResource<ResponseObject, ViewStateType>
    (
    isNetworkAvailable: Boolean
) {
    protected val result = MediatorLiveData<DataState<ViewStateType>>()
    protected lateinit var job: CompletableJob
    protected lateinit var coroutineScope: CoroutineScope

    init {
        setJob(initNewJob())
        setValue(DataState.loading(isLoading = true, cachedData = null))

        if (isNetworkAvailable) {
            coroutineScope.launch {
                // simulate a network delay for testing
                delay(TESTING_NETWORK_DELAY)
                withContext(Main) {
                    // make network call
                    val apiResponse = createCall()
                    result.addSource(apiResponse) { response ->
                        result.removeSource(apiResponse)
                        coroutineScope.launch {
                            handleNetworkCall(response)
                        }
                    }
                }
            }

            GlobalScope.launch(IO) {
                delay(NETWORK_TIMEOUT)
                if (!job.isCompleted) {
                    Log.e("NetworkBoundResource", " (line 52): job network timeout")
                    job.cancel(CancellationException(UNABLE_TO_RESOLVE_HOST))
                }
            }

        } else {
            onErrorReturn(
                UNABLE_TODO_OPERATION_WO_INTERNET,
                shouldUseDialog = true,
                shouldUseToast = false
            )
        }
    }

    private suspend fun handleNetworkCall(response: GenericApiResponse<ResponseObject>?) {
        when (response) {
            is ApiSuccessResponse -> {
                handleApiSuccessResponse(response)
            }

            is ApiErrorResponse -> {
                Log.e(
                    "NetworkBoundResource",
                    "handleNetworkCall (line 74): ${response.errorMessage}"
                )
                onErrorReturn(response.errorMessage, true, false)
            }

            is ApiEmptyResponse -> {
                Log.e("NetworkBoundResource", "handleNetworkCall (line 74): returned nothing")
                onErrorReturn(ERROR_UNKNOWN, true, false)
            }
        }
    }

    @UseExperimental(InternalCoroutinesApi::class)
    private fun initNewJob(): Job {
        Log.d("NetworkBoundResource", "initNewJob (line 19): ")
        job = Job()
        job.invokeOnCompletion(
            onCancelling = true,
            invokeImmediately = true,
            handler = object : CompletionHandler {
                override fun invoke(cause: Throwable?) {
                    if (job.isCancelled) {
                        Log.e("NetworkBoundResource", "invoke (line 23): job cancelled")
                        cause?.let {
                            onErrorReturn(
                                it.message,
                                shouldUseDialog = false,
                                shouldUseToast = true
                            )
                        } ?: onErrorReturn(
                            ERROR_UNKNOWN,
                            shouldUseDialog = false,
                            shouldUseToast = true
                        )
                    } else if (job.isCompleted) {
                        Log.e("NetworkBoundResource", "invoke (line 29): job completed")
                    }
                }
            })
        coroutineScope = CoroutineScope(IO + job)

        return job
    }

    fun onErrorReturn(message: String?, shouldUseDialog: Boolean, shouldUseToast: Boolean) {
        var msg = message
        var useDialog = shouldUseDialog
        var responseType: ResponseType = ResponseType.None()
        if (msg == null) {
            msg = ERROR_UNKNOWN
        } else if (ErrorHandling.isNetworkError(msg)) {
            msg = ERROR_CHECK_NETWORK_CONNECTION
            useDialog = false
        }

        if (shouldUseToast) {
            responseType = ResponseType.Toast()
        }

        if (useDialog) {
            responseType = ResponseType.Dialog()
        }

        onCompleteJob(
            DataState.error(
                response = Response(
                    message = msg,
                    responseType = responseType
                )
            )
        )
    }

    fun onCompleteJob(dateState: DataState<ViewStateType>) {
        GlobalScope.launch(Main) {
            job.complete()
            setValue(dateState)
        }
    }

    private fun setValue(dateState: DataState<ViewStateType>) {
        result.value = dateState
    }

    fun asLiveDate() = result as LiveData<DataState<ViewStateType>>

    abstract suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<ResponseObject>)

    abstract fun createCall(): LiveData<GenericApiResponse<ResponseObject>>

    abstract fun setJob(job: Job)
}