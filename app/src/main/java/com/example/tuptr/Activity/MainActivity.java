package com.example.tuptr.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuptr.Manager.SessionManager;
import com.example.tuptr.R;

import java.util.HashMap;

//import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    SessionManager sessionManager;
    String getId, roleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //navigation drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //session
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.C_ID);
        roleId = user.get(sessionManager.R_ID);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        if (savedInstanceState == null) {
//            navigationView.setCheckedItem(R.id.nav_home);
//
//            first_name.setText(getname);
//            email.setText(getuname);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            //sessionManager.logout();
            openDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id) {
            case R.id.nav_home:
                Intent k= new Intent(MainActivity.this, HomeActivity.class);
                startActivity(k);
                //startActivity(new Intent(MainActivity.this, HomeActivity.class));
                break;
            case R.id.nav_profile:
               //startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                Intent a= new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(a);
                break;
            case R.id.nav_request:
                //startActivity(new Intent(MainActivity.this, RequestActivity.class));
                Intent r= new Intent(MainActivity.this, RequestActivity.class);
                startActivity(r);
                break;
            case R.id.nav_budget:
                //startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                if (roleId == "8") {
                    Intent m = new Intent(MainActivity.this, BudgetActivity.class);
                    startActivity(m);
                }else if (roleId == "3") {
                    Intent l = new Intent(MainActivity.this, CourseBudget.class);
                    startActivity(l);
                }else{
                    Toast.makeText(MainActivity.this,
                            "THERE ARE NO BUDGETS TO DISPLAY", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.nav_sign:
                //startActivity(new Intent(MainActivity.this, SignatureActivity.class));
                Intent e= new Intent(MainActivity.this, SignatureActivity.class);
                startActivity(e);
                break;
            case R.id.nav_track:
                //startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                Intent c = new Intent(MainActivity.this, TracklogsActivity.class);
                    startActivity(c);
//                if (roleId == "3") {
//                    Intent c = new Intent(MainActivity.this, TracklogsActivity.class);
//                    startActivity(c);
//                }else{
//                    Toast.makeText(MainActivity.this,
//                            "THERE ARE NO TRACKER TO DISPLAY", Toast.LENGTH_SHORT)
//                            .show();
//                }
                break;
            case R.id.nav_report:
                //startActivity(new Intent(MainActivity.this, ReportActivity.class));
                Intent n= new Intent(MainActivity.this, ReportActivity.class);
                startActivity(n);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            openDialog();
        }
//        openDialog();
    }

    private void openDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Custom Title
        TextView title = new TextView(this);
        // Title Properties
        title.setText("EXIT");
        title.setPadding(10, 10, 10, 10);   // Set Position
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);

        // Set Message
        TextView msg = new TextView(this);
        // Message Properties
        msg.setText("Are you sure you want to Log-out?");
        msg.setGravity(Gravity.CENTER_HORIZONTAL);
        msg.setTextColor(Color.BLACK);
        alertDialog.setView(msg);

        // Set Button
        // you can more buttons
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
                sessionManager.logout();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
            }
        });

        new Dialog(getApplicationContext());
        alertDialog.show();

        // Set Properties for OK Button
        final Button okBT = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams neutralBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        neutralBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        okBT.setPadding(50, 10, 10, 10);   // Set Position
        okBT.setTextColor(Color.BLUE);
        okBT.setLayoutParams(neutralBtnLP);

        final Button cancelBT = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        negBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        cancelBT.setTextColor(Color.RED);
        cancelBT.setLayoutParams(negBtnLP);
    }
}


