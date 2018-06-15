package com.example.astrodashalib.provider.rest;

import android.support.annotation.NonNull;

import com.example.astrodashalib.BuildConfig;
import com.example.astrodashalib.data.service.BirthDetailService;
import com.example.astrodashalib.data.service.DownloadChatService;
import com.example.astrodashalib.data.service.FayeTokenService;
import com.example.astrodashalib.data.service.PaymentService;
import com.example.astrodashalib.data.service.UserService;
import com.example.astrodashalib.helper.PrefHelper;
import com.example.astrodashalib.helper.Util;
import com.example.astrodashalib.utils.BaseConfiguration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by himanshu on 25/09/17.
 */

public class RestProvider {

    private static Retrofit provideRxRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BaseConfiguration.BASE_URL)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private static Retrofit provideChatRxRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Util.CHAT_BASE_URL)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }



    private static OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            client.addInterceptor(new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        client.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        });
        client.connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES);
        return client.build();
    }

    @NonNull
    public static BirthDetailService getBirthDetailService() {
        return provideRxRetrofit().create(BirthDetailService.class);
    }

    @NonNull
    public static FayeTokenService getFayeTokenService() {
        return provideChatRxRetrofit().create(FayeTokenService.class);
    }

    @NonNull
    public static DownloadChatService getDownloadChatService() {
        return provideChatRxRetrofit().create(DownloadChatService.class);
    }

    @NonNull
    public static UserService getUserService() {
        return provideRxRetrofit().create(UserService.class);
    }

    @NonNull
    public static PaymentService getPaymentService() {
        return provideRxRetrofit().create(PaymentService.class);
    }



}
