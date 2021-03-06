package fr.epsi.goStyle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends GoStyleActivity {

    private TextView loginErrors;

    public static void display(AppCompatActivity activity){
        Intent intent=new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText emailInput = findViewById(R.id.login_email);
        final EditText passwordInput = findViewById(R.id.login_password);
        final Button loginButton = findViewById(R.id.connexion_button);
        final Button registerButton = findViewById(R.id.inscription_button);
        this.loginErrors = findViewById(R.id.login_errors);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();


                if(email.isEmpty() || password.isEmpty()) {
                    displayToast("Certains champs sont vides");
                    return;
                }

                try {
                    login(email, password);
                }
                catch (SQLException e) {
                    displayToast("La requête n'a pas pu être traitée");
                }
                catch (MalformedURLException e) {
                    displayToast("Connexion impossible");
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.display(LoginActivity.this);
            }
        });
    }

    public void setToken(String token) {
        this.goStyleApp.setToken(token);
    }

    protected void login(String email, String password) throws SQLException, MalformedURLException {
        try {
            String url = PropertyUtil.getProperty("base_url", getApplicationContext()) + "login";
            final Map<String, String> loginValue = new HashMap<>();
            loginValue.put("email", email);
            loginValue.put("password", password);
            new HttpAsyTask(url, "POST", loginValue, null, new HttpAsyTask.HttpAsyTaskListener() {
                @Override
                public void webServiceDone(String result) {
                    try {
                        JSONObject jsonResult = new JSONObject(result);
                        System.out.println(jsonResult.toString());
                        if(jsonResult.has("code") && Integer.parseInt(jsonResult.get("code").toString()) >= 400) {
                            loginErrors.setText(jsonResult.get("message").toString());
                        }
                        else {
                            System.out.println(jsonResult.get("token").toString());
                            setToken(jsonResult.get("token").toString());
                            CouponActivity.display(LoginActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void webServiceError(Exception e) {
                    System.out.println(e.getMessage());
                }
            }).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
