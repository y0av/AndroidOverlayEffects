package xyz.yoav.weaponizedscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        askForSystemOverlayPermission();

        Intent svc = new Intent(this, OverlayService.class);
        startService(svc);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // To prevent starting the service if the required permission is NOT granted.
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                //Permission is not available. Display error text.
                Toast.makeText(this, "Draw over other app permission not available. Can't start the application without the permission.", Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            //super.onActivityResult(requestCode, resultCode, data);
        }

    }

        private void askForSystemOverlayPermission() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                //If the draw over permission is not available to open the settings screen
                //to grant the permission.
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, TYPE_APPLICATION_OVERLAY);
            }
        }
}
