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

public class ProfilePasswordActivity extends GoStyleActivity {

    public static void display(AppCompatActivity activity){
        Intent intent=new Intent(activity, ProfilePasswordActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        super.initHeader(this);

        final EditText newPassword = findViewById(R.id.new_password);
        final EditText oldPassword = findViewById(R.id.oldPassword);
        final Button savePassword = findViewById(R.id.save_password_button);

        savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newPasswordText = newPassword.getText().toString();
                String oldPasswordText = oldPassword.getText().toString();

                if(!newPasswordText.isEmpty() && !oldPasswordText.isEmpty()) {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("new_password", newPasswordText);
                    parameters.put("password", oldPasswordText);
                    saveProfile(parameters);
                }
            }
        });

    }


    public void saveProfile(Map<String, String> parameters) {
        try {
            String url = PropertyUtil.getProperty("base_url", getApplicationContext()) + "password";
            new HttpAsyTask(url, "PUT", parameters, this.goStyleApp.getToken(), new HttpAsyTask.HttpAsyTaskListener() {
                @Override
                public void webServiceDone(String result) {
                    try {
                        JSONObject jsonResult = new JSONObject(result);
                        if(!jsonResult.has("erreurs")) {
                            ProfileEditActivity.display(ProfilePasswordActivity.this);
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