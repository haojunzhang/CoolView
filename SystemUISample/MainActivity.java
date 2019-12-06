package idv.haojun.screensample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int originVisibility = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        originVisibility = decorView.getSystemUiVisibility();

        findViewById(R.id.btn_hide_status_bar).setOnClickListener(this);
        findViewById(R.id.btn_hide_navigation_bar).setOnClickListener(this);
        findViewById(R.id.btn_hide_all).setOnClickListener(this);
        findViewById(R.id.btn_show_all).setOnClickListener(this);

    }

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void hideNavigationBar() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void hideAllSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | 
            View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    private void showAllSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(originVisibility);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_hide_status_bar:
                hideStatusBar();
                break;
            case R.id.btn_hide_navigation_bar:
                hideNavigationBar();
                break;
            case R.id.btn_hide_all:
                hideAllSystemUI();
                break;
            case R.id.btn_show_all:
                showAllSystemUI();
                break;
        }
    }
}
