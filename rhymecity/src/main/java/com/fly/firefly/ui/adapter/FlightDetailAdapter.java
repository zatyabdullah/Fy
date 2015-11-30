package com.fly.firefly.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fly.firefly.R;
import com.fly.firefly.api.obj.FlightInfo;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FlightDetailAdapter extends BaseAdapter {

    private final Context context;
    private final List<FlightInfo> obj;
    private String departureAirport;
    private String arrivalAirport;
    private String flightClass;


    public FlightDetailAdapter(Context context, List<FlightInfo> paramObj,String depart, String arrival,String fclass) {
        this.context = context;
        this.obj = paramObj;
        this.departureAirport = depart;
        this.arrivalAirport = arrival;
        this.flightClass = fclass;
    }

    @Override
    public int getCount() {
        return obj == null ? 0 : obj.size();
    }

    @Override
    public Object getItem(int position) {
        return obj == null ? null : obj.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        @InjectView(R.id.txtFlightNo) TextView txtFlightNo;
        @InjectView(R.id.txtArrivalTime) TextView txtArrivalTime;
        @InjectView(R.id.txtDepartureTime) TextView txtDepartureTime;
        @InjectView(R.id.txtDepartureAirport) TextView txtDepartureAirport;
        @InjectView(R.id.txtArrivalAirport) TextView txtArrivalAirport;
        @InjectView(R.id.txtFarePrice) TextView txtFarePrice;
        @InjectView(R.id.checkBox) CheckBox checkBox;


    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.flight_detail_list, parent, false);
            vh = new ViewHolder();
            ButterKnife.inject(vh, view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        String totalFare = "";
        if(flightClass.equals("PREMIER")){
            totalFare = "SOLD OUT";
            //totalFare = obj.get(position).getFlexObj().getTotal_fare();

        }else{
            totalFare = obj.get(position).getBasicObj().getTotal_fare();
        }

        vh.txtFlightNo.setText("FLIGHT NO. "+ obj.get(position).getFlight_number());
        vh.txtArrivalTime.setText(obj.get(position).getArrival_time());
        vh.txtDepartureTime.setText(obj.get(position).getDeparture_time());
        vh.txtDepartureAirport.setText(departureAirport);
        vh.txtDepartureAirport.setText(departureAirport);
        vh.txtArrivalAirport.setText(arrivalAirport);
        vh.txtFarePrice.setText("MYR " + totalFare);
        return view;
    }
}
