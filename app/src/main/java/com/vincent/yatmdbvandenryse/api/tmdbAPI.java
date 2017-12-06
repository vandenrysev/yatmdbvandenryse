package com.vincent.yatmdbvandenryse.api;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.vincent.yatmdbvandenryse.Config;
/**
 * Created by vincent on 22/11/17.
 */

public class tmdbAPI {

    private Retrofit rest;
    private tmdbInterface serviceApi;

    /**
     * Client modifié pour envoyer la cle a chaque requête
     * @return Client modifié
     */
    public OkHttpClient createOkHttpClient()
    {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("api_key",Config.apiKey).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        });

        // Pour loguer les request
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        builder.addInterceptor(interceptor);

        return builder.build();

    }

    public tmdbAPI()
    {


        rest = new Retrofit.Builder()
                .baseUrl(Config.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build();

        serviceApi = rest.create(tmdbInterface.class);

    }

    public Retrofit getRest() {
        return rest;
    }

    public tmdbInterface getServiceApi() {
        return serviceApi;
    }
}
