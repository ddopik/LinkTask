package com.ddopik.attendonb.network

import com.ddopik.linktask.ui.explore.model.ArticlesResponse
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Observable
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

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

        val ARTICLE_1_URL =
            "https://newsapi.org/v1/articles?source=the-next-web&apiKey=533af958594143758318137469b41ba9"
        val ARTICLE_2_URL =
            "https://newsapi.org/v1/articles?source=associated-press&apiKey=533af958594143758318137469b41ba9"


        fun getArticleList1(): Observable<ArticlesResponse> {
            return Rx2AndroidNetworking.get(ARTICLE_1_URL)
                .build()
                .getObjectObservable(ArticlesResponse::class.java)


        }

        fun getArticleList2(): Observable<ArticlesResponse> {
            return Rx2AndroidNetworking.get(ARTICLE_2_URL)
                .build()
                .getObjectObservable(ArticlesResponse::class.java)


        }

        /**
         * Configure network request and add extra CipherSuite to support older version of android
         * */
        fun initNetWorkCirtefecate(): OkHttpClient {
            try {
                // Create a trust manager that does not validate certificate chains
                // Create a trust manager that does not validate certificate chains
                val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
                    object : X509TrustManager {
                        override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {}
                        override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {}
                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }

                    }
                )


                //Using TLS 1_2 & 1_1 for HTTP/2 Server requests
                // Note : The following is suitable for my Server. Please change accordingly
                val spec = ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                    .cipherSuites(
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA
                    )
                    .build()


                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory = sslContext.getSocketFactory();

                val builder = OkHttpClient.Builder()
                builder.sslSocketFactory(sslSocketFactory)
                builder.connectionSpecs(Collections.singletonList(spec))
                builder.hostnameVerifier { hostname: String?, session: SSLSession? -> true }
                return builder.build()
            } catch (e: Exception) {
                throw  RuntimeException(e);
            }
        }


    }


}