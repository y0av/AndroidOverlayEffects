package xyz.yoav.weaponizedscreen;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.plattysoft.leonids.ParticleSystem;

public class OverlayService extends Service {

    private static String TAG = "OverlayService";

    private WindowManager mWindowManager;
    private View mOverlayView;

    private int numParticles = 80;
    private long timeToLive = 1000;
    private int drawableResId = R.drawable.particle;
    Drawable particle1;

    public OverlayService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {  return null;  }

    @Override
    public void onCreate() {
        super.onCreate();

        mOverlayView = LayoutInflater.from(this).inflate(R.layout.wheel, null);
        particle1 = ResourcesCompat.getDrawable(getResources(), drawableResId, null);

        mOverlayView.setOnTouchListener((v, event) -> { windowTouched(event); return true; });


        mOverlayView.findViewById(R.id.quitBtn).setOnClickListener(v -> {
            Log.d(TAG, "## ouchy!");
            OverlayService.this.stopService(new Intent(OverlayService.this.getApplicationContext(), OverlayService.class));
            OverlayService.this.onDestroy();
        });

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
        Log.d(TAG,"## click! particle! x:" + event.getX() + " , y:" + event.getY());
        new ParticleSystem((ViewGroup)mOverlayView, numParticles, particle1 , timeToLive)
                .setSpeedRange(0.2f, 0.5f)
                .emit ((int)event.getX(), (int)event.getY(), 200, 2000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOverlayView != null)
            mWindowManager.removeView(mOverlayView);
    }
}
