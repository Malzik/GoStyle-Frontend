package fr.epsi.goStyle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GoStyleActivity extends AppCompatActivity {

    protected GoStyleApp goStyleApp;
    protected ImageView qrcode;
    protected ImageView home;
    protected ImageView profil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        goStyleApp=(GoStyleApp) getApplication();
    }

    public void initHeader(final GoStyleActivity activity){
        qrcode = findViewById(R.id.qrCode_image);
        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanActivity.display(activity);
            }
        });
        home = findViewById(R.id.home_button);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CouponActivity.display(activity);
            }
        });
        profil = findViewById(R.id.profil_image);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity.display(activity);
            }
        });

    }



    protected void setTitle(String title){
        TextView textViewTitle=findViewById(R.id.current_page_title);
        if(textViewTitle!=null){
            textViewTitle.setText(title);
        }
    }

    protected void displayToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
