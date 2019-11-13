package com.example.jaky_d.smartattendance;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button msenddata;
    private EditText memail;
    private EditText mpassword;
    private TextView msignup;
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener mauthlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mauth = FirebaseAuth.getInstance();

        msenddata = (Button) findViewById(R.id.button1);
        memail = (EditText) findViewById(R.id.text1);
        mpassword = (EditText) findViewById(R.id.text2);
        msignup = (TextView) findViewById(R.id.signuplink);
        mauthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(MainActivity.this, AccountActivity.class));
                }
            }
        };

        msenddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startsignin();


            }
        });
        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        mauth.addAuthStateListener(mauthlistener);
    }

    public void startsignin()
    {
        String email = memail.getText().toString();
        String password = mpassword.getText().toString();

        if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(password))
        {
Toast.makeText(MainActivity.this,"Field Are Empty",Toast.LENGTH_LONG).show();
        }
        else
            {
        mauth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(!task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this,"Sign In Problem",Toast.LENGTH_LONG).show();
            }
        }

        });
        }
    }
}
