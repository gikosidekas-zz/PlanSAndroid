package com.example.georgios.plans;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.georgios.plans.Custom.CustomAdapterPlan;
import com.example.georgios.plans.api.PlanSApiAdapter;
import com.example.georgios.plans.model.GlobalClass;
import com.example.georgios.plans.model.PlanEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultActivity extends AppCompatActivity implements Callback<List<PlanEntity>> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        //Back Arrow in actionBar
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            setTitle("Planes Suscritos");
        }

        callYourCreatedPlansApi();
    }

    //For the arrow back button to function.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void callYourCreatedPlansApi(){

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        Call<List<PlanEntity>> call = PlanSApiAdapter.getApiService().searchPlan(globalVariable.getPlan().getNombre(),globalVariable.getUser().getIdUsuario());
        call.enqueue(this);

    }

    @Override
    public void onResponse(Call<List<PlanEntity>> call, Response<List<PlanEntity>> response) {

        //conversion lista a array
        final List<PlanEntity> lpe = response.body();
        PlanEntity[] lpea= new PlanEntity[lpe.size()];
        lpea = lpe.toArray(lpea);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        ListAdapter adapt = new CustomAdapterPlan(this,lpea);
        ListView listview = (ListView) findViewById(R.id.listViewRecommended);
        listview.setAdapter(adapt);

        listview.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        globalVariable.setPlan(lpe.get(i));

                        Intent intent = new Intent(getApplicationContext(),PlanSubscribeActivity.class);
                        finish();
                        startActivity(intent);

                    }
                }

        );

    }

    @Override
    public void onFailure(Call<List<PlanEntity>> call, Throwable t) {
        t.printStackTrace();
    }
}
