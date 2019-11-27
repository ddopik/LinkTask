package com.ddopik.attendonb.utilites.rxeventbus

/**
 * Created by ddopik on 23,May,2019
 * ddopik.01@gmail.com
 * brandeda.net company,
 * cairo, Egypt.
 */
class RxForceRefreshEvent(val refreshStats:Boolean) {
    private var rerFresh: Boolean = refreshStats

    fun isRefreshed(): Boolean {
        return rerFresh
    }


}