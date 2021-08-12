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

import com.example.tuptr.Adapter.DepartmentAdapter;
import com.example.tuptr.Model.departments;
import com.example.tuptr.Manager.HttpServicesClass;
import com.example.tuptr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.tuptr.Manager.URL_Routes.DEPARTMENT;

public class RequestActivity extends AppCompatActivity {
    ListView depListView;
    ProgressBar progressBardep;
    String ServerURL = DEPARTMENT;
    DepartmentAdapter adapter;
    List<departments> depList;
    SwipeRefreshLayout swiper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        depListView = findViewById(R.id.listview1);
        progressBardep = findViewById(R.id.progressBar);
        swiper = findViewById(R.id.swiper);

        new RequestActivity.GetHttpResponse(RequestActivity.this).execute();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestActivity.this, MainActivity.class);
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
                depList.clear();
                new RequestActivity.GetHttpResponse(RequestActivity.this).execute();

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
            try
            {
                httpServiceObject.ExecutePostRequest();

                if(httpServiceObject.getResponseCode() == 200)
                {
                    ResultHolder = httpServiceObject.getResponse();

                    if(ResultHolder != null)
                    {
                        JSONArray jsonArray = null;

                        try {
                            jsonArray = new JSONArray(ResultHolder);

                            JSONObject jsonObject;

                            departments departments;

                            depList = new ArrayList<>();

                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                departments = new departments();

                                jsonObject = jsonArray.getJSONObject(i);

                                departments.deptName = "Item: " +
                                        jsonObject.getString("id") + "                            " +
                                        jsonObject.getString("department_name");

                                depList.add(departments);
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
            progressBardep.setVisibility(View.GONE);

            depListView .setVisibility(View.VISIBLE);

            if(depList != null)
            {
                adapter = new DepartmentAdapter(context, depList, swiper);

                depListView .setAdapter(adapter);
            }
        }
    }
}

