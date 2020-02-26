package fr.epsi.goStyle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import fr.epsi.goStyle.model.Coupon;

public class ProfileViewActivity extends GoStyleActivity {

    private ArrayList<Coupon> coupons;
    private CouponAdapter adapter;

    public static void display(AppCompatActivity activity){
        Intent intent=new Intent(activity, ProfileViewActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        super.initHeader(this);

        final ListView couponsListView = findViewById(R.id.p_coupons_list);
        coupons = new ArrayList<>();
        adapter = new CouponAdapter(this, R.layout.c_coupon, coupons);
        couponsListView.setAdapter(adapter);

        final TextView email = findViewById(R.id.p_view_email);
        final TextView firstname = findViewById(R.id.p_view_firstname);
        final TextView lastname = findViewById(R.id.p_view_lastname);

        final Button editPassword = findViewById(R.id.edit_password);
        final Button editProfile = findViewById(R.id.edit_profile);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileEditActivity.display(ProfileViewActivity.this);
            }
        });

        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfilePasswordActivity.display(ProfileViewActivity.this);
            }
        });

        this.updateProfile(this.goStyleApp.getToken(), email, firstname, lastname, couponsListView, coupons);

    }

    public void updateProfile(String token, final TextView email, final TextView firstname, final TextView lastname, final ListView couponsListView, final ArrayList<Coupon> coupons) {
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


                            JSONArray jsonArray= jsonResult.getJSONArray("offers");
                            for(int i=0; i < jsonArray.length(); i++){
                                Coupon coupon = new Coupon(jsonArray.getJSONObject(i));
                                coupons.add(coupon);
                                adapter.notifyDataSetChanged();
                            }

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

}
