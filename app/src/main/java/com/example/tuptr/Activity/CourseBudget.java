package com.example.tuptr.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import static com.example.tuptr.Manager.URL_Routes.BUDGET;

public class CourseBudget extends AppCompatActivity {
    private static final String TAG = CourseBudget.class.getSimpleName(); //getting the info
    private TextView tv_sup, tv_sup1, tv_equip, tv_course;
    SessionManager sessionManager;
    String getId;
    String URL_Server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_budget);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        tv_sup = findViewById(R.id.tv_sup);
        tv_sup1 = findViewById(R.id.tv_sup1);
        tv_equip = findViewById(R.id.tv_equip);
        tv_course = findViewById(R.id.course);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.C_ID);
        URL_Server = BUDGET + "?getId=" + getId;
    }

        private void getUserDetail() {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading..");
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Server,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            Log.i(TAG, response);

                            try {
                                JSONObject jsonobject = new JSONObject(response);
                                String success = jsonobject.getString("success");
                                JSONArray jsonArray = jsonobject.getJSONArray("budget");

                                if (success.equals("1")) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String strS1 = object.getString("SuppliesBudget").trim();
                                        String strS = object.getString("SupplementalBudget").trim();
                                        String strE = object.getString("EquipmentBudget").trim();
                                        String strc = object.getString("course_name").trim();

                                        tv_sup1.setText(strS1);
                                        tv_sup.setText(strS);
                                        tv_equip.setText(strE);
                                        tv_course.setText(strc);

                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Toast.makeText(
                                        CourseBudget.this,
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
                                    CourseBudget.this,
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
    }
