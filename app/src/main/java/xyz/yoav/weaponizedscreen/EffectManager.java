package xyz.yoav.weaponizedscreen;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Switch;

import androidx.core.content.res.ResourcesCompat;

import com.plattysoft.leonids.ParticleSystem;

public class EffectManager {

    private static int numParticles = 80;
    private static long timeToLive = 1000;
    static Drawable particle1,
                    particle2;

    enum Effect {simpleParticle1, simpleParticle2 }
    public static Effect currentSelectedEffect = Effect.simpleParticle2;

    public static void initializeDrawables(Resources res) {
        particle1 = ResourcesCompat.getDrawable(res, R.drawable.particle, null);
        particle2 = ResourcesCompat.getDrawable(res, R.drawable.star_white_border, null);
    }

    public static void playCurrentEffect(ViewGroup viewGroup, int x, int y) {
        switch (currentSelectedEffect) {
            case simpleParticle1: simpleEffect1(viewGroup).emit (x, y, 200, 2000);
                break;
            case simpleParticle2: simpleEffect2(viewGroup).emit (x, y, 200, 200);
                break;
        }
    }

    static ParticleSystem simpleEffect1(ViewGroup viewGroup) {
        return new ParticleSystem(viewGroup, numParticles, particle1 , timeToLive)
                .setSpeedRange(0.2f, 0.5f);
    }

    static ParticleSystem simpleEffect2(ViewGroup viewGroup) {
        return new ParticleSystem(viewGroup, 100, particle2, 800)
        .setScaleRange(0.7f, 1.3f)
        .setSpeedRange(0.1f, 0.25f)
        .setAcceleration(0.0001f, 90)
        .setRotationSpeedRange(90, 180)
        .setFadeOut(200, new AccelerateInterpolator());
    }

}
