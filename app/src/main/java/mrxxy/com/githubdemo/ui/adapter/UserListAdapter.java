package mrxxy.com.githubdemo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mrxxy.com.githubdemo.R;
import mrxxy.com.githubdemo.model.User;

/**
 * Created by xiao
 * on 16/11/28.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {

    private Context context;
    private List<User> list;

    public UserListAdapter(Context context, List<User> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.layout_item_user_list, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = list.get(position);
        Glide.with(context).load(user.getAvatar_url()).into(holder.ivUser);
        holder.tvName.setText("UserName: " + user.getLogin());
        String language = user.getLanguage();
        language = TextUtils.isEmpty(language) ? "" : language;
        holder.tvLanguage.setText("Language: " + language);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_user)
        ImageView ivUser;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_language)
        TextView tvLanguage;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


