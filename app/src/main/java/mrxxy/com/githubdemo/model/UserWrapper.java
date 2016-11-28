package mrxxy.com.githubdemo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiao
 * on 16/11/28.
 */

public class UserWrapper implements Serializable{

    private int total_count;
    private boolean incomplete_results;
    private List<User> items = new ArrayList<>();

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public boolean isIncomplete_results() {
        return incomplete_results;
    }

    public void setIncomplete_results(boolean incomplete_results) {
        this.incomplete_results = incomplete_results;
    }

    public List<User> getItems() {
        return items;
    }

    public void setItems(List<User> items) {
        this.items = items;
    }

}
