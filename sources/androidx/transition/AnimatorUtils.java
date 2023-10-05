package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

/* loaded from: classes.dex */
public class AnimatorUtils {

    /* loaded from: classes.dex */
    interface AnimatorPauseListenerCompat {
        void onAnimationPause(Animator animator);

        void onAnimationResume(Animator animator);
    }

    public static void addPauseListener(Animator animator, AnimatorListenerAdapter animatorListenerAdapter) {
        animator.addPauseListener(animatorListenerAdapter);
    }

    public static void pause(Animator animator) {
        animator.pause();
    }

    public static void resume(Animator animator) {
        animator.resume();
    }

    private AnimatorUtils() {
    }
}
