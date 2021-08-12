package com.example.tuptr.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import com.example.tuptr.R;

public class ProfileActivity extends AppCompatActivity {
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        ProfileActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        CardView cv_ppmp = findViewById(R.id.ppmp); // creating a CardView and assigning a value.
        CardView cv_app = findViewById(R.id.app); // creating a CardView and assigning a value.
        CardView cv_budget = findViewById(R.id.budget); // creating a CardView and assigning a value.

        cv_ppmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
                startActivity(new Intent(ProfileActivity.this, PpmpActivity.class));
            }
        });
        cv_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
                startActivity(new Intent(ProfileActivity.this, AppActivity.class));
            }
        });
        cv_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
                startActivity(new Intent(ProfileActivity.this, CourseBudget.class));
            }
        });

    }
}
