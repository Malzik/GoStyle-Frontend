package fr.epsi.goStyle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private TextView registerErrors;

    public static void display(AppCompatActivity activity){
        Intent intent=new Intent(activity, RegisterActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Button register = findViewById(R.id.inscription_button);
        final Button login = findViewById(R.id.connexion_button);
        final EditText email = findViewById(R.id.register_email);
        final EditText firstname = findViewById(R.id.register_firstname);
        final EditText lastname = findViewById(R.id.register_lastname);
        final EditText password = findViewById(R.id.register_password);
        final EditText confirmation = findViewById(R.id.confirmation_password);
        this.registerErrors = findViewById(R.id.register_errors);

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String passwordText = password.getText().toString();
                String confirmationText = confirmation.getText().toString();
                String emailText = email.getText().toString();
                String firstnameText = firstname.getText().toString();
                String lastnameText = lastname.getText().toString();
                if(emailText.isEmpty() || firstnameText.isEmpty() || lastnameText.isEmpty() || passwordText.isEmpty()) {
                    registerErrors.setText("Certains champs sont vides");
                    return;
                }

                if(passwordText.equals(confirmationText)) {
                    register(emailText, firstnameText, lastnameText, passwordText);
                }
                else{
                    registerErrors.setText("Les mots de passe ne correspondent pas");
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.display(RegisterActivity.this);
            }
        });
    }

    protected void register(String email, String firstname, String lastname, String password) {
        String url = "http://10.0.2.2:8000/api/register";
        Map<String, String> registerValue = new HashMap<>();
        registerValue.put("first_name", firstname);
        registerValue.put("last_name", lastname);
        registerValue.put("email", email);
        registerValue.put("password", password);
        new HttpAsyTask(url, "POST", registerValue, null, new HttpAsyTask.HttpAsyTaskListener() {
            @Override
            public void webServiceDone(String result) {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    if(!jsonResult.has("erreurs") && jsonResult.get("responseCode").equals(201)) {
                        LoginActivity.display(RegisterActivity.this);
                    }
                    else {
                        System.out.println(jsonResult.get("erreurs").toString());
                        registerErrors.setText("Le formulaire est invalide");
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
