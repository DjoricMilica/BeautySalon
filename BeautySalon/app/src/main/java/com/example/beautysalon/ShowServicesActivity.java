package com.example.beautysalon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class ShowServicesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Service> services = new ArrayList<>();
    private ServiceAdapter serviceAdapter;
    private FirebaseDatabase fd = FirebaseDatabase.getInstance();
    private ImageButton btnBackToHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_service);

        recyclerView = findViewById(R.id.recyclerViewServices);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceAdapter = new ServiceAdapter(ShowServicesActivity.this,services);
        recyclerView.setAdapter(serviceAdapter);

        btnBackToHome = findViewById(R.id.btnBackToHome);
        btnBackToHome.setBackgroundResource(R.drawable.ic_action_name);
        btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowServicesActivity.this,MainActivity.class));
            }
        });

        DatabaseReference ref = fd.getReference().child("service");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> itService = snapshot.getChildren().iterator();
                while (itService.hasNext()){
                    DataSnapshot s = itService.next();
                    String name = s.child("name").getValue().toString();
                    int time = Integer.parseInt(s.child("time").getValue().toString());
                    float price = Float.parseFloat(s.child("price").getValue().toString());
                    Service service = new Service(s.getKey(),name,time,price);
                    //Log.i(s.toString(),"SERVIS");
                    services.add(service);
                    serviceAdapter.notifyDataSetChanged();
                }
                serviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }
}