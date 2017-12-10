package com.example.georgios.plans;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        //Back Arrow in actionBar
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            setTitle(globalVariable.getPlan().getNombre());
        }

        callYourCreatedPlansApi();
        showProgress(true);
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
        ListView listview = (ListView) findViewById(R.id.listViewRecommended_search);
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

        showProgress(false);

    }

    @Override
    public void onFailure(Call<List<PlanEntity>> call, Throwable t) {
        showProgress(false);
        Toast.makeText(getApplicationContext(), "Ha ocurrido un error inesperado en el servidor",Toast.LENGTH_LONG).show();
        t.printStackTrace();
    }

    private View mProgressView;
    private View mLoginFormView;
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        mLoginFormView = findViewById(R.id.listViewRecommended_search);
        mProgressView = findViewById(R.id.login_progress_search);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
