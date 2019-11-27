package com.ddopik.attendonb.base

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ddopik.linktask.utilites.Constants
import com.ddopik.linktask.utilites.rxeventbus.RxEventBus
import com.google.android.material.snackbar.Snackbar
import com.ddopik.attendonb.utilites.rxeventbus.RxForceRefreshEvent

import io.reactivex.disposables.CompositeDisposable


abstract class BaseActivity : AppCompatActivity() {

    private val TAG = BaseActivity::class.java.simpleName
    private var snackBar: Snackbar? = null
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    abstract fun initObservers()

    protected fun addFragment(containerId: Int, fragment: Fragment, title: String?, tag: String?, stackState: Boolean) {

        if (stackState) {
            supportFragmentManager.beginTransaction().replace(containerId, fragment, tag).addToBackStack(tag).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(containerId, fragment, tag).commit()
        }
    }

    fun showToast(msg: String) {
        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
    }

    fun showLog(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    override fun onResume() {
        super.onResume()
        //        snackBar?.addCallback(getSnackBarCallBack())
        val disposable = RxEventBus.getInstance().connectionStatsSubject.subscribe({ event ->
            when (event?.messageType) {
                Constants.ErrorType.ONLINE_DISCONNECTED -> {
                    showSnackBar(messag = event.message, action = View.OnClickListener {
                        RxEventBus.getInstance().post(RxForceRefreshEvent(true))
                    }, dismiss = false, actionText = "refresh")
                }
            }

        }, { throwable ->
            Log.e(TAG, throwable.message)
        })
        disposables.add(disposable)
    }


    private fun showSnackBar(messag: String, action: View.OnClickListener, dismiss: Boolean, actionText: String) {
//        if (!snackBar?.isShown!!) {
        snackBar = Snackbar.make(findViewById<View>(android.R.id.content), "", Snackbar.LENGTH_INDEFINITE)
        snackBar?.setText(messag)
        snackBar?.setAction(actionText, action)
        snackBar?.show()
 //        }

    }


 }