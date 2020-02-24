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

public class ProfilePasswordActivity extends GoStyleActivity {

    private TextView newPasswordError;

    public static void display(AppCompatActivity activity){
        Intent intent=new Intent(activity, ProfilePasswordActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        super.initHeader(this);

        this.newPasswordError = findViewById(R.id.new_password);

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
            String url = PropertyUtil.getProperty("base_url", getApplicationContext()) + "user/password";
            new HttpAsyTask(url, "PUT", parameters, this.goStyleApp.getToken(), new HttpAsyTask.HttpAsyTaskListener() {
                @Override
                public void webServiceDone(String result) {
                    try {
                        if(!result.isEmpty() && result.startsWith("[")) {
                            JSONArray errors = new JSONArray(result);
                            for(int i=0; i < errors.length(); i++){
                                if(errors.getJSONObject(i).get("property_path").toString().equals("password")) {
                                    newPasswordError.setText(errors.getJSONObject(i).get("message").toString());
                                }
                            }
                        }
                        else {
                            displayToast("Le mot de passe à été mis à jour");
                            ProfileEditActivity.display(ProfilePasswordActivity.this);
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