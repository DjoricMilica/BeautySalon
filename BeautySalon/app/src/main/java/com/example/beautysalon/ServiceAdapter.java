package com.example.beautysalon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private Context context;
    private ArrayList<Service> services;

    public ServiceAdapter(Context context, ArrayList<Service> services) {
        this.context = context;
        this.services = services;
    }

    @NonNull
    @Override
    public ServiceAdapter.ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.service_item,parent,false);
        return new ServiceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceAdapter.ServiceViewHolder holder, int position) {
        Service service = services.get(position);

        holder.txtName.setText(service.name);
        holder.txtTime.setText(String.valueOf(service.time)+" min");
        holder.txtPrice.setText(String.valueOf(service.price)+" eur");
    }

    @Override
    public int getItemCount() {
        return services.size();
    }


    public class ServiceViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName, txtTime, txtPrice;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtPrice = itemView.findViewById(R.id.txtPrice);
        }
    }
}
