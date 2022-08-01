package com.example.beautysalon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText txtFirstName, txtLastName, txtEmail,txtMobilePhone,
                    txtNewFirstName,txtNewLastName, txtNewEmail, txtNewMobilePhone;
    private String firstName, lastName, email, mobilePhone,
                    newFirstName, newLastName, newEmail, newMobilePhone;
    private String customerID;
    private TextView someone;
    FirebaseDatabase fd = FirebaseDatabase.getInstance();
     private DatabaseReference ref =fd.getReference();
    private Toolbar customerToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        customerToolbar = findViewById(R.id.customerToolbar);
        setSupportActionBar(customerToolbar);

        customerToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().equals("Make reservation")){
                    Intent intent = new Intent(CustomerProfileActivity.this, ReservationActivity.class);
                    intent.putExtra("customerID",customerID);
                    startActivity(intent);
                    return true;
                }else if(item.getTitle().toString().equals("All reservations")){
                    Intent intent = new Intent(CustomerProfileActivity.this, ShowReservationsActivity.class);
                    intent.putExtra("customerID",customerID);
                    startActivity(intent);
                    return true;
                }else if(item.getTitle().toString().equals("Log out")){
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(CustomerProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        customerID = user.getUid();

        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName =findViewById(R.id.txtLastName);
        txtEmail =findViewById(R.id.txtEmail);
        txtMobilePhone=findViewById(R.id.txtMobilePhone);
        someone = findViewById(R.id.txtSomeone);


        firstName = txtFirstName.getText().toString().trim();
        lastName = txtLastName.getText().toString().trim();
        email = txtEmail.getText().toString().trim();
        mobilePhone = txtMobilePhone.toString().trim();
        DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("customer").child(customerID);
        r.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loadDataOfCustomer(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        Button btnUpdateCustomerInfo = findViewById(R.id.btnUpdateCustomerInfo);
        btnUpdateCustomerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCustomer();
            }
        });

    }
    public void loadDataOfCustomer(DataSnapshot snapshot){
      txtFirstName.setText(snapshot.child("firstName").getValue().toString());
      txtLastName.setText(snapshot.child("lastName").getValue().toString());
      txtEmail.setText(snapshot.child("email").getValue().toString());
      txtMobilePhone.setText(snapshot.child("mobilePhone").getValue().toString());
      someone.setText("Hello "+snapshot.child("firstName").getValue().toString());

    }

    public void updateCustomer(){
        txtNewFirstName = findViewById(R.id.txtFirstName);
        txtNewLastName = findViewById(R.id.txtLastName);
        txtNewEmail = findViewById(R.id.txtEmail);
        txtNewMobilePhone = findViewById(R.id.txtMobilePhone);

        newFirstName = txtNewFirstName.getText().toString().trim();
        newLastName = txtNewLastName.getText().toString().trim();
        newEmail = txtNewEmail.getText().toString().trim();
        newMobilePhone = txtNewMobilePhone.getText().toString().trim();

        if(newFirstName.isEmpty()){
            txtFirstName.setError("First name is required!");
            txtFirstName.requestFocus();
        }else if(newLastName.isEmpty()){
            txtLastName.setError("Last name is required!");
            txtLastName.requestFocus();
        }else if(newEmail.isEmpty()){
            txtEmail.setError("Email is required!");
            txtEmail.requestFocus();
        }else if(newMobilePhone.isEmpty()){
            txtMobilePhone.setError("Mobile phone is required!");
            txtMobilePhone.requestFocus();
        } else if(!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()){
            txtEmail.setError("Email is incorrect!");
            txtEmail.requestFocus();
        }else {
            if (!newFirstName.equals(firstName)){
                ref.child("customer").child(customerID).child("firstName").setValue(newFirstName);
            }
            if (!newLastName.equals(lastName)){
                ref.child("customer").child(customerID).child("lastName").setValue(newLastName);
            }

            if (!newEmail.equals(email)){
                ref.child("customer").child(customerID).child("email").setValue(newEmail);
            }
            if (!newMobilePhone.equals(mobilePhone)){
                ref.child("customer").child(customerID).child("mobilePhone").setValue(newMobilePhone);
            }
            Toast.makeText(CustomerProfileActivity.this,"Updated!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.customer_menu,menu);
        return true;

    }
}