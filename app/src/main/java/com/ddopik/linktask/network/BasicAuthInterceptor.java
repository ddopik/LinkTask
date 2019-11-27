package com.ddopik.linktask.network;

import android.content.Context;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * interceptor is a class whose methods are invoked when business methods on a target class are invoke
 **/
public class BasicAuthInterceptor implements Interceptor {

    private String credentials;
    private Context context;

    private String userToken;
    private String userType;
    private String lang;
//  private final String USER_NAME = "user_name";
//  private final String LOGIN_PASSWORD = "PassWord";


    public BasicAuthInterceptor(Context context) {
        this.context = context;

//        this.credentials = Credentials.basic(USER_NAME, LOGIN_PASSWORD);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Request authenticatedRequest = request.newBuilder()
                .header("x-auth-token", userToken)
                .addHeader("x-user-type", userType)
                .addHeader("x-lang-code", lang)
                .build();
        return chain.proceed(authenticatedRequest);
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}