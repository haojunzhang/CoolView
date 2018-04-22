package idv.haojun.aplayer.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import idv.haojun.aplayer.R;


public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().registerActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.getInstance().unregisterActivity(this);
    }

    protected void initTitle(int titleId) {
        initTitle(R.id.tv_toolbar_tittle, getString(titleId));
    }

    protected void initTitle(String title) {
        initTitle(R.id.tv_toolbar_tittle, title);
    }

    protected void initTitle(int tvId, int titleId) {
        initTitle(tvId, getString(titleId));
    }

    protected void initTitle(int tvId, String title) {
        TextView tv = findViewById(tvId);
        tv.setText(title);
    }
}
