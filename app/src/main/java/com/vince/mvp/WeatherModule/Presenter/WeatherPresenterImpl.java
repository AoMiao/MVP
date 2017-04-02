package com.vince.mvp.WeatherModule.Presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;

import com.vince.mvp.WeatherModule.Model.HttpUtil;
import com.vince.mvp.WeatherModule.Model.Weather;
import com.vince.mvp.WeatherModule.View.IWeatherView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/30.
 */

public class WeatherPresenterImpl implements IWeatherPresenter {
    private IWeatherView weatherView;
    private android.os.Handler handler;

    public WeatherPresenterImpl(IWeatherView weatherView){
        this.weatherView = weatherView;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void update(String weatherCode, final Context context) {
        String address = "https://free-api.heweather.com/v5/weather?city="+weatherCode+"&key=bc0418b57b2d4918819d3974ac1285d9";
        HttpUtil.sendHttpURL(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String weatherContent = response.body().string();
                final Weather weather = HttpUtil.handleWeatherCode(weatherContent);
                handler.post(new Runnable() {//回到主线程修改UI
                    @Override
                    public void run() {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                        editor.putString("weather", weatherContent);//缓存功能
                        editor.apply();
                        weatherView.updateWeather(weather);
                    }
                });
            }
        });

    }

    @Override
    public void updateBack(final Context context) {
        String imageaddress = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendHttpURL(imageaddress, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String imageCahe = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                        editor.putString("imgCache", imageCahe);//缓存功能
                        editor.apply();
                        weatherView.updateBackGround(imageCahe);
                        //weatherView.colseRefresh();
                    }
                });
            }
        });
    }

    @Override
    public boolean isCahe(Context context) {//判断缓存
        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(context);
        String weatherCache = spf.getString("weather", null);
        final String imgCache = spf.getString("imgCache", null);
        if(weatherCache!=null&&imgCache!=null){
            final Weather weather = HttpUtil.handleWeatherCode(weatherCache);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    weatherView.updateWeather(weather);
                    weatherView.updateBackGround(imgCache);
                }
            });
            return true;
        }
        return false;
    }
}
