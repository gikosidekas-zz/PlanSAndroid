package com.example.georgios.plans;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.georgios.plans.api.PlanSApiAdapter;
import com.example.georgios.plans.model.GlobalClass;
import com.example.georgios.plans.model.NumberString;
import com.example.georgios.plans.model.PlanEntity;
import com.example.georgios.plans.model.PreferenciaEntity;
import com.example.georgios.plans.model.UsuarioEntity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePlanActivity extends AppCompatActivity implements Callback<PlanEntity> {

    private Spinner preferenciaSpinner;
    private AutoCompleteTextView mDescriptionView;
    private AutoCompleteTextView mUbicacionView;
    private AutoCompleteTextView mFechainiView;
    private AutoCompleteTextView mFechafinView;
    private AutoCompleteTextView mNameView;
    private AutoCompleteTextView mCostoView;
    private View mProgressView;
    private View mLoginFormView;
    private List<NumberString> preferencias = new ArrayList<NumberString>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            setTitle("Crear Nuevo Plan");
        }

        preferenciaSpinner = (Spinner)findViewById(R.id.preferencia);

        mNameView = (AutoCompleteTextView) findViewById(R.id.nombre);
        mDescriptionView = (AutoCompleteTextView) findViewById(R.id.descripcion);
        mUbicacionView = (AutoCompleteTextView) findViewById(R.id.ubicacion);
        mFechainiView = (AutoCompleteTextView) findViewById(R.id.fechaini);
        mFechafinView = (AutoCompleteTextView) findViewById(R.id.fechafin);
        mCostoView = (AutoCompleteTextView) findViewById(R.id.secondname);



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tiposid, android.R.layout.simple_spinner_item);
        preferenciaSpinner.setAdapter(adapter);

        Button mEmailSignInButton = (Button) findViewById(R.id.register_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        callDisplayPreferencesApi();
    }

    //For the arrow back button to function.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void attemptRegister() {

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        PlanEntity plan = new PlanEntity();

        mNameView.setError(null);
        mDescriptionView.setError(null);
        mUbicacionView.setError(null);
        mFechainiView.setError(null);
        mFechafinView.setError(null);
        mCostoView.setError(null);


        // Store values at the time of the login attempt.
        plan.setCostoPromedio(Integer.parseInt(mCostoView.getText().toString()));
        plan.setCreadorPlan((int)globalVariable.getUser().getIdUsuario());
        plan.setDescripcion(mDescriptionView.getText().toString());
        //plan.setDetallePreferencia(preferenciaSpinner.getSelectedItem().toString());
        //plan.setFechaFinal(mFechafinView.getText().toString());
       //plan.setFechaInicio(mFechainiView.getText().toString());
        plan.setNombre(mNameView.getText().toString());
        plan.setUbicacion(mUbicacionView.getText().toString());

        boolean cancel = false;
        View focusView = null;

//        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(ur.getContrasena()) && !isPasswordValid(ur.getContrasena())) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
//            cancel = true;
//        } else if(ur.getContrasena().compareTo(confirmPass) != 0){
//            mConfirmPassView.setError(getString(R.string.error_confirm_password));
//            focusView = mConfirmPassView;
//            cancel = true;
//        }
//
//        // Check for a valid email address.
//        if (TextUtils.isEmpty(ur.getUsuario())) {
//            mEmailView.setError(getString(R.string.error_field_required));
//            focusView = mEmailView;
//            cancel = true;
//        } else if (!isEmailValid(ur.getEmail())) {
//            mEmailView.setError(getString(R.string.error_invalid_email));
//            focusView = mEmailView;
//            cancel = true;
//        }
//
//        if (TextUtils.isEmpty(ur.getUsuario())) {
//            mUsernameView.setError(getString(R.string.error_field_required));
//            focusView = mUsernameView;
//            cancel = true;
//        }
//
//        if (TextUtils.isEmpty(ur.getNombres())) {
//            mNameView.setError(getString(R.string.error_field_required));
//            focusView = mNameView;
//            cancel = true;
//        }
//
//        if (TextUtils.isEmpty(ur.getApellidos())) {
//            mSecondnameView.setError(getString(R.string.error_field_required));
//            focusView = mSecondnameView;
//            cancel = true;
//        }
//
//        if (TextUtils.isEmpty(ur.getNumeroId())) {
//            mNumberidView.setError(getString(R.string.error_field_required));
//            focusView = mNumberidView;
//            cancel = true;
//        }
//
//        if (cancel) {
//            // There was an error; don't attempt login and focus the first
//            // form field with an error.
//            focusView.requestFocus();
//        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            callRegisterApi(plan);
            //mAuthTask = new UserLoginTask(email, password);



            //mAuthTask.execute((Void) null);
        //}
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        finish();
        startActivity(i);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 1;
    }

    private void callRegisterApi(PlanEntity ur){

        Call<PlanEntity> call = PlanSApiAdapter.getApiService().createPlan(ur);
        call.enqueue(this);

    }

    @Override
    public void onResponse(Call<PlanEntity> call, Response<PlanEntity> response) {

        if(response.isSuccessful()){
            PlanEntity plan = response.body();
            finish();
        }
        else{
            System.out.println(response.errorBody());
            Toast.makeText(getApplicationContext(), "Error al registrar el usuario",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<PlanEntity> call, Throwable t) {
        t.printStackTrace();
    }

    private void callDisplayPreferencesApi(){

        Call<List<PreferenciaEntity>> call = PlanSApiAdapter.getApiService().getPreferences();
        call.enqueue(new PreferencesCallBack());

    }

    public void addToList(long num, String str){
        NumberString ns = new NumberString();
        ns.setNumber(num);
        ns.setStr(str);
        this.preferencias.add(ns);
    }

    public void deleteList(long num, String str){
        NumberString ns = new NumberString();
        ns.setNumber(num);
        ns.setStr(str);
        this.preferencias.remove(ns);
    }

    class PreferencesCallBack implements  Callback<List<PreferenciaEntity>>{


        @Override
        public void onResponse(Call<List<PreferenciaEntity>> call, Response<List<PreferenciaEntity>> response) {
            if(response.isSuccessful()){
                final List<PreferenciaEntity> pe = response.body();
                ViewGroup mRegisterFormView = (ViewGroup) findViewById(R.id.checkboxboxes);
                for (int i = 0; i < pe.size(); i++) {

                    CheckBox checkBox = new CheckBox(getApplicationContext());
                    checkBox.setText(pe.get(i).getNombre());
                    final int finalI = i;
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b){
                                addToList(pe.get(finalI).getIdPreferencia(),pe.get(finalI).getNombre());
                            }
                            else{
                                deleteList(pe.get(finalI).getIdPreferencia(),pe.get(finalI).getNombre());
                            }
                        }
                    });

                    mRegisterFormView.addView(checkBox);
                }



            }
            else{
                System.out.println(response.errorBody());
            }
        }

        @Override
        public void onFailure(Call<List<PreferenciaEntity>> call, Throwable t) {
            t.printStackTrace();
        }
    }
}
