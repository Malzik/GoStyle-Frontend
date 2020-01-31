package fr.epsi.goStyle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import fr.epsi.goStyle.model.Coupon;

public class CouponAdapter extends ArrayAdapter<Coupon> {

    public CouponAdapter(@NonNull Context context, int resource, @NonNull List<Coupon> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater li = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = li.inflate(R.layout.c_coupon, null);

        TextView textViewName=convertView.findViewById(R.id.textViewName);
        TextView textViewDeadLine=convertView.findViewById(R.id.textViewDeadLine);
        ImageView imageView=convertView.findViewById(R.id.imageViewCoupon);

        Coupon coupon=getItem(position);

        textViewName.setText(coupon.getName());
        textViewDeadLine.setText(coupon.getDeadLine());
        Picasso.get().load(coupon.getUrlPhoto()).into(imageView);
        return convertView;
    }
}
