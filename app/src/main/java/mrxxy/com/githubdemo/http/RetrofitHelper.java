package mrxxy.com.githubdemo.http;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import mrxxy.com.githubdemo.BuildConfig;
import mrxxy.com.githubdemo.model.RepoDetail;
import mrxxy.com.githubdemo.model.UserWrapper;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xiao
 * on 16/11/28.
 */

public class RetrofitHelper {
    private static OkHttpClient okHttpClient = null;
    private static GitHubApi gitHubServer = null;
    private static RetrofitHelper instance = null;

    public static RetrofitHelper getInstance() {
        synchronized (RetrofitHelper.class) {
            if (instance == null) {
                instance = new RetrofitHelper();
            }
        }
        return instance;
    }

    private RetrofitHelper(){
        initOkHttp();
        init();
    }

    private void init() {
        gitHubServer = getGitHubServer();
    }

    private static void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        okHttpClient = builder.build();
    }

    private GitHubApi getGitHubServer() {
        Retrofit githubRetrofit = new Retrofit.Builder()
                .baseUrl(GitHubApi.HOST)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return githubRetrofit.create(GitHubApi.class);
    }

    public void getUser(String userName, Callback<UserWrapper> callback) {
        Call<UserWrapper> userWrapperCall = gitHubServer.getUser(userName);
        enqueue(userWrapperCall, callback);
    }

    public void getUserRepo(String userName, Callback<ArrayList<RepoDetail>> callback) {
        Call<ArrayList<RepoDetail>> userRepoWrapperCall = gitHubServer.getUserRepo(userName);
        enqueue(userRepoWrapperCall, callback);
    }

    private <T> void enqueue(Call<T> call, Callback<T> callback) {
        if (call != null && callback != null) {
            call.enqueue(callback);
        }
    }

}
