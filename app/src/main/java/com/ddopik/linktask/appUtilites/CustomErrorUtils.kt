import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.androidnetworking.error.ANError
import com.ddopik.linktask.R
import com.ddopik.linktask.base.commonModel.ErrorMessageResponse
import com.ddopik.linktask.appUtilites.Constants
import com.ddopik.linktask.appUtilites.rxeventbus.RxEventBus
import com.ddopik.attendonb.app.LinkTaskApp
import com.ddopik.attendonb.network.BaseNetWorkApi.Companion.STATUS_401
import com.ddopik.attendonb.network.BaseNetWorkApi.Companion.STATUS_404
import com.ddopik.attendonb.network.BaseNetWorkApi.Companion.STATUS_500
import com.ddopik.attendonb.network.BaseNetWorkApi.Companion.STATUS_BAD_REQUEST
import com.ddopik.attendonb.utilites.rxeventbus.RxAppStatsEvent
import com.google.gson.Gson


/**
 * Created by abdalla_maged on 11/6/2018.
 */
class CustomErrorUtils {
    companion object {

        private val TAG = CustomErrorUtils::class.java.simpleName



        //Universal Error State From Server
        fun setError( contextTAG: String, throwable: Throwable?) {
            try {
                throwable.takeIf { it is ANError }.apply {
                    (throwable as ANError).errorBody?.let {
                        val errorData = throwable.errorBody
                        val statusCode = throwable.errorCode
                        val gson = Gson()
                        when (statusCode) {
                            STATUS_BAD_REQUEST -> {
                                var errorMessageResponse: ErrorMessageResponse = gson.fromJson(errorData, ErrorMessageResponse::class.java)
                                viewError(LinkTaskApp.app?.getApp()?.baseContext!!, contextTAG, errorMessageResponse)
                            }
                            STATUS_404 -> {
                                Log.e(TAG, contextTAG + "---STATUS_404--->" + STATUS_404 + "---" + throwable.response)
                            }
                            STATUS_401 -> {
                                Log.e(TAG, contextTAG + "---STATUS_401--->" + STATUS_401 + "---" + throwable.response)
                            }
                            STATUS_500 -> {
                                Log.e(TAG, contextTAG + "---STATUS_500--->" + STATUS_500 + "---" + throwable.response)
                            }
                            else -> {
                                Log.e(TAG, contextTAG + "-------unKnown_stats-------->" + throwable.response)
                            }
                        }
                        return
                    }
                    Log.e(TAG, contextTAG + "-------un_known _network_error-------->" + throwable.message)

                    if (!haveNetworkConnection(LinkTaskApp.app?.baseContext!!)) {

                        viewSnackBarError(Constants.ErrorType.ONLINE_DISCONNECTED)
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, contextTAG + "-------G_error-------->" + throwable?.message)
            }
        }


        ///PreDefined Error Code From Server
        private fun viewError(context: Context, contextTAG: String, errorMessageResponse: ErrorMessageResponse) {
//
//                    when (errorMessageResponse.code) {
//                        ERROR_STATE_1,ERROR_STATE_2 -> {
//                            Toast.makeText(context, errorMessageResponse.data.msg, Toast.LENGTH_LONG).show()
//                        }
//                        else -> {
////                            Toast.makeText(context, errorMessageResponse.data.msg, Toast.LENGTH_SHORT).show()
//                            Log.e(TAG, "$contextTAG------> ${errorMessageResponse.data.msg}")
//                        }
//                    }


        }

        /**
         * PreDefined Error Code From Server
         * --> user this mehod to handle A False state of A valid Api Call
         * ex---> the api hit was successfully 200 but call back was invalid
         */

        fun viewError(contextTAG: String, errorMessageResponse: ErrorMessageResponse) {

//            when (errorMessageResponse.code) {
//                ERROR_STATE_1 -> {
//                    Toast.makeText(LinkTaskApp.app?.getApp()?.baseContext!!, errorMessageResponse.data.msg, Toast.LENGTH_LONG).show()
//                }
//                else -> {
////                            Toast.makeText(context, errorMessageResponse.data.msg, Toast.LENGTH_SHORT).show()
//                    Log.e(TAG, "$contextTAG------> ${errorMessageResponse.data.msg}")
//                }
//            }


        }

        fun viewSnackBarError(errorType: Constants.ErrorType) {
            when (errorType){

                Constants.ErrorType.ONLINE_CONNECTED -> {
                RxEventBus.getInstance().post(RxAppStatsEvent(LinkTaskApp.app?.getString(R.string.please_check_your_internet_connection)!!,Constants.ErrorType.ONLINE_DISCONNECTED))
            }
            }


        }

         fun haveNetworkConnection(context: Context): Boolean {
            var haveConnectedWifi = false
            var haveConnectedMobile = false

            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            val netInfo = cm!!.allNetworkInfo
            for (ni in netInfo) {
                if (ni.typeName.equals("WIFI", ignoreCase = true))
                    if (ni.isConnected)
                        haveConnectedWifi = true
                if (ni.typeName.equals("MOBILE", ignoreCase = true))
                    if (ni.isConnected)
                        haveConnectedMobile = true
            }
            return haveConnectedWifi || haveConnectedMobile
        }
    }


}
// CustomErrorUtils.setError(context, TAG, throwable);