package com.vince.mvp.WeatherModule.Presenter;

import android.content.Context;

/**
 * Created by Administrator on 2017/3/30.
 */

public interface IWeatherPresenter {
    void update(String weatherCode, Context context);
    void updateBack(Context context);
    boolean isCahe(Context context);
}
