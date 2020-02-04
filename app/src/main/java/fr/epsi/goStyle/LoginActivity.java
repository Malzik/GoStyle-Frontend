package fr.epsi.goStyle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    public static String token;
    private TextView tokenView;

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
        this.tokenView = findViewById(R.id.token_text);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()) {
                    try {
                        LoginActivity.this.login(email, password);
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                    catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
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

    protected void login(String email, String password) throws SQLException, MalformedURLException {
        String url = "http://10.0.2.2:8000/api/login";
        Map<String, String> loginValue = new HashMap<>();
        loginValue.put("email", email);
        loginValue.put("password", password);
        new HttpAsyTask(url, "POST", loginValue, null, new HttpAsyTask.HttpAsyTaskListener() {
            @Override
            public void webServiceDone(String result) {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    if(!jsonResult.has("erreurs")) {
                        System.out.println(jsonResult.get("token").toString());
                        LoginActivity.this.token = jsonResult.get("token").toString();
                    }
                    else {
                        System.out.println(jsonResult.get("status").toString());
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void webServiceError(Exception e) {
                // Gestion erreurs
            }
        }).execute();
    }

}
