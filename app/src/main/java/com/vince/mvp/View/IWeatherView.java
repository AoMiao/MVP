package com.vince.mvp.View;

import com.vince.mvp.Model.Weather;

/**
 * Created by Administrator on 2017/3/30.
 */

public interface IWeatherView {
    public void updateWeather(Weather weather);
    public void updateBackGround(String imageCahe);
}
