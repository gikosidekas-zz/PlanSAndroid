package com.example.georgios.plans;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.georgios.plans.api.PlanSApiAdapter;
import com.example.georgios.plans.model.GlobalClass;
import com.example.georgios.plans.model.NumberString;
import com.example.georgios.plans.model.PreferenciaEntity;
import com.example.georgios.plans.model.UsuarioEntity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends AppCompatActivity implements Callback<UsuarioEntity>{

    private Spinner tiposid;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPassView;
    private AutoCompleteTextView mUsernameView;
    private AutoCompleteTextView mNameView;
    private AutoCompleteTextView mSecondnameView;
    private AutoCompleteTextView mNumberidView;
    private List<NumberString> preferencias = new ArrayList<NumberString>();
    private List<PreferenciaEntity> preferencesUser = new ArrayList<PreferenciaEntity>();

    private Uri imageUri;
    private ImageView mImageView;
    private String encodedImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        //Back Arrow in actionBar
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            setTitle("Edita tu perfil");
        }

        tiposid = (Spinner)findViewById(R.id.tipoid);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mConfirmPassView = (EditText) findViewById(R.id.confirm_password);
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);
        mNameView = (AutoCompleteTextView) findViewById(R.id.name);
        mSecondnameView = (AutoCompleteTextView) findViewById(R.id.secondname);
        mNumberidView = (AutoCompleteTextView) findViewById(R.id.numeroid);
        mImageView = (ImageView) findViewById(R.id.imagen_edituser);

        mEmailView.setText(globalVariable.getUser().getEmail());
        mUsernameView.setText(globalVariable.getUser().getUsuario());
        mNameView.setText(globalVariable.getUser().getNombres());
        mSecondnameView.setText(globalVariable.getUser().getApellidos());
        mNumberidView.setText(globalVariable.getUser().getNumeroId());
        mImageView.setImageBitmap(dencodeImage(globalVariable.getUser().getFotoPerfil()));

        LinearLayout img = (LinearLayout)findViewById(R.id.image_layout_edituser);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tiposid, android.R.layout.simple_spinner_item);
        tiposid.setAdapter(adapter);

        String myString = globalVariable.getUser().getTipoId(); //the value you want the position for

        ArrayAdapter myAdap = (ArrayAdapter) tiposid.getAdapter(); //cast to an ArrayAdapter

        int spinnerPosition = myAdap.getPosition(myString);

        //set the default according to value
        tiposid.setSelection(spinnerPosition);

        Button mEmailSignInButton = (Button) findViewById(R.id.register_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        callUserPreferencesApi();
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

        UsuarioEntity ur = new UsuarioEntity();
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

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

        ur.setIdUsuario(globalVariable.getUser().getIdUsuario());

        if(!TextUtils.isEmpty(encodedImage)){
            ur.setFotoPerfil(encodedImage);
        }

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(ur.getEmail())) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(ur.getEmail())) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else if (TextUtils.isEmpty(ur.getContrasena()) && !isPasswordValid(ur.getContrasena())) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if(ur.getContrasena().compareTo(confirmPass) != 0){
            mConfirmPassView.setError(getString(R.string.error_confirm_password));
            focusView = mConfirmPassView;
            cancel = true;
        } else if (TextUtils.isEmpty(ur.getUsuario())) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (TextUtils.isEmpty(ur.getNombres())) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        } else if (TextUtils.isEmpty(ur.getApellidos())) {
            mSecondnameView.setError(getString(R.string.error_field_required));
            focusView = mSecondnameView;
            cancel = true;
        } else if (TextUtils.isEmpty(ur.getNumeroId())) {
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
            showProgress(true);
            callRegisterApi(ur);
            //mAuthTask = new UserLoginTask(email, password);



            //mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 1;
    }

    private void callRegisterApi(UsuarioEntity ur){

        Call<UsuarioEntity> call = PlanSApiAdapter.getApiService().updateUser(ur);
        call.enqueue(this);

    }

    private void callSetPreferencesApi(List<NumberString> ns){

        Call<NumberString> call = PlanSApiAdapter.getApiService().updateUserPreferences(ns);
        call.enqueue(new setPreferencesCallBack());

    }

    @Override
    public void onResponse(Call<UsuarioEntity> call, Response<UsuarioEntity> response) {

        if(response.isSuccessful()){
            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            globalVariable.setUser(response.body());
            for(int i=0;i<this.preferencias.size();i++){
                this.preferencias.get(i).setNumber(globalVariable.getUser().getIdUsuario());
            }
            callSetPreferencesApi(this.preferencias);
        }
        else{
            System.out.println(response.errorBody());
            Toast.makeText(this, "Error al registrar el usuario",Toast.LENGTH_LONG).show();
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

    private void callUserPreferencesApi(){

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        Call<List<PreferenciaEntity>> call = PlanSApiAdapter.getApiService().getUserPreferencesById(globalVariable.getUser().getIdUsuario());
        call.enqueue(new UserPreferencesCallBack());

    }

    public void addToList(long num, String str){
        NumberString ns = new NumberString();
        ns.setNumber(num);
        ns.setStr(str);
        this.preferencias.add(ns);
    }

    public void setUserPrefereceList(List<PreferenciaEntity> lpe){
        this.preferencesUser = lpe;
    }

    public List<PreferenciaEntity> getUserPrefereceList(){
        return this.preferencesUser;
    }

    public void deleteList(long num, String str){
        NumberString ns = new NumberString();
        ns.setNumber(num);
        ns.setStr(str);

        for(int i=0; i<this.preferencias.size();i++){
            if(this.preferencias.get(i).getStr().compareTo(str)==0){
                this.preferencias.remove(i);
            }
        }
    }

    class setPreferencesCallBack implements Callback<NumberString> {


        @Override
        public void onResponse(Call<NumberString> call, Response<NumberString> response) {
            showProgress(false);
            finish();
        }

        @Override
        public void onFailure(Call<NumberString> call, Throwable t) {
            showProgress(false);
            Toast.makeText(getApplicationContext(), "Ha ocurrido un error inesperado en el servidor",Toast.LENGTH_LONG).show();
            t.printStackTrace();
        }
    }

    class UserPreferencesCallBack implements  Callback<List<PreferenciaEntity>>{

        @Override
        public void onResponse(Call<List<PreferenciaEntity>> call, Response<List<PreferenciaEntity>> response) {
            setUserPrefereceList(response.body());
            callDisplayPreferencesApi();
        }

        @Override
        public void onFailure(Call<List<PreferenciaEntity>> call, Throwable t) {
            t.printStackTrace();
        }
    }


    class PreferencesCallBack implements  Callback<List<PreferenciaEntity>>{


        @Override
        public void onResponse(Call<List<PreferenciaEntity>> call, Response<List<PreferenciaEntity>> response) {
            if(response.isSuccessful()){
                List<PreferenciaEntity> usrPref = getUserPrefereceList();
                final List<PreferenciaEntity> pe = response.body();
                ViewGroup mRegisterFormView = (ViewGroup) findViewById(R.id.checkboxboxes);
                final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                for (int i = 0; i < pe.size(); i++) {

                    CheckBox checkBox = new CheckBox(getApplicationContext());
                    checkBox.setText(pe.get(i).getNombre());

                    for (int y = 0; y < usrPref.size(); y++) {
                        if(usrPref.get(y).getIdPreferencia()==pe.get(i).getIdPreferencia()){
                            checkBox.setChecked(true);
                            addToList(globalVariable.getUser().getIdUsuario(),pe.get(i).getNombre());
                        }
                    }

                    final int finalI = i;
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b){
                                addToList(pe.get(finalI).getIdPreferencia(),pe.get(finalI).getNombre());
                            }
                            else{
                                deleteList(globalVariable.getUser().getIdUsuario(),pe.get(finalI).getNombre());
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

        mLoginFormView = findViewById(R.id.login_form_edituser);
        mProgressView = findViewById(R.id.login_progress_edituser);

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

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(bm.getHeight()>1080 && bm.getWidth()>1920){
            bm = getResizedBitmap(bm,bm.getHeight()/2,bm.getWidth()/2);
        }
        bm.compress(Bitmap.CompressFormat.JPEG,60,baos);
        int co=bm.getByteCount();
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK && requestCode==100){
            imageUri = data.getData();
            mImageView.setImageURI(imageUri);
            final InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                encodedImage = encodeImage(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth) {
        int width = image.getWidth();
        int height = image.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    //Image Picker
    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 100);
    }

    private Bitmap dencodeImage(String str)
    {
        try{
            byte [] encodeByte=Base64.decode(str,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}
