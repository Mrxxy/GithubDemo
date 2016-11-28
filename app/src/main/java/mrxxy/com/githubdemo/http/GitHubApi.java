package mrxxy.com.githubdemo.http;

import java.util.ArrayList;

import mrxxy.com.githubdemo.model.RepoDetail;
import mrxxy.com.githubdemo.model.UserWrapper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by xiao
 * on 16/11/28.
 */

public interface GitHubApi {
    String HOST = "https://api.github.com/";

    @GET("search/users")
    Call<UserWrapper> getUser(@Query("q") String userName);

    @GET("users/{userName}/repos")
    Call<ArrayList<RepoDetail>> getUserRepo(@Path("userName") String userName);
}
