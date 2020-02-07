package fr.epsi.goStyle;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import fr.epsi.goStyle.model.Coupon;

public class CouponActivity extends GoStyleActivity {

    private CouponAdapter adapter;
    private ArrayList<Coupon> couponsSave;
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

        ImageView backButton = findViewById(R.id.home_button);
        backButton.setVisibility(View.GONE);


        coupons=new ArrayList<>();
        couponsSave=new ArrayList<>();
        ListView listView=findViewById(R.id.list_coupons);
        adapter=new CouponAdapter(this,R.layout.c_coupon,coupons);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CouponDetailsActivity.display(CouponActivity.this, coupons.get(position));
            }
        });



        SearchView searchView = findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchOffer(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchOffer(newText);
                return false;
            }
        });
        try {
            String url = PropertyUtil.getProperty("base_url", getApplicationContext()) + "offers";

            new HttpAsyTask(url, "GET", null, goStyleApp.getToken(), new HttpAsyTask.HttpAsyTaskListener() {
                @Override
                public void webServiceDone(String result) {
                    System.out.println(result);
                    initData(result);
                }

                @Override
                public void webServiceError(Exception e) {
                    System.out.println(e.getMessage());
                    displayToast(e.getMessage());
                }
            }).execute();
        } catch (IOException e) {
            displayToast(e.getMessage());
        }
    }

    private void searchOffer(String query) {
        coupons.clear();
        for (Coupon coupon: couponsSave) {
            if(coupon.getName().toLowerCase().contains(query))
                coupons.add(coupon);

        }
        adapter.notifyDataSetChanged();
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
        couponsSave.addAll(coupons);
        adapter.notifyDataSetChanged();
    }
}
