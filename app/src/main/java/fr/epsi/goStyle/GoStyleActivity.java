package fr.epsi.goStyle;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import fr.epsi.goStyle.model.Coupon;

public class GoStyleActivity extends AppCompatActivity {
    protected GoStyleApp goStyleApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goStyleApp=(GoStyleApp) getApplication();
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
