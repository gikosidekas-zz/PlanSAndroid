package com.example.georgios.plans;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.georgios.plans.api.PlanSApiAdapter;
import com.example.georgios.plans.model.GlobalClass;
import com.example.georgios.plans.model.PlanEntity;
import com.example.georgios.plans.Custom.CustomAdapterPlan;
import com.example.georgios.plans.model.UsuarioEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Callback<List<PlanEntity>> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        globalVariable.setUser(new UsuarioEntity());
    }

    @Override
    protected void onResume() {
        super.onResume();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        if(globalVariable.getUser().getIdUsuario()!=0){
            callRecomendPlansApi(globalVariable.getUser());
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void callRecomendPlansApi(UsuarioEntity ue){

        Call<List<PlanEntity>> call = PlanSApiAdapter.getApiService().getRecomendedPlans(ue.getIdUsuario());
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

                        Intent intent = new Intent(getApplicationContext(),PlanSubscribe.class);
                        startActivity(intent);

                    }
                }

        );

    }

    @Override
    public void onFailure(Call<List<PlanEntity>> call, Throwable t) {
        t.printStackTrace();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_create_plan) {
            // Handle the camera action
        } else if (id == R.id.nav_my_plans) {

        } else if (id == R.id.nav_subscribed_plans) {

        } else if (id == R.id.nav_recommended_plans) {

        } else if (id == R.id.nav_edit_profile) {

        } else if (id == R.id.nav_log_out) {

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            globalVariable.setUser(new UsuarioEntity());

            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
