package com.example.georgios.plans;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.georgios.plans.api.PlanSApiAdapter;
import com.example.georgios.plans.model.GlobalClass;
import com.example.georgios.plans.model.NumberString;
import com.example.georgios.plans.model.PlanEntity;
import com.example.georgios.plans.model.PreferenciaEntity;
import com.example.georgios.plans.model.UsuarioEntity;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.sql.Timestamp;

public class CreatePlanActivity extends AppCompatActivity implements Callback<PlanEntity> {

    private Spinner preferenciaSpinner;
    private AutoCompleteTextView mDescriptionView;
    private AutoCompleteTextView mNameView;
    private AutoCompleteTextView mCostoView;
    private List<NumberString> preferencias = new ArrayList<NumberString>();

    //datetime lines
    private static final String TAG = "Sample";

    private static final String TAG_DATETIME_FRAGMENT_INI = "TAG_DATETIME_FRAGMENT_INI";
    private static final String TAG_DATETIME_FRAGMENT_FIN = "TAG_DATETIME_FRAGMENT_FIN";

    private static final String STATE_INI_TEXTVIEW = "STATE_TEXTVIEW";
    private TextView textViewIni, textViewFin, mUbicacionView;

    private SwitchDateTimeDialogFragment dateTimeFragment;

    private SwitchDateTimeDialogFragment dateTimeFragmentFin;

    private Date fechaini;
    private Date fechafin;

    private final int REQUEST_CODE_PLACEPICKER = 1;

