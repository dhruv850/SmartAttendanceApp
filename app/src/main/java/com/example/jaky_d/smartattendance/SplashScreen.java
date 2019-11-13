package com.example.jaky_d.smartattendance;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar mprogress;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mprogress = (ProgressBar) findViewById(R.id.splash_screen_progressbar);

            new Thread(new Runnable()
            {

            public void run()
                {

                doWork();
                startApp();
                finish();
                }
            }).start();

}

public void doWork()
{
        for (int prog=0;prog<100;prog+=25)
        {
            try
            {
                Thread.sleep(1000);
                mprogress.setProgress(prog);
            }   catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void startApp()
    {
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(intent);
    }
}
