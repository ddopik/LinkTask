package com.ddopik.linktask.appUtilites.networkstatus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;
import com.ddopik.attendonb.app.LinkTaskApp;


public class NetworkChangeBroadcastReceiver extends BroadcastReceiver {

    private  final  String TAG=NetworkChangeBroadcastReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean connected = networkInfo != null && networkInfo.isConnected();
        Log.e(TAG,"onReceive --->"+connected);
        if(!connected){
            Toast.makeText(LinkTaskApp.Companion.getApp().getBaseContext(),"Check your internet connicetion",Toast.LENGTH_SHORT).show();
//            RxEventBus.getInstance().post(new RxAppStatsEvent(RxAppStatsEvent.Type.ONLINE_DISCONNECTED, connected));
        }

    }
}
