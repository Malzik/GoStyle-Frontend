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

    private TextView emailError;
    private TextView firstnameError;
    private TextView lastnameError;
    private TextView passwordError;
    private TextView confirmationError;

    public static void display(AppCompatActivity activity){
        Intent intent=new Intent(activity, RegisterActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.emailError = findViewById(R.id.register_email_error);
        this.firstnameError = findViewById(R.id.register_firstname_error);
        this.lastnameError = findViewById(R.id.register_lastname_error);
        this.passwordError = findViewById(R.id.register_password_error);
        this.confirmationError = findViewById(R.id.register_confirmation_error);

        final Button register = findViewById(R.id.inscription_button);
        final Button login = findViewById(R.id.connexion_button);
        final EditText email = findViewById(R.id.register_email);
        final EditText firstname = findViewById(R.id.register_firstname);
        final EditText lastname = findViewById(R.id.register_lastname);
        final EditText password = findViewById(R.id.register_password);
        final EditText confirmation = findViewById(R.id.confirmation_password);

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String passwordText = password.getText().toString();
                String confirmationText = confirmation.getText().toString();
                String emailText = email.getText().toString();
                String firstnameText = firstname.getText().toString();
                String lastnameText = lastname.getText().toString();

                if(passwordText.equals(confirmationText)) {
                    register(emailText, firstnameText, lastnameText, passwordText);
                }
                else{
                    confirmationError.setText("Les mots de passe ne correspondent pas");
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
                this.emailError.setText(jsonObject.get("message").toString());
                break;
            case "first_name":
                this.firstnameError.setText(jsonObject.get("message").toString());
                break;
            case "last_name":
                this.lastnameError.setText(jsonObject.get("message").toString());
                break;
            case "password":
                this.passwordError.setText(jsonObject.get("message").toString());
                break;
        }
    }
}
