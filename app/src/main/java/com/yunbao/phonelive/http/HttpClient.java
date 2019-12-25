package com.yunbao.phonelive.http;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.MemoryCookieStore;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.yunbao.phonelive.AppConfig;
import com.yunbao.phonelive.AppContext;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by cxf on 2018/9/17.
 */

public class HttpClient {

    private static final int TIMEOUT = 10000;
    private static HttpClient sInstance;
    private OkHttpClient mOkHttpClient;
    private String mLanguage;//语言
    private String mUrl;
    private String mUrlOhter;
    private OkHttpClient okHttpClient;

    private HttpClient() {
        mUrl = AppConfig.HOST + "/api/public/?service=";
        mUrlOhter=AppConfig.HOST+"/index.php?g=";
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();


    }


    public static HttpClient getInstance() {
        if (sInstance == null) {
            synchronized (HttpClient.class) {
                if (sInstance == null) {
                    sInstance = new HttpClient();
                }
            }
        }
        return sInstance;
    }


    public void init() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(TIMEOUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS);
        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));
        builder.retryOnConnectionFailure(true);
//        Dispatcher dispatcher = new Dispatcher();
//        dispatcher.setMaxRequests(20000);
//        dispatcher.setMaxRequestsPerHost(10000);
//        builder.dispatcher(dispatcher);

        //输出HTTP请求 响应信息
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("http");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.HEADERS);
        builder.addInterceptor(loggingInterceptor);
        mOkHttpClient = builder.build();

        OkGo.getInstance().init(AppContext.sInstance)
                .setOkHttpClient(mOkHttpClient)
                .setCacheMode(CacheMode.NO_CACHE)
                .setRetryCount(1);

    }

    public GetRequest<JsonBean> get(String serviceName, String tag) {
        return OkGo.<JsonBean>get(mUrl + serviceName)
                .headers("Connection","keep-alive")
                .tag(tag)
                .params(HttpConsts.LANGUAGE, mLanguage);
    }

    public GetRequest<JsonBean> getT(String tag) {
        return OkGo.<JsonBean>get("http://lhwhk.com/api/public/?service=User.getNobleList")
                .headers("Connection","keep-alive")
                .tag(tag)
                .params(HttpConsts.LANGUAGE, mLanguage);
    }


    public GetRequest<JsonBean> getTixian(String serviceName, String tag) {
        return OkGo.<JsonBean>get(mUrlOhter+ serviceName)
                .headers("Connection","keep-alive")
                .tag(tag)
                .params(HttpConsts.LANGUAGE, mLanguage);
    }





    public void get(String url, final Callback netCallback) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(netCallback);
    }


    public GetRequest<JsonBean> getOther(String url, String tag) {
        return OkGo.<JsonBean>get(url)
                .headers("Connection","keep-alive")
                .tag(tag)
                .params(HttpConsts.LANGUAGE, mLanguage);
    }

    public PostRequest<JsonBean> post(String serviceName, String tag) {
        return OkGo.<JsonBean>post(mUrl + serviceName)
                .headers("Connection","keep-alive")
                .tag(tag)
                .params(HttpConsts.LANGUAGE, mLanguage);
    }

    public void cancel(String tag) {
        OkGo.cancelTag(mOkHttpClient, tag);
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

}
