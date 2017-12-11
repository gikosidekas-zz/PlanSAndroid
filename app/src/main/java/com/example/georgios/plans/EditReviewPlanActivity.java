package com.example.georgios.plans;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.georgios.plans.Custom.CustomAdapterUsuario;
import com.example.georgios.plans.api.PlanSApiAdapter;
import com.example.georgios.plans.model.GlobalClass;
import com.example.georgios.plans.model.NumberPair;
import com.example.georgios.plans.model.UsuarioEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditReviewPlanActivity extends AppCompatActivity {

    private TextView mplanDescriptionText;
    private TextView mplanUbicacionText;
    private TextView mplanFechaInicioText;
    private TextView mplanFechaFinalText;
    private TextView mplanCostoText;
    private TextView mplanCreadorText;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_review_plan);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        //Back Arrow in actionBar
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            setTitle(globalVariable.getPlan().getNombre());
        }

        mplanDescriptionText = (TextView) findViewById(R.id.planDescriptionText);
        mplanUbicacionText = (TextView) findViewById(R.id.planUbicacionText);
        mplanFechaInicioText = (TextView) findViewById(R.id.planFechaInicioText);
        mplanFechaFinalText = (TextView) findViewById(R.id.planFechaFinalText);
        mplanCostoText = (TextView) findViewById(R.id.planCostoText);
        mplanCreadorText = (TextView) findViewById(R.id.planCreadorText);
        mImageView = (ImageView) findViewById(R.id.imagen_edsubs);

        callgetUserbyIdApi(globalVariable.getPlan().getCreadorPlan());

        mplanDescriptionText.setText(globalVariable.getPlan().getDescripcion());
        mplanUbicacionText.setText(globalVariable.getPlan().getUbicacion().split("\\|")[0]);

        mplanFechaInicioText.setText(makeNewDate(globalVariable.getPlan().getFechaInicio()));
        mplanFechaFinalText.setText(makeNewDate(globalVariable.getPlan().getFechaFinal()));
        mplanCostoText.setText(String.valueOf(globalVariable.getPlan().getCostoPromedio()));

        Button mEmailSignInButton = (Button) findViewById(R.id.subscribe_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),EditPlanActivity.class);
                finish();
                startActivity(intent);
            }
        });

        mImageView.setImageBitmap(dencodeImage(globalVariable.getPlan().getImagenPlan()));

        callAsistentesPlanApi(globalVariable.getPlan().getIdPlan());
    }

    //For the arrow back button to function.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public  String getDateCurrentTimeZone(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currenTimeZone = (Date) calendar.getTime();
            return makeNewDate(sdf.format(currenTimeZone));
        }catch (Exception e) {
        }
        return "";
    }

    private Bitmap dencodeImage(String str)
    {
        try{
            byte [] encodeByte= Base64.decode(str,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public String makeNewDate(String str){
        String date;
        String time;
        String[] parts;

        parts=str.split("T");

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

    private void callgetUserbyIdApi(long id){
        Call<UsuarioEntity> call = PlanSApiAdapter.getApiService().getUserById(id);
        call.enqueue(new PreferencesCallBack());
    }

    private void callAsistentesPlanApi(long id){
        Call<List<UsuarioEntity>> call = PlanSApiAdapter.getApiService().getPlanSubscribedUsersById(id);
        call.enqueue(new SubscribedUsersCallBack());
    }

    private void callSubscribeApi(long idplan, long idusr){

        NumberPair np = new NumberPair();

        np.setIdPlan(idplan);
        np.setIdUser(idusr);

        Call<NumberPair> call = PlanSApiAdapter.getApiService().subscribeToPlan(np);
        call.enqueue(new SubscribeCallBack());
    }

    class SubscribeCallBack implements Callback<NumberPair> {

        @Override
        public void onResponse(Call<NumberPair> call, Response<NumberPair> response) {
            finish();
        }

        @Override
        public void onFailure(Call<NumberPair> call, Throwable t) {
            t.printStackTrace();
        }
    }

    class PreferencesCallBack implements Callback<UsuarioEntity> {
        @Override
        public void onResponse(Call<UsuarioEntity> call, Response<UsuarioEntity> response) {
            mplanCreadorText.setText("Usuario: "+response.body().getUsuario()+" Correo: "+response.body().getEmail());
        }

        @Override
        public void onFailure(Call<UsuarioEntity> call, Throwable t) {
            t.printStackTrace();
        }
    }

    class SubscribedUsersCallBack implements Callback<List<UsuarioEntity>> {

        private Bitmap dencodeImage(String str)
        {
            try{
                byte [] encodeByte= Base64.decode(str,Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return bitmap;
            }catch(Exception e){
                e.getMessage();
                return null;
            }
        }

        @Override
        public void onResponse(Call<List<UsuarioEntity>> call, Response<List<UsuarioEntity>> response) {
            //conversion lista a array
            final List<UsuarioEntity> lpe = response.body();
            UsuarioEntity[] lpea= new UsuarioEntity[lpe.size()];
            lpea = lpe.toArray(lpea);
            UsuarioEntity item;

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

            ListAdapter adapt = new CustomAdapterUsuario(getApplicationContext(),lpea);
            LinearLayout linearLay = (LinearLayout) findViewById(R.id.asistentes);
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            for (int i=0;i<lpe.size();i++) {

                View view  = inflater.inflate(R.layout.custom_row, linearLay, false);

                UsuarioEntity singlePlan = lpe.get(i);

                TextView name = (TextView) view.findViewById(R.id.planName);
                TextView location = (TextView) view.findViewById(R.id.planLocation);
                TextView date = (TextView) view.findViewById(R.id.planDate);
                TextView cost = (TextView) view.findViewById(R.id.planCost);
                ImageView image = (ImageView) view.findViewById(R.id.planImage);

                name.setText(singlePlan.getUsuario());

                location.setText("Nombres: "+singlePlan.getNombres());

                date.setText("Apellidos: "+singlePlan.getApellidos());

                cost.setText("Correo: "+singlePlan.getEmail());

                image.setImageBitmap(dencodeImage(singlePlan.getFotoPerfil()));

                // set item content in view
                linearLay.addView(view);
            }

        }

        @Override
        public void onFailure(Call<List<UsuarioEntity>> call, Throwable t) {
            t.printStackTrace();
        }
    }
}
