package ru.codex.heartbeat;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private Vibrator vibrator;
    private boolean running = false;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        if (Build.VERSION.SDK_INT >= 31) {
            vibrator = ((VibratorManager) getSystemService(Context.VIBRATOR_MANAGER_SERVICE)).getDefaultVibrator();
        } else {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        }
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(32, 32, 32, 32);
        layout.setBackgroundColor(Color.rgb(250, 246, 246));
        TextView title = new TextView(this);
        title.setText("Биение сердца");
        title.setTextSize(28);
        title.setTextColor(Color.rgb(130, 35, 50));
        title.setGravity(Gravity.CENTER);
        layout.addView(title, new LinearLayout.LayoutParams(-1, -2));
        TextView hint = new TextView(this);
        hint.setText("Ритм около 72 ударов в минуту");
        hint.setTextSize(16);
        hint.setGravity(Gravity.CENTER);
        hint.setPadding(0, 12, 0, 28);
        layout.addView(hint, new LinearLayout.LayoutParams(-1, -2));
        Button start = new Button(this);
        start.setText("Старт");
        layout.addView(start, buttonParams());
        Button stop = new Button(this);
        stop.setText("Стоп");
        layout.addView(stop, buttonParams());
        start.setOnClickListener(v -> startHeartbeat());
        stop.setOnClickListener(v -> stopHeartbeat());
        setContentView(layout);
    }

    private LinearLayout.LayoutParams buttonParams() {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.setMargins(0, 8, 0, 8);
        return p;
    }

    private void startHeartbeat() {
        if (running || vibrator == null || !vibrator.hasVibrator()) return;
        running = true;
        long[] pattern = {0, 120, 110, 220, 380};
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0));
        } else {
            vibrator.vibrate(pattern, 0);
        }
    }

    private void stopHeartbeat() {
        running = false;
        if (vibrator != null) vibrator.cancel();
    }

    @Override protected void onStop() {
        stopHeartbeat();
        super.onStop();
    }
}
