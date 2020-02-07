package fr.epsi.goStyle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import fr.epsi.goStyle.model.Coupon;

public class CouponDetailsActivity extends GoStyleActivity {
    private Coupon coupon;
    private String qrcode;

    public static void display(GoStyleActivity activity, Coupon coupon){
        Intent intent=new Intent(activity,CouponDetailsActivity.class);
        intent.putExtra("coupon",coupon);
        activity.startActivity(intent);
    }

    public static void display(GoStyleActivity activity, String qrcode){
        Intent intent=new Intent(activity,CouponDetailsActivity.class);
        intent.putExtra("qrcode",qrcode);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_details);
        super.initHeader(this);

        TextView textName = findViewById(R.id.textViewNameDetails);
        TextView textViewCode= findViewById(R.id.textViewCodeDetails);
        TextView textViewDeadLine= findViewById(R.id.textViewDeadLineDetails);
        TextView textViewDescription = findViewById(R.id.textViewDescriptionDetails);
        ImageView imageView= findViewById(R.id.imageViewCouponDetails);
        Button btnAddOffers = findViewById(R.id.add_offers);
        qrcode = (String) getIntent().getExtras().get("qrcode");

        btnAddOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addOffers(coupon.getCode());
                } catch (IOException e) {
                    System.out.println("Le coupon n'a pas été enregistrer");
                }
            }
        });


        if (qrcode != null){
            try {
                String url = PropertyUtil.getProperty("base_url", getApplicationContext()) + "offers/" + qrcode;

                new HttpAsyTask(url, "GET", null, this.goStyleApp.getToken(), new HttpAsyTask.HttpAsyTaskListener() {
                    @Override
                    public void webServiceDone(String result) {
                        TextView textName = findViewById(R.id.textViewNameDetails);
                        TextView textViewCode= findViewById(R.id.textViewCodeDetails);
                        TextView textViewDeadLine= findViewById(R.id.textViewDeadLineDetails);
                        TextView textViewDescription = findViewById(R.id.textViewDescriptionDetails);
                        ImageView imageView= findViewById(R.id.imageViewCouponDetails);
                        initData(result);
                        setData(textName, textViewCode, textViewDeadLine, textViewDescription, imageView);
                    }

                    @Override
                    public void webServiceError(Exception e) {
                        System.out.println(e.getMessage());
                        displayToast(e.getMessage());
                    }
                }).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            coupon = (Coupon) getIntent().getExtras().get("coupon");
            setData(textName, textViewCode, textViewDeadLine, textViewDescription, imageView);
        }
        setTitle("Détails");
    }

    private void addOffers(String code) throws IOException {
        String url = PropertyUtil.getProperty("base_url", getApplicationContext()) + "user/addoffer";
        Map<String, String> offer = new HashMap<>();
        offer.put("code", code);
        new HttpAsyTask(url, "PUT", offer, this.goStyleApp.getToken(), new HttpAsyTask.HttpAsyTaskListener() {
            @Override
            public void webServiceDone(String result) {
                System.out.println(result);
                CouponActivity.display(CouponDetailsActivity.this);
            }

            @Override
            public void webServiceError(Exception e) {
                System.out.println(e.getMessage());
                displayToast(e.getMessage());
            }
        }).execute();
    }

    private void setData(TextView textName, TextView textViewCode, TextView textViewDeadLine, TextView textViewDescription, ImageView imageView){
        textName.setText(coupon.getName());
        textViewCode.setText(coupon.getCode());
        textViewDeadLine.setText("Utiliser avant le : " + coupon.getDeadline());
        textViewDescription.setText(coupon.getDescription());
        Picasso.get().load(coupon.getLogo()).into(imageView);
    }

    private void initData(String data) {
        try {
            JSONObject jsonObject= new JSONObject(data);
            coupon = new Coupon(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
