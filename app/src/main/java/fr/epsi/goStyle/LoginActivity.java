package fr.epsi.goStyle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText emailInput = findViewById(R.id.email_input);
        final EditText passwordInput = findViewById(R.id.password_input);
        final Button loginButton = findViewById(R.id.connexion_button);
        final Button registerButton = findViewById(R.id.inscription_button);
        this.tokenView = findViewById(R.id.token_text);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                try {
                    LoginActivity.this.tokenView.setText("heyyy");
                    LoginActivity.this.login(email, password);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
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
                    JSONObject token = new JSONObject(result);
                    LoginActivity.this.token = token.get("token").toString();
                } catch (JSONException e) {
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
