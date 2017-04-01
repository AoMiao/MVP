package com.vince.mvp.CityModule.View;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.vince.mvp.CityModule.Model.City;
import com.vince.mvp.CityModule.Model.County;
import com.vince.mvp.CityModule.Model.Province;
import com.vince.mvp.CityModule.Presenter.CityPresenter;
import com.vince.mvp.CityModule.Presenter.CityPresenterImpl;
import com.vince.mvp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/1.
 */

public class ChooseAreaFragment extends Fragment implements FragmentImpl {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private int currentLevel;
    private ProgressDialog progressDialog;

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private Province selectProvince;
    private City selectCity;

    private ArrayList<String> datalist = new ArrayList<>();//显示listview的链表数据
    private ArrayAdapter<String> adapter;
    private ListView listView;

    private Button backbutton;
    private TextView title;

    private CityPresenterImpl presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chose_area, container, false);
        initView(view);
        setListener();
        presenter = new CityPresenter((FragmentImpl) view);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.queryProvince();
    }

    public void initView(View view){
        backbutton = (Button) view.findViewById(R.id.backbutton);//注意这里要通过view才能用findViewById.最外面那层布局
        title = (TextView) view.findViewById(R.id.titlename);
        listView = (ListView) view.findViewById(R.id.listview);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, datalist);
        listView.setAdapter(adapter);
    }

    public void setListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectProvince = provinceList.get(i);
                    presenter.queryCity(selectProvince.getId(),selectProvince.getProvinceCode());
                } else if (currentLevel == LEVEL_CITY) {
                    selectCity = cityList.get(i);
                    presenter.queryCounty();
                } else if (currentLevel == LEVEL_COUNTY) {
                    /*String weatherCode = countyList.get(i).getWeatherCode();

                    if (getActivity() instanceof MainActivity) {
                        Intent intent = new Intent(getActivity(), WeatherAcitivity.class);
                        intent.putExtra("weatherCode", weatherCode);
                        startActivity(intent);
                        getActivity().finish();
                    }else if(getActivity()instanceof WeatherAcitivity){
                        WeatherAcitivity weatherAcitivity = (WeatherAcitivity) getActivity();
                        weatherAcitivity.drawerLayout.closeDrawer(GravityCompat.START);
                        weatherAcitivity.swipe_refresh.setRefreshing(true);
                        weatherAcitivity.weatherCode = weatherCode;
                        weatherAcitivity.queryFormServer(weatherCode);*/
                }
            }
        });
    }


    @Override
    public void SetProvince(List<Province> provinceList) {
        this.provinceList = provinceList;
        title.setText("中国");
        datalist.clear();//先把列表的数据清空！！！
        for (Province p : provinceList) {
            datalist.add(p.getProvinceName());
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        currentLevel = LEVEL_PROVINCE;
    }

    @Override
    public void SetCity(List<City> cityList) {
        this.cityList = cityList;
        title.setText(selectProvince.getProvinceName());
        datalist.clear();
        for (City c : cityList) {
            datalist.add(c.getCityName());
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        currentLevel = LEVEL_CITY;
    }

    @Override
    public void SetCounty() {

    }
}
