package com.example.tuptr.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tuptr.Manager.SessionManager;
import com.example.tuptr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.tuptr.Manager.URL_Routes.PROFILE_EDIT;
import static com.example.tuptr.Manager.URL_Routes.PROFILE_READ;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = ProfileActivity.class.getSimpleName(); //getting the info
    private TextView tv_first_name, tv_last_name, tv_email,tv_pass;
    private Button btn_logout;
    private Menu action;
    SessionManager sessionManager;
    String getId;
    String URL_READ = PROFILE_READ;
    String URL_EDIT = PROFILE_EDIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        tv_first_name = findViewById(R.id.first_name);
        tv_last_name = findViewById(R.id.last_name);
        tv_email = findViewById(R.id.email);
        tv_pass = findViewById(R.id.password);
        btn_logout = findViewById(R.id.btn_logout);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    private void getUserDetail() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response);

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            String success = jsonobject.getString("success");
                            JSONArray jsonArray = jsonobject.getJSONArray("read");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String strF_Name = object.getString("first_name").trim();
                                    String strL_Name = object.getString("last_name").trim();
                                    String strEmail = object.getString("username").trim();
//                                    String strPass = object.getString("password").trim();

                                    tv_first_name.setText(strF_Name);
                                    tv_last_name.setText(strL_Name);
                                    tv_email.setText(strEmail);
//                                    tv_pass.setText(strPass);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(
                                    HomeActivity.this,
                                    "Error Reading Detail" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                HomeActivity.this,
                                "Error Reading Detail" + error.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetail();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_action, menu);

        action = menu;
        action.findItem(R.id.menu_save).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_edit:
                tv_first_name.setFocusableInTouchMode(true);
                tv_last_name.setFocusableInTouchMode(true);
                tv_email.setFocusableInTouchMode(true);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(tv_first_name, InputMethodManager.SHOW_IMPLICIT);
                imm.showSoftInput(tv_last_name, InputMethodManager.SHOW_IMPLICIT);

                action.findItem(R.id.menu_edit).setVisible(false);
                action.findItem(R.id.menu_save).setVisible(true);

                return true;

            case R.id.menu_save:

                SaveEditDetail();

                action.findItem(R.id.menu_edit).setVisible(true);
                action.findItem(R.id.menu_save).setVisible(false);

                tv_first_name.setFocusableInTouchMode(false);
                tv_last_name.setFocusableInTouchMode(false);
                tv_email.setFocusableInTouchMode(false);
                tv_first_name.setFocusable(false);
                tv_last_name.setFocusable(false);
                tv_email.setFocusable(false);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void SaveEditDetail() {
        final String first_name = this.tv_first_name.getText().toString().trim();
        final String last_name = this.tv_last_name.getText().toString().trim();
        final String email = this.tv_email.getText().toString().trim();
        final String id = getId;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            String success = jsonobject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(
                                        HomeActivity.this,
                                        "Success!",
                                        Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(
                                    HomeActivity.this,
                                    "Error" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                HomeActivity.this,
                                "Error" + error.toString(),
                                Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", first_name);
                params.put("last_name", last_name);
                params.put("email", email);
                params.put("id", getId);
                return params;
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void openDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Custom Title
        TextView title = new TextView(this);
        // Title Properties
        title.setText("CHANGE PASSWORD");
        title.setPadding(10, 10, 10, 10);   // Set Position
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);

        // Set Message
        EditText msg = new EditText(this);
        // Message Properties
        msg.setText("Current Password");
        msg.setGravity(Gravity.CENTER_HORIZONTAL);
        msg.setTextColor(Color.BLACK);
        alertDialog.setView(msg);


        // Set Button
        // you can more buttons
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
//                sessionManager.logout();
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