    private List<PreferenciaEntity> lpe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            setTitle("Crear Nuevo Plan");
        }

        textViewIni = (TextView) findViewById(R.id.fecha_ini_text);
        textViewFin = (TextView) findViewById(R.id.fecha_fin_text);
        mUbicacionView=(TextView) findViewById(R.id.ubicacion_text);

        preferenciaSpinner = (Spinner)findViewById(R.id.preferencia);

        mNameView = (AutoCompleteTextView) findViewById(R.id.nombre);
        mDescriptionView = (AutoCompleteTextView) findViewById(R.id.descripcion);

        mCostoView = (AutoCompleteTextView) findViewById(R.id.costo);

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

        //Date time picker INI
        // Construct SwitchDateTimePicker
        dateTimeFragment = (SwitchDateTimeDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT_INI);
        if(dateTimeFragment == null) {
            dateTimeFragment = SwitchDateTimeDialogFragment.newInstance(
                    getString(R.string.label_datetime_dialog),
                    getString(android.R.string.ok),
                    getString(android.R.string.cancel),
                    getString(R.string.clean) // Optional
            );
        }

        // Init format
        final SimpleDateFormat myDateFormat = new SimpleDateFormat("d MMM yyyy HH:mm", java.util.Locale.getDefault());
        // Assign unmodifiable values
        dateTimeFragment.set24HoursMode(false);
        List<String> time = getTimeNowPices();
        dateTimeFragment.setMinimumDateTime(new GregorianCalendar(Integer.parseInt(time.get(0)),Integer.parseInt(time.get(1))-1,Integer.parseInt(time.get(2))).getTime());
        dateTimeFragment.setMaximumDateTime(new GregorianCalendar(Integer.parseInt(time.get(0))+1,Integer.parseInt(time.get(1))-1,Integer.parseInt(time.get(2))).getTime());

        // Define new day and month format
        try {
            dateTimeFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("MMMM dd", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }

        // Set listener for date
        // Or use dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
        dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                Timestamp ts= new Timestamp(date.getTime());
                textViewIni.setText(makeNewDate(ts.toString()));
                fechaini=date;
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Do nothing
            }

            @Override
            public void onNeutralButtonClick(Date date) {
                // Optional if neutral button does'nt exists
                textViewIni.setText("Define fecha de inicio");
            }
        });

        //Date time picker FIN
        // Construct SwitchDateTimePicker
        dateTimeFragmentFin = (SwitchDateTimeDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT_FIN);
        if(dateTimeFragmentFin == null) {
            dateTimeFragmentFin = SwitchDateTimeDialogFragment.newInstance(
                    getString(R.string.label_datetime_dialog),
                    getString(android.R.string.ok),
                    getString(android.R.string.cancel),
                    getString(R.string.clean) // Optional
            );
        }

        // Assign unmodifiable values
        dateTimeFragmentFin.set24HoursMode(false);
        dateTimeFragmentFin.setMinimumDateTime(new GregorianCalendar(Integer.parseInt(time.get(0)),Integer.parseInt(time.get(1))-1,Integer.parseInt(time.get(2))).getTime());
        dateTimeFragmentFin.setMaximumDateTime(new GregorianCalendar(Integer.parseInt(time.get(0))+1,Integer.parseInt(time.get(1))-1,Integer.parseInt(time.get(2))).getTime());

        // Define new day and month format
        try {
            dateTimeFragmentFin.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("MMMM dd", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }

        // Set listener for date
        // Or use dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
        dateTimeFragmentFin.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                Timestamp ts= new Timestamp(date.getTime());
                textViewFin.setText(makeNewDate(ts.toString()));
                fechafin=date;
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Do nothing
            }

            @Override
            public void onNeutralButtonClick(Date date) {
                // Optional if neutral button does'nt exists
                textViewFin.setText("Define fecha de finalización");
            }
        });

        LinearLayout fechaini = (LinearLayout)findViewById(R.id.fecha_ini_layout);
        fechaini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeFragment.startAtCalendarView();
                List<String> strtime=getTimeNowPices();
                dateTimeFragment.setDefaultDateTime(new GregorianCalendar(Integer.parseInt(strtime.get(0)),Integer.parseInt(strtime.get(1))-1,Integer.parseInt(strtime.get(2)),Integer.parseInt(strtime.get(3)),Integer.parseInt(strtime.get(4))).getTime());
                dateTimeFragment.show(getSupportFragmentManager(), TAG_DATETIME_FRAGMENT_INI);
            }
        });

        LinearLayout fechafin = (LinearLayout)findViewById(R.id.fecha_fin_layout);
        fechafin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeFragmentFin.startAtCalendarView();
                List<String> strtime=getTimeNowPices();
                dateTimeFragmentFin.setDefaultDateTime(new GregorianCalendar(Integer.parseInt(strtime.get(0)),Integer.parseInt(strtime.get(1))-1,Integer.parseInt(strtime.get(2)),Integer.parseInt(strtime.get(3)),Integer.parseInt(strtime.get(4))).getTime());
                dateTimeFragmentFin.show(getSupportFragmentManager(), TAG_DATETIME_FRAGMENT_FIN);
            }
        });

        LinearLayout ubicacion = (LinearLayout)findViewById(R.id.ubicacion_layout);
        ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlacePickerActivity();
            }
        });
    }

    private void startPlacePickerActivity() {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        // this would only work if you have your Google Places API working

        try {
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent, REQUEST_CODE_PLACEPICKER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displaySelectedPlaceFromPlacePicker(Intent data) {
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        Place placeSelected = PlacePicker.getPlace(data, this);

        String latlon = placeSelected.getAddress().toString()+"|"+placeSelected.getLatLng().latitude+"|"+placeSelected.getLatLng().longitude;

        globalVariable.getPlan().setUbicacion(latlon);

        TextView enterCurrentLocation = (TextView) findViewById(R.id.ubicacion_text);
        enterCurrentLocation.setText(placeSelected.getAddress().toString());
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PLACEPICKER && resultCode == RESULT_OK) {
            displaySelectedPlaceFromPlacePicker(data);
        }
    }

    //For the arrow back button to function.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the current textView
        savedInstanceState.putCharSequence(STATE_INI_TEXTVIEW, textViewIni.getText());
        super.onSaveInstanceState(savedInstanceState);
    }

    private void attemptRegister() {

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        PlanEntity plan = new PlanEntity();

        String datePost;

        mNameView.setError(null);
        mDescriptionView.setError(null);
        mCostoView.setError(null);
        textViewIni.setError(null);
        textViewFin.setError(null);
        mUbicacionView.setError(null);


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(mNameView.getText().toString())) {
            mNameView.setError("El nombre no puede estar vacio");
            focusView = mNameView;
            cancel = true;
        } else if(TextUtils.isEmpty(mDescriptionView.getText().toString())){
            mDescriptionView.setError("La descripcion no puede estar vacia");
            focusView = mDescriptionView;
            cancel = true;
        }else if(TextUtils.isEmpty(mCostoView.getText().toString())){
            mCostoView.setError("El costo no puede ser vacio");
            focusView = mCostoView;
            cancel = true;
        }else if(textViewIni.getText().equals("Define fecha de inicio") || TextUtils.isEmpty(textViewIni.getText())){
            textViewIni.setError("Porfavor define la fecha de inicio");
            focusView = textViewIni;
            cancel = true;
        }else if(textViewFin.getText().equals("Define fecha de finalización") || TextUtils.isEmpty(textViewFin.getText())){
            textViewFin.setError("Porfavor define la fecha de Finalización");
            focusView = textViewFin;
            cancel = true;
        }else if(mUbicacionView.getText().equals("Define ubicación de tu plan")){
            mUbicacionView.setError("Porfavor define la ubicación de tu plan");
            focusView = mUbicacionView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }else{
            plan.setCostoPromedio(Integer.parseInt(mCostoView.getText().toString()));
            plan.setCreadorPlan((int)globalVariable.getUser().getIdUsuario());
            plan.setDescripcion(mDescriptionView.getText().toString());
            plan.setDetallePreferencia(getPreferenciaSpiner(preferenciaSpinner.getSelectedItem().toString()));

            Timestamp ts = new Timestamp(fechafin.getTime());
            datePost = changeFormat(ts.toString());
            plan.setFechaFinal(datePost);


            ts=new Timestamp(fechaini.getTime());
            datePost = changeFormat(ts.toString());
            plan.setFechaInicio(datePost);

            plan.setNombre(mNameView.getText().toString());
            plan.setUbicacion(globalVariable.getPlan().getUbicacion());

            callRegisterApi(plan);
            showProgress(true);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
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

        showProgress(false);

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
        showProgress(false);
        Toast.makeText(getApplicationContext(), "Ha ocurrido un error inesperado en el servidor",Toast.LENGTH_LONG).show();
        t.printStackTrace();
    }

    private void callDisplayPreferencesApi(){

        Call<List<PreferenciaEntity>> call = PlanSApiAdapter.getApiService().getPreferences();
        call.enqueue(new PreferencesCallBack());

    }

    public long getPreferenciaSpiner(String str){

        long pos=-1;
        for(int i=0; i<lpe.size();i++){
            if(str.compareTo(lpe.get(i).getNombre())==0){
                pos=lpe.get(i).getIdPreferencia();
            }
        }
        return pos;

    }

    class PreferencesCallBack implements  Callback<List<PreferenciaEntity>> {


        @Override
        public void onResponse(Call<List<PreferenciaEntity>> call, Response<List<PreferenciaEntity>> response) {

            lpe = response.body();

            List<String> str = new ArrayList<String>();

            for(int i=0;i<lpe.size();i++){
                str.add(lpe.get(i).getNombre());
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.preference_category, str);
            preferenciaSpinner.setAdapter(dataAdapter);
        }

        @Override
        public void onFailure(Call<List<PreferenciaEntity>> call, Throwable t) {
            t.printStackTrace();
        }
    }

    private List<String> getTimeNowPices(){
        List<String> rtnStr = new ArrayList<String>();
        String[] dateS;
        String[] time;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());

        dateS = date.split("T");
        time= dateS[1].split(":");
        dateS=dateS[0].split("-");

        for(int i=0; i<dateS.length;i++){
            rtnStr.add(dateS[i]);
        }
        for(int i=0; i<time.length;i++){
            rtnStr.add(time[i]);
        }

        return rtnStr;
    }

    public String changeFormat(String date){
        String[] parts = date.split(" ");
        String newFormat = parts[0]+"T"+parts[1];
        return newFormat;
    }

    public String makeNewDate(String str){
        String date;
        String time;
        String[] parts;

        parts=str.split(" ");

        date = parts[0];
        time = parts[1];

        parts= date.split("-");
        date=parts[2]+"/"+parts[1]+"/"+parts[0];

        parts= time.split(":");
        time = parts[0]+":"+parts[1];

        time = convertTimeAMPM(time);

        str=date+" "+time;
        return str;
    }

    public String convertTimeAMPM(String hour){
        String[] parts;
        int hora;

        parts=hour.split(":");

        hora = Integer.parseInt(parts[0]);

        if(hora<=11 && hora>0){
            hour = Integer.toString(hora)+":"+parts[1]+" AM";
        }
        else{
            if(hora==12){
                hour = parts[0]+":"+parts[1]+" PM";
            }
            else if(hora == 0){
                hour = 12+":"+parts[1]+" AM";
            }
            else{
                hora = hora - 12;
                hour = Integer.toString(hora)+":"+parts[1]+" PM";
            }
        }
        return hour;
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

        mLoginFormView = findViewById(R.id.login_form_createplan);
        mProgressView = findViewById(R.id.login_progress_createplan);

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
