package com.example.tuptr.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import static com.example.tuptr.Manager.URL_Routes.LOGIN;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button btn_login;
//  private TextView link_register;
    private ProgressBar loading;
    //private static String URL_LOGIN = "http://192.168.43.50/api/login/";
    String URL_LOGIN = LOGIN;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        loading = findViewById(R.id.loading);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        //link_register = findViewById(R.id.link_register);
        loading.setVisibility(View.GONE);
        btn_login.setVisibility(View.VISIBLE);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email.getText().toString().trim();
                String mPass = password.getText().toString().trim();


                if (!mEmail.isEmpty() || !mPass.isEmpty()) {
                    login(mEmail,mPass);
                } else {
                    email.setError("Please Insert Email Address");
                    password.setError("Please Insert Password");
                }
            }
        });
//        link_register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//            }
//        });
    }

    private void login(final String email, final String password) {
        loading.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    String email = object.getString("username").trim();
                                    String first_name = object.getString("first_name").trim();
                                    String last_name = object.getString("last_name").trim();
                                    String id = object.getString("id").trim();
                                    String course_id = object.getString("course_id").trim();
                                    String role_id = object.getString("role_id").trim();

                                    sessionManager.createSession(first_name,last_name , email, id, course_id, role_id);

                                    Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("first_name", first_name );
                                    intent.putExtra("last_name", last_name );
                                    intent.putExtra("username", email);
                                    intent.putExtra("course_id", course_id);
                                    intent.putExtra("role_id", role_id);
                                    startActivity(intent);

                                    Toast.makeText(LoginActivity.this,
                                            "Success Login. \nYour Name:"
                                                    +first_name+" "+last_name+"\nYour Username: "
                                                    +email+"\n Course id: "+course_id+"\n Role id: "+role_id, Toast.LENGTH_SHORT)
                                            .show();
                                    loading.setVisibility(View.GONE);



                                }
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this,
                                    "Error"+e.toString(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this,
                                "Error"+error.toString(), Toast.LENGTH_SHORT)
                                .show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", email);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}