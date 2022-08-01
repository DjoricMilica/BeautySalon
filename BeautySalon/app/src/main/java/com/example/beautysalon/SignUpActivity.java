package com.example.beautysalon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Customer customer;
    private EditText txtFirstName, txtLastName, txtEMail, txtMobilePhone, txtPassword;
    private TextView txtRedirectToLogIn;
    private String firstName, lastName, email, mobilePhone, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        txtFirstName =(EditText) findViewById(R.id.txtFirstNameCustomer);
        txtLastName =findViewById(R.id.txtLastNameCustomer);
        txtEMail =findViewById(R.id.txtEMailCustomer);
        txtMobilePhone=findViewById(R.id.txtMobilePhoneCustomer);
        txtPassword = findViewById(R.id.txtPasswordCustomer) ;
        txtRedirectToLogIn = findViewById(R.id.txtRedirectToLogIn);

        Button btnSignUpCustomer = (Button) findViewById(R.id.btnSignUpCustomer);
        btnSignUpCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               signUp();
            }
        });

        txtRedirectToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,LogInActivity.class);
                startActivity(intent);
            }
        });
    }

    public void signUp(){
        firstName = txtFirstName.getText().toString().trim();
        lastName = txtLastName.getText().toString().trim();
        email = txtEMail.getText().toString().trim();
        mobilePhone = txtMobilePhone.getText().toString().trim();
        password = txtPassword.getText().toString().trim();

        if(firstName.isEmpty()){
            txtFirstName.setError("First name is required!");
            txtFirstName.requestFocus();
        }else if(lastName.isEmpty()){
            txtLastName.setError("Last name is required!");
            txtLastName.requestFocus();
        }else if(email.isEmpty()){
            txtEMail.setError("Email is required!");
            txtEMail.requestFocus();
        }else if(mobilePhone.isEmpty()){
            txtMobilePhone.setError("Mobile phone is required!");
            txtMobilePhone.requestFocus();
        }else if(password.isEmpty()) {
            txtPassword.setError("Password is required!");
            txtPassword.requestFocus();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEMail.setError("Email is incorrect!");
            txtEMail.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        customer = new Customer(firstName,lastName, email,mobilePhone);
                        FirebaseDatabase.getInstance().getReference("customer").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                setValue(customer).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SignUpActivity.this,"Customer signed up successfully!",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(SignUpActivity.this,LogInActivity.class));
                                }else{
                                    Toast.makeText(SignUpActivity.this,"Failed to sign up!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(SignUpActivity.this,"Failed to sign up!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}

