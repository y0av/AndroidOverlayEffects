package xyz.yoav.weaponizedscreen;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.plattysoft.leonids.ParticleSystem;

public class OverlayService extends Service {

    private static String TAG = "OverlayService";

    private WindowManager mWindowManager;
    private View mOverlayView;




    public OverlayService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {  return null;  }

    @Override
    public void onCreate() {
        super.onCreate();

        EffectManager.initializeDrawables(getResources());
        mOverlayView = LayoutInflater.from(this).inflate(R.layout.wheel, null); //inflate entire layout
        mOverlayView.setOnTouchListener((v, event) -> { windowTouched(event); return true; });
        mOverlayView.findViewById(R.id.quitBtn).setOnClickListener(v -> { quitApp(); }); //set quit btn

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mOverlayView, params);
    }

    private void windowTouched(MotionEvent event) {
        EffectManager.playCurrentEffect((ViewGroup)mOverlayView,(int)event.getX(), (int)event.getY());
    }

    private void quitApp() {
        OverlayService.this.stopService(new Intent(OverlayService.this.getApplicationContext(), OverlayService.class));
        OverlayService.this.onDestroy();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOverlayView != null)
            mWindowManager.removeView(mOverlayView);
    }
}
