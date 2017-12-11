package com.example.georgios.plans.Custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.georgios.plans.R;
import com.example.georgios.plans.model.PlanEntity;
import com.example.georgios.plans.model.UsuarioEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Georgios on 26/11/2017.
 */

public class CustomAdapterUsuario extends ArrayAdapter<UsuarioEntity> {

    private UsuarioEntity[] pe;
    private Context context;

    public CustomAdapterUsuario(@NonNull Context context, UsuarioEntity[] resource) {
        super(context, R.layout.custom_row ,resource);
        pe = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflator = LayoutInflater.from(getContext());

        String auxStr;

        View customView = convertView;
        if(customView == null){
            customView = inflator.inflate(R.layout.custom_row, parent, false);
        }

        UsuarioEntity singlePlan = getItem(position);

        ImageView image = (ImageView) customView.findViewById(R.id.planImage);
        TextView name = (TextView) customView.findViewById(R.id.planName);
        TextView location = (TextView) customView.findViewById(R.id.planLocation);
        TextView date = (TextView) customView.findViewById(R.id.planDate);
        TextView cost = (TextView) customView.findViewById(R.id.planCost);

        name.setText(singlePlan.getUsuario());

        location.setText("Nombres: "+singlePlan.getNombres());

        date.setText("Apellidos: "+singlePlan.getApellidos());

        cost.setText("Correo: "+singlePlan.getEmail());

        image.setImageBitmap(dencodeImage(singlePlan.getFotoPerfil()));

        return customView;

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



}
