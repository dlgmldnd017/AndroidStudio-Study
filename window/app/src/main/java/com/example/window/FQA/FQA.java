package com.example.window.FQA;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.window.R;

public class FQA extends AppCompatActivity {
    Button button1,button2,button3,button4,button5;
    TextView textView1;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fqa);

        textView1 = findViewById(R.id.fqa_textview1);

        button1 = findViewById(R.id.fqa_button1);
        button2 = findViewById(R.id.fqa_button2);
        button3 = findViewById(R.id.fqa_button3);
        button4 = findViewById(R.id.fqa_button4);
        button5 = findViewById(R.id.fqa_button5);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setText("A)\n\n 현재 구현한 메인보드(기기)가 한 개 이기 때문에, 현재 앱에서 제어할 수 있는 기기는 한 개 밖에 없습니다.");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setText("A)\n\n 하단바 맨 오른쪽 앱설정에 있습니다.");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setText("A)\n\n 하단바 오른쪽 3번째 디바이스 설정에 있고, 맨 위 새로고침 버튼을 눌러 위치를 확인되어야 기기 추가를 할 수 있습니다.");
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setText("A)\n\n 자동화는 실시간으로 미세먼지, 비 감지, 가스 감지를 통해 사용자에게 현재 창문의 정보를 전달할 수 있는 기능입니다.");
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setText("A)\n\n 메인보드(기기)를 여러 개 만들어 다중으로 제어할 수 있게 만들 것 입니다.");
            }
        });

    }
}
