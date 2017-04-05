package com.vince.mvp.CityModule.View;

import com.vince.mvp.CityModule.Model.City;
import com.vince.mvp.CityModule.Model.County;
import com.vince.mvp.CityModule.Model.Province;

import java.util.List;

/**
 * Created by Administrator on 2017/4/1.
 */

public interface FragmentImpl {
    public void SetProvince(List<Province> provinceList);
    public void SetCity(List<City> cityList);
    public void SetCounty(List<County> countyList);
    public void showProgressDialog();
    public void closeProgressDialog();
}
