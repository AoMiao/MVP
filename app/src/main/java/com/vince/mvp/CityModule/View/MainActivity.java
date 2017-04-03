package com.vince.mvp.CityModule.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.vince.mvp.R;
import com.vince.mvp.WeatherModule.View.WeatherView;

/**
 * Created by Administrator on 2017/4/1.
 */

public class MainActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherCache = spf.getString("weather", null);//获取缓存信息
        if (weatherCache != null) {//有缓存数据直接跳转天气界面
            Intent intent = new Intent(MainActivity.this, WeatherView.class);
            startActivity(intent);
            finish();
        }
    }
}
