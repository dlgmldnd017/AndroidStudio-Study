package com.example.window.Dust;

import androidx.appcompat.app.AppCompatActivity;

import com.example.window.Adapter.Window;
import com.example.window.ui.deviceSetting.DeviceSettingFragment;
import com.example.window.ui.home.HomeFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WeatherApiParsing extends AppCompatActivity {
    DeviceSettingFragment deviceSettingFragment = new DeviceSettingFragment();
    String address1 = deviceSettingFragment.address;  //위치 정보를 가져오는 변수
    Document document;
    Elements elements;

    public void tempGetApi(){
        String[] arr = address1.split(" ");
        String path = "http://search.naver.com/search.naver?sm=top_hty&fbm=0&ie=utf8&query="+ arr[0] + "+" +arr[1] + "+" + arr[2] + "+" + arr[3] + "+"+arr[4]+"+날씨";
        try {
            document = Jsoup.connect(path).get();
            elements = document.select("span.todaytemp");
            for(Element element: elements){
                HomeFragment.tempData = element.text() + "℃";
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void weatherGetApi(){
        String[] arr = address1.split(" ");
        String path = "http://search.naver.com/search.naver?sm=top_hty&fbm=0&ie=utf8&query="+ arr[0] + "+" +arr[1] + "+" + arr[2] + "+" + arr[3] + "+"+arr[4]+"+날씨";
        try {
            document = Jsoup.connect(path).get();
            elements = document.select("p.cast_txt");
            for(Element element: elements){
                HomeFragment.weatherData = element.text() + "\n";
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
