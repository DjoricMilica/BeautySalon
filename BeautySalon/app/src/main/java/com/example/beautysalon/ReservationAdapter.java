package com.example.beautysalon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class ReservationAdapter extends  RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>{

    private Context context;
    private ArrayList<Reservation> reservations;

    public ReservationAdapter(Context context, ArrayList<Reservation> reservations) {
        this.context = context;
        this.reservations = reservations;
    }
    @NonNull
    @Override
    public ReservationAdapter.ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reservation_item,parent,false);
        return new ReservationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationAdapter.ReservationViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        DatabaseReference ref = fd.getReference().child("service").child(reservation.serviceID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.txtServiceName.setText(snapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.txtReservationDate.setText(String.valueOf(reservation.date));
        holder.txtReservationTime.setText(reservation.time+" h");
        holder.btnCancelReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = fd.getReference().child("reservation");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterator<DataSnapshot> it = snapshot.getChildren().iterator();
                        while (it.hasNext()){
                            DataSnapshot r = it.next();
                            if (r.child("serviceID").getValue().toString().equals(reservation.serviceID) &&
                            r.child("time").getValue().toString().equals(reservation.time) &&
                            r.child("date").getValue().toString().equals(reservation.date) &&
                            r.child("customerID").getValue().toString().equals(reservation.customerID)){
                                r.getRef().removeValue();
                                notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public class ReservationViewHolder extends RecyclerView.ViewHolder {

        private TextView txtReservationDate, txtReservationTime, txtServiceName;
        private Button btnCancelReservation;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            txtReservationDate = itemView.findViewById(R.id.txtReservationDate);
            txtReservationTime= itemView.findViewById(R.id.txtReservationTime);
            txtServiceName = itemView.findViewById(R.id.txtServiceName);
            btnCancelReservation = itemView.findViewById(R.id.btnCancleReservation);

        }
    }


}
