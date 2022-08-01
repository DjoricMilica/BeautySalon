package com.example.beautysalon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;


public class ReservationActivity extends AppCompatActivity  {
    private String [] oneHour = new String[] {"9:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00"};
    private String[] halfHour= new String[]{"9:00","9:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30","14:00"};
    private TextView txtDate, txtGetTermins;
    private String date="",selectedService="",serviceID;
    private int time;
    private FirebaseDatabase fd = FirebaseDatabase.getInstance();
    private DatabaseReference ref = fd.getReference();
    private ArrayList<String> serviceNames = new ArrayList<>();
    private ArrayList<String> reservatedTermins = new ArrayList<>();
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private RecyclerView recycleViewTable;
    private  ArrayList<Service> services=new ArrayList<>();
    private ArrayList<Reservation> reservations = new ArrayList<>();
    private ImageButton btnBackToProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        spinner= findViewById(R.id.spinner);

        txtDate = findViewById(R.id.txtDate);
        recycleViewTable = findViewById(R.id.recycleViewTermin);
        txtGetTermins = findViewById(R.id.txtGetTermins);
        recycleViewTable.setLayoutManager(new LinearLayoutManager(this));

        btnBackToProfile = findViewById(R.id.btnBackToProfile);
        btnBackToProfile.setBackgroundResource(R.drawable.ic_action_name);
        btnBackToProfile.setBackgroundColor(Color.TRANSPARENT);
        btnBackToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReservationActivity.this,CustomerProfileActivity.class));
            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                services.clear();
                services = getServices(snapshot);
                reservations.clear();
                reservations = getReservations(snapshot);
                fillSpinner(services);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ReservationActivity.this, android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        date = dayOfMonth + "." + month + "." + year + ".";
                        txtDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.RED));
                datePickerDialog.show();

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedService=spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtGetTermins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservatedTermins.clear();
                selectedService = spinner.getSelectedItem().toString();
                date = txtDate.getText().toString();
                if(selectedService.equals("")){
                    Toast.makeText(ReservationActivity.this,"Select service!",Toast.LENGTH_LONG).show();
                }else if(date.equals("")){
                    Toast.makeText(ReservationActivity.this,"Select date!",Toast.LENGTH_LONG).show();
                }else {
                    services.forEach(service -> {
                        if(service.name.equals(selectedService)){
                            serviceID=service.id;
                            time = service.time;
                        }
                    });
                    reservations.forEach(reservation -> {
                        if(reservation.date.equals(date) && reservation.serviceID.equals(serviceID)){
                            reservatedTermins.add(reservation.time);
                        }
                    });
                    if(time==30){
                        TerminAdapter terminAdapter1 = new TerminAdapter(ReservationActivity.this,halfHour,reservatedTermins,getIntent().getStringExtra("customerID"),date,serviceID);
                        recycleViewTable.setAdapter(terminAdapter1);
                    }else{
                        TerminAdapter terminAdapter2 = new TerminAdapter(ReservationActivity.this,oneHour,reservatedTermins,getIntent().getStringExtra("customerID"),date,serviceID);
                        recycleViewTable.setAdapter(terminAdapter2);
                    }

                }
            }
        });
    }

    public ArrayList<Service> getServices (DataSnapshot snapshot){
        ArrayList<Service> s = new ArrayList<>();
        Iterator<DataSnapshot> itService = snapshot.child("service").getChildren().iterator();
        while (itService.hasNext()){
            DataSnapshot dataSnapshot = itService.next();
            String name = dataSnapshot.child("name").getValue().toString();
            int time = Integer.parseInt(dataSnapshot.child("time").getValue().toString());
            Float price =Float.parseFloat(dataSnapshot.child("price").getValue().toString());
            s.add(new Service(dataSnapshot.getKey(),name,time,price));
        }
        return s;
    }

    public ArrayList<Reservation> getReservations(DataSnapshot snapshot){
        ArrayList<Reservation> r = new ArrayList<>();
        Iterator<DataSnapshot> itReservation = snapshot.child("reservation").getChildren().iterator();
        while (itReservation.hasNext()){
            DataSnapshot dataSnapshot = itReservation.next();
            String customerID = dataSnapshot.child("customerID").getValue().toString();
            String time = dataSnapshot.child("time").getValue().toString();
            String date = dataSnapshot.child("date").getValue().toString();
            String serviceID = dataSnapshot.child("serviceID").getValue().toString();
            r.add(new Reservation(time,date,customerID,serviceID));
        }
        return r;

    }

    public void fillSpinner(ArrayList<Service> services){
        serviceNames.clear();
        services.forEach(service -> {
            serviceNames.add(service.getName());
        });
        adapter = new ArrayAdapter<>(ReservationActivity.this,R.layout.service_name_item,serviceNames);
        Spinner spinner = findViewById(R.id.spinner);
                spinner.setAdapter(adapter);
    }
}
