package com.example.jaky_d.smartattendance;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import android.content.Intent;
import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private EditText f1;
    private EditText f2;
    private EditText f3;
    private Button signUp;
    private TextView signInLink;
    private CheckBox tnc;
    private int userid;
    private EditText f4;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference myRootRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://smartattendance-c896a.firebaseio.com/Users");
        f1 = (EditText) findViewById(R.id.newUserName);
        f2 = (EditText) findViewById(R.id.newPassword);
        signUp = (Button) findViewById(R.id.signupbtn);
        signInLink = (TextView) findViewById(R.id.signinlink);
        tnc = (CheckBox) findViewById(R.id.tnc);
        f3 = (EditText) findViewById(R.id.cfmpassword);
        f4 = (EditText) findViewById(R.id.enroll);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignup();
                String Enrollment = f4.getText().toString();
                String email = f1.getText().toString();
                String pass = f2.getText().toString();
                DatabaseReference user = myRootRef.child(Enrollment);
                user.child("Email").setValue(email);
                user.child("Password").setValue(pass);




            }
        });

        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void startSignup() {
        final String email = f1.getText().toString();
        final String password = f2.getText().toString();
        String ConfirmPassword = f3.getText().toString();


        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(SignUpActivity.this, "Please enter Email ID or password", Toast.LENGTH_SHORT).show();
        } else {
            if (password.equals(ConfirmPassword)) {
                if (tnc.isChecked()) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    Toast.makeText(SignUpActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                                        Log.e("Account Create: failed" + task.getException(), String.valueOf(task));
                                    } else {
                                        Log.e("task", String.valueOf(task));

                                    }
                                }
                            });
                } else
                    Toast.makeText(SignUpActivity.this, "Please accept terms and condition!", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(SignUpActivity.this, "Password Do Not Match", Toast.LENGTH_SHORT).show();
        }
    } //end of fn
}