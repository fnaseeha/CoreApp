package com.example.user.lankabellapps.services;


//import com.ecyber.ivivaanywhere.smartoffice.models.DomainData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ThejanThrimanna on 4/7/16.
 */

/**
 * Generate the Web service url using this class. #Standard#
 */
public class ServiceGenerator {

    public static <S> S CreateService(Class<S> serviceClass, String serviceUrl) {

        /*Gson gson = new GsonBuilder().setLenient().create();

OkHttpClient client = new OkHttpClient();
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("http://kafe.netai.net/")
    .client(client)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()
*/
        Gson gson = new GsonBuilder().setLenient().create();

        String protocol = "http://";
        //int ssl = new DomainData().getSsl();//
        //protocol = ssl == 1 ? "https://" : "http://";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ serviceUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(serviceClass);
    }

    public static <S> S CreateServiceForDomain(Class<S> serviceClass, String serviceUrl) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).create();

        String protocol = "https://";
        //int ssl = new DomainData().getSsl();//
        //protocol = ssl == 1 ? "https://" : "http://";


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(/*"http://"*/protocol + "10.12.14.143:8090/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(serviceClass);
    }

    public static <S> S CreateServiceHTTPS(Class<S> serviceClass, String serviceUrl) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).create();

        String protocol = "http://";
        //int ssl = new DomainData().getSsl();//
        //protocol = ssl == 1 ? "https://" : "http://";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(/*"https://"*/protocol + serviceUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(serviceClass);
    }

    public static <S> S CreateServiceWithTimeout(Class<S> serviceClass, String serviceUrl) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).create();

        OkHttpClient okHttpClient = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + serviceUrl)
                .client(okHttpClient.newBuilder().connectTimeout(100, TimeUnit.MINUTES).readTimeout(100, TimeUnit.SECONDS).writeTimeout(100, TimeUnit.SECONDS).build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(serviceClass);
    }



    public static <S> S CreateServiceWithTimeoutTest(Class<S> serviceClass, String serviceUrl) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).create();

        OkHttpClient okHttpClient = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + serviceUrl)
                .client(okHttpClient.newBuilder().connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(serviceClass);
    }




    public static Retrofit retrofit(String serviceUrl) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + serviceUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }


}
