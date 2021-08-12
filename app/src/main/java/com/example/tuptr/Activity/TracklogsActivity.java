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

import com.example.tuptr.Adapter.ProgressAdapter;
import com.example.tuptr.Manager.HttpServicesClass;
import com.example.tuptr.Manager.SessionManager;
import com.example.tuptr.Model.progress;
import com.example.tuptr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.tuptr.Manager.URL_Routes.PPMP_VIEWS;
import static com.example.tuptr.Manager.URL_Routes.TRACKER;

public class TracklogsActivity extends AppCompatActivity {
        ListView progListView;
        ProgressBar progressBarprog;
        String ServerURL, ServerURL1 = PPMP_VIEWS;
        ProgressAdapter adapter;
        List<progress> progList;
        SwipeRefreshLayout swiper;
        String getId, roleId;
        SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracklogs);

            sessionManager = new SessionManager(this);
            sessionManager.checkLogin();

            progListView = findViewById(R.id.listview1);
            progressBarprog = findViewById(R.id.progressBar);
            swiper = findViewById(R.id.swiper);

            HashMap<String, String> user = sessionManager.getUserDetail();
            getId = user.get(sessionManager.C_ID);
            roleId = user.get(sessionManager.ID);

        Toast.makeText(TracklogsActivity.this,
                "Success Login. \nYour Userid:"
                        +roleId+"\nYour CourseID: "
                        +getId, Toast.LENGTH_SHORT)
                .show();

            ServerURL = TRACKER+"?getId="+roleId;

        new TracklogsActivity.GetHttpResponse(TracklogsActivity.this).execute();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TracklogsActivity.this, MainActivity.class);
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
                    progList.clear();
                    new TracklogsActivity.GetHttpResponse(TracklogsActivity.this).execute();

                    adapter.notifyDataSetChanged();

                    swiper.setRefreshing(false);
                }
            }, 3000);
        }

        private class GetHttpResponse extends AsyncTask<Void, Void, Void> {
            public Context context;
            String ResultHolder;

            public GetHttpResponse(Context context) {
                this.context = context;
            }

            @SuppressWarnings("deprecation")
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
                                JSONArray jsonArray = jsonObject.getJSONArray("trace");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jOBJ = jsonArray.getJSONObject(i);

                                    JSONArray jArray = jOBJ.getJSONArray("progresss");

                                    progress progress;
                                    progList = new ArrayList<>();

                                    for (int j = 0; j < jArray.length(); j++) {
                                        progress = new progress();
                                        JSONObject jOBJNEW = jArray.getJSONObject(j);

                                        progress.proglists = "Request no: " +
                                                jOBJNEW.getString("request_id") + "\nStatus " +
                                                jOBJNEW.getString("status") + "\nLocation: " +
                                                jOBJNEW.getString("office") + "\nTime: " +
                                                jOBJNEW.getString("created_at");
                                        progList.add(progress);
                                    }
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Toast.makeText(context, httpServiceObject.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
//             TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                progressBarprog.setVisibility(View.GONE);

                progListView.setVisibility(View.VISIBLE);

                if (progList != null) {
                    adapter = new ProgressAdapter(context, progList, swiper);

                    progListView.setAdapter(adapter);
                }
            }
        }
}
