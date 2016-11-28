package mrxxy.com.githubdemo.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mrxxy.com.githubdemo.R;
import mrxxy.com.githubdemo.http.RetrofitHelper;
import mrxxy.com.githubdemo.model.RepoDetail;
import mrxxy.com.githubdemo.model.User;
import mrxxy.com.githubdemo.model.UserWrapper;
import mrxxy.com.githubdemo.ui.adapter.UserListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xiao
 * on 16/11/28
 */

public class MainActivity extends AppCompatActivity implements TextWatcher {
    private final long DELAY_TIME = 600;
    private final int SCROLL_THRESHOLD = 30;

    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private ProgressDialog progress;

    private List<User> userList = new ArrayList<>();
    private List<RepoDetail> repoDetailList = new ArrayList<>();
    private String userName;
    private String preText = "";
    private Handler handler = new Handler();
    private Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {
            doSearch();
        }
    };

    private void doSearch() {
        hideSoftKeyboard();
        if (userName.equals(preText)) {
            return;
        } else {
            preText = userName;
            searchUser(userName);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        progress = new ProgressDialog(this);
        progress.setMessage("加载中...");

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > SCROLL_THRESHOLD) {
                    Glide.with(MainActivity.this).pauseRequests();
                } else {
                    Glide.with(MainActivity.this).resumeRequests();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(MainActivity.this).resumeRequests();
                }
            }
        });

        etInput.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!TextUtils.isEmpty(s)) {
            userName = s.toString().trim();
            handler.removeCallbacks(searchRunnable);
            handler.postDelayed(searchRunnable, DELAY_TIME);
        } else {
            handler.removeCallbacks(searchRunnable);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void searchUser(final String userName) {
        progress.show();
        RetrofitHelper.getInstance().getUser(userName, new Callback<UserWrapper>() {
            @Override
            public void onResponse(Call<UserWrapper> call, Response<UserWrapper> response) {
                if (response.isSuccessful()) {
                    if (response.body().getTotal_count() != 0) {
                        userList = response.body().getItems();
                        searchUserRepo(userName);
                    } else {
                        showEmptyView();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserWrapper> call, Throwable t) {
                showEmptyView();
            }
        });
    }

    private void searchUserRepo(String userName) {
        RetrofitHelper.getInstance().getUserRepo(userName, new Callback<ArrayList<RepoDetail>>() {
            @Override
            public void onResponse(Call<ArrayList<RepoDetail>> call, Response<ArrayList<RepoDetail>> response) {
                if (response.isSuccessful()) {
                    repoDetailList = response.body();
                    if (repoDetailList != null && repoDetailList.size() != 0) {
                        progress.dismiss();
                        filterUser();
                        setupRecyclerView();
                    } else {
                        showEmptyView();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RepoDetail>> call, Throwable t) {
                showEmptyView();
            }
        });
    }

    /**
     * getUserRepo只能获取到的repo对应的user是精确的，所以需要过滤，使显示正确
     */
    private void filterUser() {
        StringBuilder builder = new StringBuilder();
        User tempUser = new User();
        int id = repoDetailList.get(0).getOwner().getId();
        tempUser.setId(id);
        for (RepoDetail detail : repoDetailList) {
            String language = detail.getLanguage();
            String temp = builder.toString();
            if (!TextUtils.isEmpty(language) && !temp.contains(language))
                builder.append(language + " ");
        }
        tempUser.setLanguage(builder.toString());

        for (User user : userList) {
            if (user.equals(tempUser)) {
                user.setLanguage(tempUser.getLanguage());
            }
        }
    }

    private void setupRecyclerView() {
        UserListAdapter adapter = new UserListAdapter(this, userList);
        recycler.setAdapter(adapter);
    }

    private void showEmptyView() {
        progress.dismiss();
        Toast.makeText(this, "未找到对应用户", Toast.LENGTH_SHORT).show();
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

}
