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

import com.example.tuptr.Adapter.BudgetAdapter;
import com.example.tuptr.Manager.HttpServicesClass;
import com.example.tuptr.Manager.SessionManager;
import com.example.tuptr.Model.budgets;
import com.example.tuptr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.tuptr.Manager.URL_Routes.BUDGETS;

public class BudgetActivity extends AppCompatActivity {
    ListView budgetListView;
    ProgressBar progressBarbudget;
    String ServerURL = BUDGETS;
    BudgetAdapter adapter;
    List<budgets> budgetList;
    SwipeRefreshLayout swiper;
//    String getId, roleId;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        budgetListView = findViewById(R.id.listview1);
        progressBarbudget = findViewById(R.id.progressBar);
        swiper = findViewById(R.id.swiper);


        new BudgetActivity.GetHttpResponse(BudgetActivity.this).execute();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BudgetActivity.this, MainActivity.class);
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
                budgetList.clear();
                new BudgetActivity.GetHttpResponse(BudgetActivity.this).execute();

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
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(ResultHolder);

                            JSONArray jsonArray;
                            jsonArray = jsonObject.getJSONArray("budget");

                            budgets budgets;
                            budgetList = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {

                                budgets = new budgets();

                                jsonObject = jsonArray.getJSONObject(i);

                                budgets.budgetName = "Course Name: " +
                                        jsonObject.getString("course_name")+ "\nSupplies Budget: " +
                                        jsonObject.getString("SuppliesBudget")+ "\nSupplemental Budget: " +
                                        jsonObject.getString("SupplementalBudget")+ "\nEquipment Budget: " +
                                        jsonObject.getString("EquipmentBudget");
                                budgetList.add(budgets);
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
            progressBarbudget.setVisibility(View.GONE);

            budgetListView .setVisibility(View.VISIBLE);

            if(budgetList != null)
            {
                adapter = new BudgetAdapter(context, budgetList, swiper);

                budgetListView .setAdapter(adapter);
            }
        }
    }
}
