package fr.epsi.goStyle;

import android.app.Application;
import android.view.View;
import android.widget.Button;

public class GoStyleApp extends Application {

    private String title="";
    private String token;
    @Override
    public void onCreate() {

        super.onCreate();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    protected void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
