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

public class CustomAdapter extends ArrayAdapter<PlanEntity> {

    private PlanEntity[] pe;
    private Context context;

    public CustomAdapter(@NonNull Context context, PlanEntity[] resource) {
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

        String str = getDateCurrentTimeZone(singlePlan.getFechaInicio());
        date.setText("Fecha: "+str);

        cost.setText("Costo: "+Integer.toString(singlePlan.getCostoPromedio()));

        return customView;

    }

    public  String getDateCurrentTimeZone(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        }catch (Exception e) {
        }
        return "";
    }
}
