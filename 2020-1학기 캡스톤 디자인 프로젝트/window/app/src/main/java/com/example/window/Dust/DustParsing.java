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

public class DustParsing extends AppCompatActivity {
    DeviceSettingFragment deviceSettingFragment = new DeviceSettingFragment();
    String address = deviceSettingFragment.address; //현재 위치 정보를 가져오는 변수
    Document document;
    Elements elements;

    public void getDust10Api(){
        String[] arr = address.split(" ");
        String path = "http://search.naver.com/search.naver?sm=top_hty&fbm=0&ie=utf8&query="+ arr[0] + "+" +arr[1] + "+" + arr[2] + "+" + arr[3] + "+"+arr[4]+"+날씨";

        try {
            document = Jsoup.connect(path).get();
            elements = document.select("span.num");
            int cnt = 0;
            for(Element element: elements){
                if(cnt == 4){
                    HomeFragment.dust10 = "미세먼지: " + element.text() + "\n";
                    break;
                }
                cnt++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void getDust25Api(){
        String[] arr = address.split(" ");
        String path = "http://search.naver.com/search.naver?sm=top_hty&fbm=0&ie=utf8&query="+ arr[0] + "+" +arr[1] + "+" + arr[2] + "+" + arr[3] + "+"+arr[4]+"+날씨";

        try {
            document = Jsoup.connect(path).get();
            elements = document.select("span.num");
            int cnt = 0;
            for(Element element: elements){
                if(cnt == 5){
                    HomeFragment.dust25 = "초미세먼지: " + element.text() + "\n";
                    break;
                }
                cnt++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
