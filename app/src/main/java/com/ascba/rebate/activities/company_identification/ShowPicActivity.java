package com.ascba.rebate.activities.company_identification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.squareup.picasso.Picasso;

public class ShowPicActivity extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic);
        initViews();
        getDataFromIntent();
    }

    private void initViews() {
        imageView = ((ImageView) findViewById(R.id.image));
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            String image = intent.getStringExtra("image");
            if(image!=null){
                Picasso.with(this).load(image).fit().centerInside().into(imageView);
            }
        }
    }
}
