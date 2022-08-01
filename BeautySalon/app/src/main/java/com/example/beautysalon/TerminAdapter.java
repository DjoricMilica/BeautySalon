package com.example.beautysalon;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TerminAdapter extends RecyclerView.Adapter<TerminAdapter.TableViewHolder> {
    Context context;
   String[] termins;
   ArrayList<String > reservatedTermins;
    String customerID;
    String serviceID;
    String date;

    public TerminAdapter(Context context, String[] termins, ArrayList<String> reservatedTermins , String customerID, String date, String serviceID) {
        this.context = context;
        this.termins = termins;
        this.reservatedTermins =reservatedTermins;
        this.customerID=customerID;
        this.serviceID=serviceID;
        this.date=date;
    }

    @NonNull
    @Override
    public TerminAdapter.TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.termin_item,parent,false);
        return new TableViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TerminAdapter.TableViewHolder holder, int position) {
        holder.txtTermin.setText(termins[position] + " h" );
        if(holder.txtTermin.isClickable()) {
            holder.txtTermin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Reservation r = new Reservation(termins[position], date, customerID, serviceID);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("reservation");
                    reference.push().setValue(r);
                    holder.txtTermin.setClickable(false);
                    holder.txtTermin.setBackgroundColor(Color.RED);
                    holder.txtTermin.setText(holder.txtTermin.getText().toString() + " RESERVED");

                }
            });
        }
        reservatedTermins.forEach(termin -> {
                    if (termin.equals(termins[position])) {
                        holder.txtTermin.setClickable(false);
                        holder.txtTermin.setBackgroundColor(Color.RED);
                        holder.txtTermin.setText(holder.txtTermin.getText().toString() + " RESERVED");
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return termins.length;
    }

    public class TableViewHolder extends RecyclerView.ViewHolder {
        TextView txtTermin;
        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTermin= itemView.findViewById(R.id.terminItem);

        }
    }
}
