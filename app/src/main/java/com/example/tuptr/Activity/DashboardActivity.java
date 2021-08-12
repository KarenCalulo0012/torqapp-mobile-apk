package com.example.tuptr.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import com.example.tuptr.R;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        CardView cv_account = findViewById(R.id.account); // creating a CardView and assigning a value.
        CardView cv_request = findViewById(R.id.request); // creating a CardView and assigning a value.
        CardView cv_sign = findViewById(R.id.sign); // creating a CardView and assigning a value.

        cv_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
                startActivity(new Intent(DashboardActivity.this, HomeActivity.class));
            }
        });
        cv_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
                startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
            }
        });
        cv_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
                startActivity(new Intent(DashboardActivity.this, SignatureActivity.class));
            }
        });

    }
}
