package com.vince.mvp.CityModule.Presenter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.vince.mvp.CityModule.Model.City;
import com.vince.mvp.CityModule.Model.County;
import com.vince.mvp.CityModule.Model.Province;
import com.vince.mvp.CityModule.Model.Utility;
import com.vince.mvp.CityModule.View.FragmentImpl;
import com.vince.mvp.WeatherModule.Model.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/1.
 */

public class CityPresenter implements CityPresenterImpl {
    private FragmentImpl fragment;
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private int provinceID;
    private int provinceCode;

    private android.os.Handler handler;

    public CityPresenter(FragmentImpl fragment){
        this.fragment = fragment;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void queryProvince() {
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            fragment.SetProvince(provinceList);
        } else {
            String address = "http://guolin.tech/api/china";
            queryFormServer(address, "province");
        }
    }

    @Override
    public void queryCity(int ProvinceID,int ProvinceCode) {
        this.provinceID = provinceID;
        this.provinceCode = provinceCode;
        cityList = DataSupport.where("provinceId=?", String.valueOf(ProvinceID)).find(City.class);
        if (cityList.size() > 0) {
            fragment.SetCity(cityList);
        } else {
            String address = "http://guolin.tech/api/china/" + ProvinceCode;
            queryFormServer(address, "city");
        }
    }

    @Override
    public void queryCounty() {

    }

    public void queryFormServer(String address, final String type) {
        //showProgressDialog();
        HttpUtil.sendHttpURL(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //closeProgressDialog();
                Log.w(fragment.toString(), "错误");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Boolean flag = false;
                String data = response.body().string();//获取服务器返回的数据
                if (type.equals("province")) {
                    flag = Utility.handleProvinceResponse(data);//解析数据把省数据存到本地数据库
                } else if (type.equals("city")) {//这里的ID是省类的id字段，不是code
                    flag = Utility.handleCityResponse(data, provinceID);//先把城市存到本地，还把省id联系起来
                } else if (type.equals("county")) {
                    //flag = Utility.handleCountyResponse(data, selectCity.getId());
                }
                if (flag) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (type.equals("province")) {
                                queryProvince();
                            } else if (type.equals("city")) {
                                queryCity(provinceID,provinceCode);
                            } else if (type.equals("county")) {
                                //queryCounty();
                            }
                        }
                    });
                }
            }
        });

    }
}


