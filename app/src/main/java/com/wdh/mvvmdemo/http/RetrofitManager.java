package com.wdh.mvvmdemo.http;

import com.wdh.mvvmdemo.BuildConfig;
import com.wdh.mvvmdemo.MyApplication;
import com.wdh.mvvmdemo.bean.WeXinData;
import com.wdh.mvvmdemo.utils.NetworkUtil;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class RetrofitManager {

    private String BASE_URL = "http://v.juhe.cn/";
    private static final int READ_TIME_OUT = 10;
    private static final int CONNECT_TIME_OUT = 10;
    private Api api;

    private RetrofitManager() {

        //指定缓存路径,缓存大小100Mb    内置存储
        Cache cache = new Cache(new File(MyApplication.getInstance().getCacheDir(), "gamesystemCache"),
                1024 * 1024 * 100);
        //设置缓存路径 内置存储
        //File httpCacheDirectory = new File(context.getCacheDir(), "responses");
        //外部存储
//        File httpCacheDirectory = new File(context.getExternalCacheDir(), "responses");
//        设置缓存 10M
//        int cacheSize = 10 * 1024 * 1024;
//        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //设置超时
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(getInterceptor())
                .cache(cache).addInterceptor(new cacheInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .build();
        api= retrofit.create(Api.class);

    }

    /**
     * 单例对象持有者
     */
    private static class SingletonHolder {
        private static final RetrofitManager INSTANCE = new RetrofitManager();
    }

    /**
     * 获取ApiManager单例对象
     * @return
     */
    public static RetrofitManager getApiInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Api getService() {
        return api;
    }

    public Observable<WeXinData> weixinData(Map<String, String> params){
       return api.WeiXinRxjava(params);
    }

    /**
     * 打印返回的json数据拦截器
     */
    private  HttpLoggingInterceptor getInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // 测试
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE); // 打包
        }
        return interceptor;
    }

    private class cacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtil.isAvailable(MyApplication.getInstance())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            if (NetworkUtil.isAvailable(MyApplication.getInstance())) {
               /* int maxAge = 0;
                // 有网络时 设置缓存超时时间0个小时*/
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                response.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .build();
            } else {
                // 无网络时，设置超时为1周
                int maxStale = 60 * 60 * 24 * 7;
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
            return response;
        }
    }
}
