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
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText txtEmail, txtPassword;
    private String email,password;
    private TextView txtRedirectToSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        txtEmail = findViewById(R.id.txtEmailLogIn);
        txtPassword = findViewById(R.id.txtPassword);
        txtRedirectToSignUp = findViewById(R.id.txtRedirectToSignUp);

        Button btnLogIn = findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });

        txtRedirectToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public void logIn(){
        email = txtEmail.getText().toString().trim();
        password = txtPassword.getText().toString().trim();

    if(email.isEmpty()){
        txtEmail.setError("Email is required!");
        txtEmail.requestFocus();
    }else if(password.isEmpty()) {
        txtPassword.setError("Password is required!");
        txtPassword.requestFocus();
    }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        txtEmail.setError("Email is incorrect!");
        txtEmail.requestFocus();
    }else{
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                    if(u.isEmailVerified()){
                        startActivity(new Intent(LogInActivity.this,CustomerProfileActivity.class));
                        Toast.makeText(LogInActivity.this,"Successfully!",Toast.LENGTH_SHORT).show();
                    }else{
                        u.sendEmailVerification();
                        Toast.makeText(LogInActivity.this,"Verify your account!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LogInActivity.this,"Failed to log in!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    }
}