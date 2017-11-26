package com.example.georgios.plans;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.georgios.plans.model.NumberString;
import com.example.georgios.plans.model.PreferenciaEntity;
import com.example.georgios.plans.model.UsuarioEntity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUserActivity extends AppCompatActivity implements Callback<UsuarioEntity> {

    private Spinner tiposid;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPassView;
    private AutoCompleteTextView mUsernameView;
    private AutoCompleteTextView mNameView;
    private AutoCompleteTextView mSecondnameView;
    private AutoCompleteTextView mNumberidView;
    private View mProgressView;
    private View mLoginFormView;
    private List<NumberString> preferencias = new ArrayList<NumberString>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        tiposid = (Spinner)findViewById(R.id.tipoid);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mConfirmPassView = (EditText) findViewById(R.id.confirm_password);
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);
        mNameView = (AutoCompleteTextView) findViewById(R.id.name);
        mSecondnameView = (AutoCompleteTextView) findViewById(R.id.secondname);
        mNumberidView = (AutoCompleteTextView) findViewById(R.id.numeroid);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tiposid, android.R.layout.simple_spinner_item);
        tiposid.setAdapter(adapter);

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

    private void attemptRegister() {

        UsuarioEntity ur = new UsuarioEntity();

        mEmailView.setError(null);
        mPasswordView.setError(null);
        mConfirmPassView.setError(null);
        mUsernameView.setError(null);
        mNameView.setError(null);
        mSecondnameView.setError(null);
        mNumberidView.setError(null);


        // Store values at the time of the login attempt.
        ur.setEmail(mEmailView.getText().toString());
        ur.setContrasena(mPasswordView.getText().toString());
        String confirmPass = mConfirmPassView.getText().toString();
        ur.setUsuario(mUsernameView.getText().toString());
        ur.setNombres(mNameView.getText().toString());
        ur.setApellidos(mSecondnameView.getText().toString());
        ur.setNumeroId(mNumberidView.getText().toString());
        ur.setTipoId(tiposid.getSelectedItem().toString());

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(ur.getContrasena()) && !isPasswordValid(ur.getContrasena())) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if(ur.getContrasena().compareTo(confirmPass) != 0){
            mConfirmPassView.setError(getString(R.string.error_confirm_password));
            focusView = mConfirmPassView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(ur.getUsuario())) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(ur.getEmail())) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (TextUtils.isEmpty(ur.getUsuario())) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(ur.getNombres())) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(ur.getApellidos())) {
            mSecondnameView.setError(getString(R.string.error_field_required));
            focusView = mSecondnameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(ur.getNumeroId())) {
            mNumberidView.setError(getString(R.string.error_field_required));
            focusView = mNumberidView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            callRegisterApi(ur);
            //mAuthTask = new UserLoginTask(email, password);



            //mAuthTask.execute((Void) null);
        }
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

//    /**
//     * Shows the progress UI and hides the login form.
//     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    private void showProgress(final boolean show) {
//        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
//        // for very easy animations. If available, use these APIs to fade-in
//        // the progress spinner.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });
//
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mProgressView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
//        } else {
//            // The ViewPropertyAnimator APIs are not available, so simply show
//            // and hide the relevant UI components.
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//        }
//    }

    private void callRegisterApi(UsuarioEntity ur){

        Call<UsuarioEntity> call = PlanSApiAdapter.getApiService().registerNewUser(ur);
        call.enqueue(this);

    }

    private void callSetPreferencesApi(List<NumberString> ns){

        Call<NumberString> call = PlanSApiAdapter.getApiService().setUserPreferences(ns);
        call.enqueue(new setPreferencesCallBack());

    }

    @Override
    public void onResponse(Call<UsuarioEntity> call, Response<UsuarioEntity> response) {
        //a@amAuthTask = null;
        //showProgress(false);

        if(response.isSuccessful()){
            UsuarioEntity ur = response.body();
            for(int i=0;i<this.preferencias.size();i++){
                this.preferencias.get(i).setNumber(ur.getIdUsuario());
            }
            callSetPreferencesApi(this.preferencias);
        }
        else{
            System.out.println(response.errorBody());
            Toast.makeText(getApplicationContext(), "Error al registrar el usuario",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<UsuarioEntity> call, Throwable t) {
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

    class setPreferencesCallBack implements  Callback<NumberString>{


        @Override
        public void onResponse(Call<NumberString> call, Response<NumberString> response) {
            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            finish();
            startActivity(i);
        }

        @Override
        public void onFailure(Call<NumberString> call, Throwable t) {
            t.printStackTrace();
        }
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
