package com.task.taskshopping.view;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.task.taskshopping.R;

/**
 * Created by KATHIR on 07-06-2020
 */
public class Splash extends Activity {
    String app_ver;
    TextView v;
    private long ms = 0;
    private long splashDuration = 3000;
    private boolean splashActive = true;
    private boolean paused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);


        Thread mythread = new Thread() {

            public void run() {

                try {

                    while (splashActive && ms < splashDuration) {

                        if (!paused)

                            ms = ms + 100;

                        sleep(100);

                    }

                } catch (Exception e) {

                } finally {

                    Intent intent = new Intent(Splash.this, MainActivity.class);

                    startActivity(intent);
                    finish();


                }

            }

        };

        mythread.start();

    }

}