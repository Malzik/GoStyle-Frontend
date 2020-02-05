package fr.epsi.goStyle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        qrcode = (String) getIntent().getExtras().get("qrcode");

        if (qrcode != null){
            String urlStr="http://192.168.43.49:8000/offers/" + qrcode;
            new HttpAskTask(urlStr, new HttpAskTask.HttpAsyTaskListener() {
                @Override
                public void webServiceDone(String result) {
                    initData(result);
                }

                @Override
                public void webServiceError(Exception e) {
                    displayToast(e.getMessage());
                }
            }).execute();

            setData(textName, textViewCode, textViewDeadLine, textViewDescription, imageView);
        }
        else
        {
            coupon = (Coupon) getIntent().getExtras().get("coupon");
            setData(textName, textViewCode, textViewDeadLine, textViewDescription, imageView);
        }

        setTitle("Détails");

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
            Coupon couponNew = new Coupon(jsonObject);
            coupon = couponNew;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
