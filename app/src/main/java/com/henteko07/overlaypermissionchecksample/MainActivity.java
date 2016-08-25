package com.henteko07.overlaypermissionchecksample;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Process;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";
    private AppOpsManager appOpsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Overlayの許可画面の監視
        appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            appOpsManager.startWatchingMode(AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW, getPackageName(), mOpChangedListener);
        }

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (!Settings.canDrawOverlays(getApplicationContext())) {
                        // Overlayの許可画面を開く
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
//                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private AppOpsManager.OnOpChangedListener mOpChangedListener = new AppOpsManager.OnOpChangedListener() {
        @Override
        public void onOpChanged(String op, String packageName) {
            Log.v(TAG, op + " changed in " + packageName);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                try{
                    int mode = appOpsManager.checkOp(AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW, Process.myUid(),
                            getApplicationContext().getPackageName());
                    if (mode == AppOpsManager.MODE_ALLOWED) {
                        // 許可時のみ
                        Log.d(TAG, "change");
                    }
                } catch (RuntimeException e){
                    Log.d(TAG, e.toString());
                }
            }
        }
    };
}
