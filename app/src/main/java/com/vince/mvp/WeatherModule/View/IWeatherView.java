package com.vince.mvp.WeatherModule.View;

import com.vince.mvp.WeatherModule.Model.Weather;

/**
 * Created by Administrator on 2017/3/30.
 */

public interface IWeatherView {
    public void updateWeather(Weather weather);
    public void updateBackGround(String imageCahe);
    public void colseRefresh();
}
