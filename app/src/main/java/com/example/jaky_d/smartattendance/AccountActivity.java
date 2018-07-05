package com.example.jaky_d.smartattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {
private Button logOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        logOut = (Button) findViewById(R.id.logoutbtn);
       logOut.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               FirebaseAuth.getInstance().signOut();
               startActivity(new Intent(AccountActivity.this,MainActivity.class));
               Toast.makeText(AccountActivity.this, "Logged Out Successfully!", Toast.LENGTH_SHORT).show();
           }
       });
    }
}
