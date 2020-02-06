package fr.epsi.goStyle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
        try {
            String url = PropertyUtil.getProperty("base_url", getApplicationContext()) + "register";
            Map<String, String> registerValue = new HashMap<>();
            registerValue.put("first_name", firstname);
            registerValue.put("last_name", lastname);
            registerValue.put("email", email);
            registerValue.put("password", password);
            new HttpAsyTask(url, "POST", registerValue, null, new HttpAsyTask.HttpAsyTaskListener() {
                @Override
                public void webServiceDone(String result) {
                    try {
                        if(result.startsWith("[")) {
                            JSONArray errors = new JSONArray(result);
                            for(int i=0;i<errors.length();i++){
                                initErrors(errors.getJSONObject(i));
                            }
                        } else {
                            LoginActivity.display(RegisterActivity.this);
                        }
                    }
                    catch (JSONException e) {
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

    private void initErrors(JSONObject jsonObject) throws JSONException {
        switch (jsonObject.get("property_path").toString()) {
            case "email":
                System.out.println(jsonObject.get("message").toString());
            case "first_name":
                System.out.println(jsonObject.get("message").toString());
            case "last_name":
                System.out.println(jsonObject.get("message").toString());
            case "password":
                System.out.println(jsonObject.get("message").toString());
        }
    }
}
