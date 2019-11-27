package com.ddopik.attendonb.network

class BaseNetWorkApi {
    companion object {


        //Network Status
        var STATUS_OK = "200"
        var DEFAULT_USER_TYPE = "0"
        val STATUS_BAD_REQUEST = 400
        val STATUS_401 = 401
        val STATUS_404 = 404
        val STATUS_500 = 500
        var STATUS_ERROR = "405"
        val ERROR_STATE_1 = "login-400"
        val ERROR_STATE_2 = "attend by ip - 400"



//
//        fun login(userName: String, password: String, currentLat: String, currentLng: String, deviceImei: String): Observable<LoginResponse> {
//            return Rx2AndroidNetworking.post(LOGIN_URL)
//                    .addBodyParameter("username", userName)
//                    .addBodyParameter("pass", password)
//                    .addBodyParameter("latitude", currentLat)
//                    .addBodyParameter("longitude", currentLng)
//                    .addBodyParameter("imei", deviceImei)
//                    .addPathParameter(LANGUAGE_PATH_PARAMETER, PrefUtil.getAppLanguage(AttendOnBApp.app!!))
//                    .setPriority(Priority.HIGH)
//                    .build()
//                    .getObjectObservable(LoginResponse::class.java)
//        }



    }


}