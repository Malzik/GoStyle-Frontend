package fr.epsi.goStyle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import fr.epsi.goStyle.model.Coupon;

public class CouponActivity extends GoStyleActivity {

    private CouponAdapter adapter;
    private ArrayList<Coupon> coupons;

    public static void display(AppCompatActivity activity){
        Intent intent=new Intent(activity, CouponActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        super.initHeader(this);
        coupons=new ArrayList<>();
        ListView listView=findViewById(R.id.list_coupons);
        adapter=new CouponAdapter(this,R.layout.c_coupon,coupons);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CouponDetailsActivity.display(CouponActivity.this,coupons.get(position));
            }
        });
        String urlStr="http://192.168.43.49:8000/offers";

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


    }

    private void initData(String data) {
        try {
            JSONArray jsonArray= new JSONArray(data);
            for(int i=0;i<jsonArray.length();i++){
                Coupon coupon=new Coupon(jsonArray.getJSONObject(i));
                coupons.add(coupon);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }
}