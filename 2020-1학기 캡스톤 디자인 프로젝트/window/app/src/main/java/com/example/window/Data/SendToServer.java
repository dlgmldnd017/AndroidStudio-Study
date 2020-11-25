package com.example.window.Data;
import androidx.appcompat.app.AppCompatActivity;
import com.example.window.ui.home.HomeFragment;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class SendToServer extends AppCompatActivity {
    public void sendToServer(){
        try {
            URL url = new URL("http://222.98.176.203/" + HomeFragment.openState);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.getResponseCode();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}