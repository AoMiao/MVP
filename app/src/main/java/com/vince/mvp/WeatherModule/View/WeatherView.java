package com.vince.mvp.WeatherModule.View;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vince.mvp.R;
import com.vince.mvp.WeatherModule.Model.Forecast;
import com.vince.mvp.WeatherModule.Model.Weather;
import com.vince.mvp.WeatherModule.Presenter.IWeatherPresenter;
import com.vince.mvp.WeatherModule.Presenter.WeatherPresenterImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherView extends AppCompatActivity implements IWeatherView {
    private TextView title_city;
    private TextView update_time;
    private TextView tmp_text;
    private TextView weather_info_text;
    private LinearLayout forecast_layout;
    private TextView aqi_text;
    private TextView pm25_text;
    private TextView comfort_text;
    private TextView carwash_text;
    private TextView sport_text;
    private TextView dress_text;
    private Button choose_button;
    private ImageView now_png;
    private ImageView bing_pic_img;
    private SwipeRefreshLayout swipe_refresh;
    public DrawerLayout drawerLayout;
    private String weatherCode;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SharedPreferences spf;

    private IWeatherPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {//android5.0以上把布局充满整个手机屏幕,并把状态栏设置成透明
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.weather_layout);
        initView();
        setListener();
        presenter = new WeatherPresenterImpl(this);
        weatherCode = spf.getString("weather", null);
        if(weatherCode!=null){
            presenter.update(weatherCode,this);
            presenter.updateBack(this);
        }else {
            weatherCode = getIntent().getStringExtra("weatherCode");
            presenter.update(weatherCode,this);
            presenter.updateBack(this);
        }
        /*if(!presenter.isCahe(this)){
            presenter.update("广州",this);
            presenter.updateBack(this);
        }*/
    }


    public void initView() {
        title_city = (TextView) findViewById(R.id.title_city);
        update_time = (TextView) findViewById(R.id.update_time);
        tmp_text = (TextView) findViewById(R.id.tmp_text);
        weather_info_text = (TextView) findViewById(R.id.weather_info_text);
        forecast_layout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqi_text = (TextView) findViewById(R.id.aqi_text);
        pm25_text = (TextView) findViewById(R.id.pm25_text);
        comfort_text = (TextView) findViewById(R.id.comfort_text);
        carwash_text = (TextView) findViewById(R.id.carwash_text);
        sport_text = (TextView) findViewById(R.id.sport_text);
        dress_text = (TextView) findViewById(R.id.dress_text);
        now_png = (ImageView) findViewById(R.id.now_png);
        choose_button = (Button) findViewById(R.id.choose_button);
        bing_pic_img = (ImageView) findViewById(R.id.bing_pic_img);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        swipe_refresh.setColorSchemeResources(R.color.colorPrimary);
        spf = PreferenceManager.getDefaultSharedPreferences(this);
    }



    @Override
    public void updateWeather(Weather weather) {
        title_city.setText(weather.basic.cityName);
        update_time.setText(weather.basic.update.updateTime.split(" ")[1] + "发布");
        tmp_text.setText(weather.now.temperature + "°C");
        weather_info_text.setText(weather.now.weatherMessage.info);
        Glide.with(this).load("http://files.heweather.com/cond_icon/" + weather.now.weatherMessage.code + ".png").into(now_png);
        if (weather.aqi != null) {//有的城市天气没有AQI指数
            aqi_text.setText(weather.aqi.city.aqi);
            pm25_text.setText(weather.aqi.city.pm25);
        }
        comfort_text.setText("舒适度：" + weather.suggestion.comfort.ComfortSuggestion);
        carwash_text.setText("洗车建议：" + weather.suggestion.carWash.CarWashSuggestion);
        sport_text.setText("运动建议：" + weather.suggestion.sport.SportSuggestion);
        dress_text.setText("穿衣建议：" + weather.suggestion.dress.DressSuggestion);

        forecast_layout.removeAllViews();//先把所有的子项清除
        for (Forecast forecast : weather.forecastList) {
            //这里的forecast_layout是指forecast.xml文件里的LinearLayout布局
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecast_layout, false);//加载子项布局
            TextView forecast_date_text = (TextView) view.findViewById(R.id.date_text);
            TextView forecast_info_text = (TextView) view.findViewById(R.id.info_text);
            TextView forecast_max_text = (TextView) view.findViewById(R.id.max_text);
            TextView forecast_min_text = (TextView) view.findViewById(R.id.min_text);
            ImageView png = (ImageView) view.findViewById(R.id.png);
            Glide.with(this).load("http://files.heweather.com/cond_icon/" + forecast.weatherMessage.png + ".png").into(png);
            String today = sdf.format(new Date());
            if (forecast.date.equals(today)) {
                forecast_date_text.setText("今天");
            } else {
                forecast_date_text.setText(forecast.date.split("-")[1] + "月" + forecast.date.split("-")[2] + "日");
            }
            forecast_info_text.setText(forecast.weatherMessage.info);
            forecast_max_text.setText(forecast.temperature.max + "°C");
            forecast_min_text.setText(forecast.temperature.min + "°C");
            forecast_layout.addView(view);
        }
    }

    @Override
    public void updateBackGround(String imageCahe) {
        Glide.with(this).load(imageCahe).into(bing_pic_img);
    }

    @Override
    public void colseRefresh() {
        swipe_refresh.setRefreshing(false);//关闭下拉刷新
    }

    @Override
    public void openRefresh() {
        swipe_refresh.setRefreshing(true);
    }

    public void setListener(){
        choose_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//下拉刷新
            @Override
            public void onRefresh() {
                presenter.update(weatherCode,WeatherView.this);
                presenter.updateBack(WeatherView.this);
            }
        });
    }
    public void ClickFromItself(String weatherCode){//在自己的界面的fragment点击时调用
        this.weatherCode = weatherCode;
        drawerLayout.closeDrawer(GravityCompat.START);
        swipe_refresh.setRefreshing(true);
        presenter.update(weatherCode,WeatherView.this);
        presenter.updateBack(WeatherView.this);
    }
}
