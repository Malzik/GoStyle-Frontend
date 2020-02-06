package fr.epsi.goStyle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileEditActivity extends GoStyleActivity {

    public static void display(AppCompatActivity activity){
        Intent intent=new Intent(activity, ProfileEditActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        super.initHeader(this);
        final EditText email = findViewById(R.id.profile_email);
        final EditText firstname = findViewById(R.id.profile_firstname);
        final EditText lastname = findViewById(R.id.profile_lastname);
        final EditText newPassword = findViewById(R.id.new_password);
        final Button saveProfile = findViewById(R.id.save_profile_button);

        this.updateProfile(this.goStyleApp.getToken(), email, firstname, lastname);

        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailText = email.getText().toString();
                String firstnameText = firstname.getText().toString();
                String lastnameText = lastname.getText().toString();

                Map<String, String> parameters = new HashMap<>();

                if(!emailText.isEmpty())
                    parameters.put("email", emailText);

                if(!firstnameText.isEmpty())
                    parameters.put("first_name", firstnameText);

                if(!lastnameText.isEmpty())
                    parameters.put("last_name", lastnameText);

                saveProfile(parameters);
            }
        });

    }

    public void updateProfile(String token, final EditText email, final EditText firstname, final EditText lastname) {
        try {
            String url = PropertyUtil.getProperty("base_url", getApplicationContext()) + "user";
            new HttpAsyTask(url, "GET", null, this.goStyleApp.getToken(), new HttpAsyTask.HttpAsyTaskListener() {
                @Override
                public void webServiceDone(String result) {
                    try {
                        JSONObject jsonResult = new JSONObject(result);
                        if(!jsonResult.has("erreurs")) {
                            email.setText(jsonResult.get("email").toString());
                            firstname.setText(jsonResult.get("first_name").toString());
                            lastname.setText(jsonResult.get("last_name").toString());
                        }
                        else {
                            System.out.println("L'utilisateur n'a pas pu être récupérer");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveProfile(Map<String, String> parameters) {
        try {
            String url = PropertyUtil.getProperty("base_url", getApplicationContext()) + "user";
            new HttpAsyTask(url, "PUT", parameters, this.goStyleApp.getToken(), new HttpAsyTask.HttpAsyTaskListener() {
                @Override
                public void webServiceDone(String result) {
                    try {
                        JSONObject jsonResult = new JSONObject(result);
                        if(!jsonResult.has("erreurs")) {
                            ProfileEditActivity.display(ProfileEditActivity.this);
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
                    System.out.println(e);
                }
            }).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
