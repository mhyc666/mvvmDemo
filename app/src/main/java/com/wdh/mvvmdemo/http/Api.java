package com.wdh.mvvmdemo.http;


import com.wdh.mvvmdemo.bean.WeXinData;

import java.util.Map;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface Api {

    @GET("weixin/query")
    Observable<WeXinData> WeiXinRxjava(@QueryMap Map<String, String> params );
}
