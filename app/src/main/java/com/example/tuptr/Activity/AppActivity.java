package com.example.tuptr.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tuptr.Adapter.AppAdapter;
import com.example.tuptr.Manager.HttpServicesClass;
import com.example.tuptr.Manager.SessionManager;
import com.example.tuptr.Model.apps;
import com.example.tuptr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.tuptr.Manager.URL_Routes.APP_VIEW;

public class AppActivity extends AppCompatActivity {
    ListView appListView;
    ProgressBar progressBarapp;
    String ServerURL;
    AppAdapter adapter;
    List<apps> appList;
    SwipeRefreshLayout swiper;
    String getId, roleId;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        appListView = findViewById(R.id.listview1);
        progressBarapp = findViewById(R.id.progressBar);
        swiper = findViewById(R.id.swiper);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.C_ID);
        roleId = user.get(sessionManager.R_ID);

        ServerURL = APP_VIEW+"?getId="+getId;

        new AppActivity.GetHttpResponse(AppActivity.this).execute();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                appList.clear();
                new AppActivity.GetHttpResponse(AppActivity.this).execute();

                adapter.notifyDataSetChanged();

                swiper.setRefreshing(false);
            }
        },3000);
    }

    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;
        String ResultHolder;

        public GetHttpResponse(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpServicesClass httpServiceObject = new HttpServicesClass(ServerURL);
            try {
                httpServiceObject.ExecutePostRequest();

                if (httpServiceObject.getResponseCode() == 200) {
                    ResultHolder = httpServiceObject.getResponse();

                    if (ResultHolder != null) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(ResultHolder);
                            JSONArray jsonArray = jsonObject.getJSONArray("app");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jOBJ = jsonArray.getJSONObject(i);

                                JSONArray jArray = jOBJ.getJSONArray("items");

                                apps apps;
                                appList = new ArrayList<>();

                                for (int j = 0; j < jArray.length(); j++) {
                                    apps = new apps();
                                    JSONObject jOBJNEW = jArray.getJSONObject(j);

                                    apps.applists = "ITEM no: " +
                                            jOBJNEW.getString("id") + "\nPPMP no " +
                                            jOBJNEW.getString("ppmp_id") + "\nDescription: " +
                                            jOBJNEW.getString("item_desc") + "\nCost: " +
                                            jOBJNEW.getString("item_cost") + "\nStatus " +
                                            jOBJNEW.getString("status") + "\nReviewed by: " +
                                            jOBJNEW.getString("approver1");
//                                            jOBJNEW.getString("approver2") + "\nApproved by: " +
//                                            jOBJNEW.getString("approver3");

                                    appList.add(apps);
                                    //                            JSONObject jsonObject;
//
//                            apps apps;
//
//                            appList = new ArrayList<>();
//
//                            for(int i=0; i<jsonArray.length(); i++)
//                            {
//                                apps = new apps();
//
//                                jsonObject = jsonArray.getJSONObject(i);
//
//                                apps.applists = "Item: " +
//                                        jsonObject.getString("id") + "\nCourse " +
//                                        jsonObject.getString("Courses") + "\nStatus " +
//                                        jsonObject.getString("status");
//
//                                appList.add(apps);
                                }
                            }
                        }
                        catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

                else
                {
                    Toast.makeText(context, httpServiceObject.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
//             TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result)

        {
            progressBarapp.setVisibility(View.GONE);

            appListView .setVisibility(View.VISIBLE);

            if(appList != null)
            {
                adapter = new AppAdapter(context, appList, swiper);

                appListView .setAdapter(adapter);
            }
        }


    }
}