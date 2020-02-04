package fr.epsi.goStyle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.epsi.goStyle.model.Coupon;

public class CouponDetailsActivity extends GoStyleActivity {
    private Coupon coupon;

    public static void display(CouponActivity activity, Coupon coupon){
        Intent intent=new Intent(activity,CouponDetailsActivity.class);
        intent.putExtra("coupon",coupon);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_details);
        coupon = (Coupon) getIntent().getExtras().get("coupon");
        TextView textName = findViewById(R.id.textViewNameDetails);
        TextView textViewCode= findViewById(R.id.textViewCodeDetails);
        TextView textViewDeadLine= findViewById(R.id.textViewDeadLineDetails);
        TextView textViewDescription = findViewById(R.id.textViewDescriptionDetails);
        ImageView imageView= findViewById(R.id.imageViewCouponDetails);

        textName.setText(coupon.getName());
        textViewCode.setText(coupon.getCode());
        textViewDeadLine.setText("Utiliser avant le : " + coupon.getDeadline());
        textViewDescription.setText(coupon.getDescription());
        Picasso.get().load(coupon.getLogo()).into(imageView);

        setTitle("Détails");

    }
}
