package com.example.tuptr.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tuptr.Adapter.PpmpAdapter;
import com.example.tuptr.Model.ppmps;
import com.example.tuptr.Manager.HttpServicesClass;
import com.example.tuptr.Manager.SessionManager;
import com.example.tuptr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.tuptr.Manager.URL_Routes.PPMP_VIEW;
import static com.example.tuptr.Manager.URL_Routes.PPMP_VIEWS;

public class PpmpActivity extends Activity {
    ListView ppmpListView;
    ProgressBar progressBarppmp;
    String ServerURL, ServerURL1 = PPMP_VIEWS;
    PpmpAdapter adapter;
    List<ppmps> ppmpsList;
    SwipeRefreshLayout swiper;
    String getId, roleId;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppmp);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        ppmpListView = findViewById(R.id.listview1);
        progressBarppmp = findViewById(R.id.progressBar);
        swiper = findViewById(R.id.swiper);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.C_ID);
        roleId = user.get(sessionManager.R_ID);

        ServerURL = PPMP_VIEW+"?getId="+getId;



        new PpmpActivity.GetHttpResponse(PpmpActivity.this).execute();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PpmpActivity.this, MainActivity.class);
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
                ppmpsList.clear();
                new PpmpActivity.GetHttpResponse(PpmpActivity.this).execute();

                adapter.notifyDataSetChanged();

                swiper.setRefreshing(false);
            }
        }, 3000);
    }

    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
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
            if (roleId == "3") {
                HttpServicesClass httpServiceObject = new HttpServicesClass(ServerURL);
                try {
                    httpServiceObject.ExecutePostRequest();

                    if (httpServiceObject.getResponseCode() == 200) {
                        ResultHolder = httpServiceObject.getResponse();

                        if (ResultHolder != null) {
                            JSONObject jsonObject;
                            try {

                                jsonObject = new JSONObject(ResultHolder);
                                JSONArray jsonArray = jsonObject.getJSONArray("ppmp");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jOBJ = jsonArray.getJSONObject(i);

                                    JSONArray jArray = jOBJ.getJSONArray("items");

                                    ppmps ppmps;
                                    ppmpsList = new ArrayList<>();

                                    for (int j = 0; j < jArray.length(); j++) {
                                        ppmps = new ppmps();
                                        JSONObject jOBJNEW = jArray.getJSONObject(j);

                                        ppmps.ppmplists = "ITEM no: " +
                                                jOBJNEW.getString("id") + "\nCategory " +
                                                jOBJNEW.getString("item_cat") + "\nDescription: " +
                                                jOBJNEW.getString("item_desc") + "\nCost: " +
                                                jOBJNEW.getString("item_cost") + "\nStatus " +
                                                jOBJNEW.getString("status") + "\nReviewed by: " +
                                                jOBJNEW.getString("approver1");
//                                            jOBJNEW.getString("approver2") + "\nApproved by: " +
//                                            jOBJNEW.getString("approver3");

                                        ppmpsList.add(ppmps);
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
            }else{
                HttpServicesClass httpServiceObject = new HttpServicesClass(ServerURL1);
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

                                ppmps ppmps;

                                ppmpsList = new ArrayList<>();

                                for(int i=0; i<jsonArray.length(); i++)
                                {
                                    ppmps = new ppmps();

                                    jsonObject = jsonArray.getJSONObject(i);

                                    ppmps.ppmplists = "PPMP no: " +
                                            jsonObject.getString("id")+ "\nCourse Name: " +
                                            jsonObject.getString("Courses")+ "\nStatus: " +
                                            jsonObject.getString("status")+ "\nPrepare by: " +
                                            jsonObject.getString("PreparedBy");

                                    ppmpsList.add(ppmps);
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
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            progressBarppmp.setVisibility(View.GONE);

            ppmpListView.setVisibility(View.VISIBLE);

            if(ppmpsList != null)
            {
                adapter = new PpmpAdapter(context, ppmpsList, swiper);

                ppmpListView.setAdapter(adapter);
//                ppmpListView.setOnItemClickListener(new onItemClickListener() {
//                    @Override
//                    public void OnItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                        // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
////                startActivity(new Intent(PpmpActivity.this, SignatureActivity.class));
//                        String itemValue = (String)
//                                ppmpListView.getItemAtPosition(position);
//
//                        Toast.makeText(getApplicationContext(),
//                                itemValue, Toast.LENGTH_LONG).show();
//                    }
//                });
            }
        }
    }
}

