package mrxxy.com.githubdemo.model;

import java.io.Serializable;

/**
 * Created by xiao
 * on 16/11/28.
 */

public class User implements Serializable {

    private String login;
    private int id;
    private String avatar_url;
    private String language;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            if (((User) obj).getId() == id) {
                return true;
            }
        }
        return false;
    }
}
