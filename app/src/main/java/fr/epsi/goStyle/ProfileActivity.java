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

public class ProfileActivity extends GoStyleActivity {

    public static void display(GoStyleActivity activity){
        Intent intent=new Intent(activity, ProfileActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText email = findViewById(R.id.profile_email);
        final EditText firstname = findViewById(R.id.profile_firstname);
        final EditText lastname = findViewById(R.id.profile_lastname);
        final EditText newPassword = findViewById(R.id.new_password);
        final EditText password = findViewById(R.id.profile_password);
        final EditText confirmation = findViewById(R.id.confirmation_password);
        final Button saveProfile = findViewById(R.id.save_profile_button);

        saveProfile.setOnClickListener(new View.OnClickListener() {

            String emailText = email.getText().toString();
            String firstnameText = firstname.getText().toString();
            String lastnameText = lastname.getText().toString();
            String newPasswordText = newPassword.getText().toString();
            String passwordText = password.getText().toString();
            String confirmationText = confirmation.getText().toString();

            @Override
            public void onClick(View v) {
                if(passwordText.equals(confirmationText)) {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("email", emailText);
                    parameters.put("first_name", firstnameText);
                    parameters.put("last_name", lastnameText);

                    if(newPasswordText.isEmpty()) {
                        parameters.put("password", newPasswordText);
                    }

                    saveProfile(parameters);
                }
            }
        });
    }

    public void saveProfile(Map<String, String> parameters) {
        new HttpAsyTask("http://10.0.2.2:8000/api/user", "PUT", parameters, this.goStyleApp.getToken(), new HttpAsyTask.HttpAsyTaskListener() {
            @Override
            public void webServiceDone(String result) {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    if(!jsonResult.has("erreurs")) {
                        ProfileActivity.display(ProfileActivity.this);
                    }
                    else {
                        System.out.println("La requête n'a pas été traitée correctement");
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
