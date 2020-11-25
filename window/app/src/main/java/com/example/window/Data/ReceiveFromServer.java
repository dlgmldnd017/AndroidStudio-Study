package com.example.window.Data;

import androidx.appcompat.app.AppCompatActivity;

import com.example.window.Adapter.Window;
import com.example.window.ui.home.HomeFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ReceiveFromServer extends AppCompatActivity {

    public void receiveToServer(){
        String path = "http://222.98.176.203";
        try {
            Document document = Jsoup.connect(path).get();
            Elements elements = document.select("body");
            for(Element element: elements){
                Window.Estimated_dust[0] = element.text();
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
