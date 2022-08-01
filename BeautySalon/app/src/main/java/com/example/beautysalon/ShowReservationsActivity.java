package com.example.beautysalon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;

public class ShowReservationsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Reservation> reservations = new ArrayList<>();
    private ReservationAdapter reservationAdapter;
    private FirebaseDatabase fd = FirebaseDatabase.getInstance();
    private ImageButton btnBackToProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_reservations);

        recyclerView = findViewById(R.id.recyclerViewReservations);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reservationAdapter = new ReservationAdapter(ShowReservationsActivity.this,reservations);
        recyclerView.setAdapter(reservationAdapter);
        btnBackToProfile = findViewById(R.id.btnBackToProfile1);
        btnBackToProfile.setBackgroundResource(R.drawable.ic_action_name);
        btnBackToProfile.setBackgroundColor(Color.TRANSPARENT);
        btnBackToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowReservationsActivity.this,CustomerProfileActivity.class));
            }
        });

        DatabaseReference ref = fd.getReference().child("reservation");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reservations.clear();
                Iterator<DataSnapshot> itReservation = snapshot.getChildren().iterator();
                while (itReservation.hasNext()){
                    DataSnapshot s = itReservation.next();
                    String customerID = s.child("customerID").getValue().toString();
                    if(customerID.equals(getIntent().getStringExtra("customerID"))){
                        String serviceID = s.child("serviceID").getValue().toString();
                        String time = s.child("time").getValue().toString();
                        String date = s.child("date").getValue().toString();
                        Reservation reservation = new Reservation(time,date,customerID,serviceID);
                        // Log.i(s.toString(),"SERVIS");
                        reservations.add(reservation);
                        reservationAdapter.notifyDataSetChanged();
                    }

                }
                reservationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}