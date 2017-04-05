package com.vince.mvp.CityModule.Presenter;

/**
 * Created by Administrator on 2017/4/1.
 */

public interface CityPresenterImpl {
    void queryProvince();

    void queryCity(int ProvinceID, int ProvinceCode);

    void queryCounty(int CityID, int CityCode);

    void showDialog();

    void closeDialog();
}
