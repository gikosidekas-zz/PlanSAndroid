package com.example.georgios.plans.Custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.georgios.plans.R;
import com.example.georgios.plans.model.PlanEntity;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by IkosidekasDesktop on 26/11/2017.
 */

public class CustomAdapterPlan extends ArrayAdapter<PlanEntity> {

    private PlanEntity[] pe;
    private Context context;

    public CustomAdapterPlan(@NonNull Context context, PlanEntity[] resource) {
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

        PlanEntity singlePlan = getItem(position);

        TextView name = (TextView) customView.findViewById(R.id.planName);
        TextView location = (TextView) customView.findViewById(R.id.planLocation);
        TextView date = (TextView) customView.findViewById(R.id.planDate);
        TextView cost = (TextView) customView.findViewById(R.id.planCost);

        name.setText(singlePlan.getNombre());

        auxStr = singlePlan.getUbicacion();

        String[] parts = auxStr.split("\\|");

        auxStr = parts[0];

        location.setText("Ubicacion: "+auxStr);

        String str = singlePlan.getFechaInicio();
        date.setText("Fecha: "+makeNewDate(str));

        cost.setText("Costo: "+Long.toString(singlePlan.getCostoPromedio()));

        return customView;

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
}
