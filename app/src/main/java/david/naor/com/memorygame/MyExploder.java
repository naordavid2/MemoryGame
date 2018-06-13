package david.naor.com.memorygame;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

import tyrantgit.explosionfield.ExplosionField;
import tyrantgit.explosionfield.Utils;

public class MyExploder extends View{
    private ExplosionField explosionField;
    private long duration;
    private int[] mExpandInset = new int[2];

    public MyExploder(Context context) {
        super(context);
    }

    public MyExploder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyExploder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setExplosionField(ExplosionField explosionField) {
        this.explosionField = explosionField;
    }

    public void explode(final View view){

        Rect r = new Rect();
        view.getGlobalVisibleRect(r);
        int[] location = new int[2];
        getLocationOnScreen(location);
        r.offset(-location[0], -location[1]);
        r.inset(-mExpandInset[0], -mExpandInset[1]);
        int startDelay = 100;
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(150);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            Random random = new Random();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTranslationX((random.nextFloat() - 0.5f) * view.getWidth() * 0.05f);
                view.setTranslationY((random.nextFloat() - 0.5f) * view.getHeight() * 0.05f);

            }
        });
        animator.start();
        view.animate().setDuration(150).setStartDelay(startDelay).scaleX(0f).scaleY(0f).alpha(0f).start();
        explosionField.explode(Utils.createBitmapFromView(view), r, startDelay, duration);

    }
}
