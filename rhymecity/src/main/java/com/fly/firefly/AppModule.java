package com.fly.firefly;

import android.util.Log;

import com.fly.firefly.api.ApiEndpoint;
import com.fly.firefly.api.ApiRequestInterceptor;
import com.fly.firefly.api.ApiService;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

@Module(
        injects = FireFlyApplication.class
)
public class AppModule {

    private final String apiKey;

    public AppModule(String apiKey) {
        this.apiKey = apiKey;
    }

    @Provides
    @Singleton
    RequestInterceptor provideRequestInterceptor() {
        return new ApiRequestInterceptor(apiKey);
    }

    @Provides
    @Singleton
    Endpoint provideEndpoint() {
        return new ApiEndpoint();
    }

    @Provides
    @Singleton
    ApiService provideApiService(RequestInterceptor requestInterceptor, Endpoint endpoint) {
        return new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new RestAdapter.Log() {
                    public void log(String msg) {
                        Log.i("retrofit", msg);
                    }
                })
                .build()
                .create(ApiService.class);
    }


   /* builder.setLogLevel(LogLevel.FULL).setLog(new RestAdapter.Log() {
        public void log(String msg) {
            Log.i("retrofit", msg);
        }
    });*/

    @Provides
    @Singleton
    Bus provideBus() {
        return new Bus();
    }
}
