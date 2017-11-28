package com.example.georgios.plans;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.georgios.plans.model.PlanEntity;

public class SearchResultsActivity extends AppCompatActivity {

    private String searchString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results2);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            this.searchString = intent.getStringExtra(SearchManager.QUERY);

            callSearchApi();

        }
    }

    private void callSearchApi(){
        System.out.println(this.searchString);
    }
}
