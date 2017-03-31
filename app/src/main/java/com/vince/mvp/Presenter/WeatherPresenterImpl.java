package com.vince.mvp.Presenter;

import android.os.Handler;
import android.os.Looper;

import com.vince.mvp.Model.Utilily;
import com.vince.mvp.Model.Weather;
import com.vince.mvp.View.IWeatherView;
import com.vince.mvp.View.WeatherView;

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

    public WeatherPresenterImpl(WeatherView weatherView){
        this.weatherView = weatherView;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void update(String weatherCode) {
        String address = "https://free-api.heweather.com/v5/weather?city="+weatherCode+"&key=bc0418b57b2d4918819d3974ac1285d9";
        Utilily.sendHttpURL(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final Weather weather = Utilily.handleWeatherCode(response.body().string());
                handler.post(new Runnable() {//回到主线程修改UI
                    @Override
                    public void run() {
                        weatherView.updateWeather(weather);
                    }
                });
            }
        });

    }

    @Override
    public void updateBack() {
        String imageaddress = "http://guolin.tech/api/bing_pic";
        Utilily.sendHttpURL(imageaddress, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String imageCahe = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        weatherView.updateBackGround(imageCahe);
                    }
                });
            }
        });
    }
}
