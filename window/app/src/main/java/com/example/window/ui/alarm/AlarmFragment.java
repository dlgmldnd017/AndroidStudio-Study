package com.example.window.ui.alarm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.window.Adapter.Window;
import com.example.window.MainActivity;
import com.example.window.R;
import com.example.window.ui.deviceSetting.DeviceSettingFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class AlarmFragment extends Fragment implements Comparator<Integer> {
    private AlarmViewModel dashboardViewModel;
    public static TextView textView;
    public static int index = 0;
    TimePicker timePicker;
    Button button1,button2, button3;
    public static int i = DeviceSettingFragment.i, hour, min;
    String day, parseMin;

    public static int now_device = 0;
    public static List<String> reserved_data1 = new ArrayList<>();
    public static List<String> reserved_data2 = new ArrayList<>();
    public static List<String> reserved_data3 = new ArrayList<>();

    public static List<Integer> reserving_data1 = new ArrayList<>();
    public static List<Integer> reserving_data2 = new ArrayList<>();
    public static List<Integer> reserving_data3 = new ArrayList<>();

    public static ArrayAdapter<String> arrayAdapter1;
    public static ArrayAdapter<String> arrayAdapter2;
    public static ArrayAdapter<String> arrayAdapter3;

    public static ListView listView1;
    public static ListView listView2;
    public static ListView listView3;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);
        View root = inflater.inflate(R.layout.fragment_alarm, container, false);

        listView1 = root.findViewById(R.id.alarm_listView1);
        listView2 = root.findViewById(R.id.alarm_listView2);
        listView3 = root.findViewById(R.id.alarm_listView3);

        timePicker = root.findViewById(R.id.time_picker);

        textView = root.findViewById(R.id.alarm_textview1);
        textView.setText(Window.name[now_device]);

        if(now_device == 0){
            arrayAdapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice,reserved_data1);
            listView1.setVisibility(View.VISIBLE);
            listView2.setVisibility(View.INVISIBLE);
            listView3.setVisibility(View.INVISIBLE);
            listView1.setAdapter(arrayAdapter1);
            textView.setText(Window.name[now_device]);

        }else if(now_device == 1){
            arrayAdapter2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice,reserved_data2);
            listView1.setVisibility(View.INVISIBLE);
            listView2.setVisibility(View.VISIBLE);
            listView3.setVisibility(View.INVISIBLE);
            listView2.setAdapter(arrayAdapter2);
            textView.setText(Window.name[now_device]);

        }else if(now_device == 2) {
            arrayAdapter3 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice,reserved_data3);
            listView1.setVisibility(View.INVISIBLE);
            listView2.setVisibility(View.INVISIBLE);
            listView3.setVisibility(View.VISIBLE);
            listView3.setAdapter(arrayAdapter3);
            textView.setText(Window.name[now_device]);

        }

        button1 = root.findViewById(R.id.alarm_button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DeviceSettingFragment.i == 0) {
                    Toast.makeText(getContext(), "기기 설정을 해주세요.",Toast.LENGTH_LONG).show();
                } else {
                    final MainActivity mainActivity = (MainActivity) getActivity();
                    AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                    builder.setTitle("예약");
                    builder.setMessage("\""+textView.getText().toString()+"\"" + " 기기를 예약 하시겠습니까?");
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        String name;
                                        int cal_hour_mul_min;
                                        hour = timePicker.getHour();
                                        min = timePicker.getMinute();
                                        cal_hour_mul_min = (hour * 60) + min;
                                        if (hour >= 12) {
                                            day = "오후";
                                            if (hour > 12) {

                                                hour = hour - 12;
                                            }
                                        } else {
                                            day = "오전";
                                        }
                                        if (min < 10) {
                                            parseMin = "0" + min;
                                        } else {
                                            parseMin = "" + min;
                                        }
                                        name = day + " " + hour + ":" + parseMin;
                                        if (now_device == 0) {
                                            arrayAdapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice, reserved_data1);
                                            listView1.setAdapter(arrayAdapter1);

                                            reserved_data1.add(name);

                                            HashSet<String> arr = new HashSet<>(reserved_data1);
                                            ArrayList<String> arr1 = new ArrayList<>(arr);
                                            reserved_data1 = arr1;
                                            Collections.sort(reserved_data1);
                                            arrayAdapter1.notifyDataSetChanged();


                                            reserving_data1.add(cal_hour_mul_min);
                                            Collections.sort(reserving_data1);

                                            HashSet<Integer> arr2 = new HashSet<>(reserving_data1);
                                            ArrayList<Integer> arr3 = new ArrayList<>(arr2);
                                            reserving_data1 = arr3;

                                            listView1.setAdapter(arrayAdapter1);
                                        } else if (now_device == 1) {
                                            arrayAdapter2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice, reserved_data2);
                                            listView2.setAdapter(arrayAdapter2);

                                            reserved_data2.add(name);

                                            HashSet<String> arr = new HashSet<>(reserved_data2);
                                            ArrayList<String> arr1 = new ArrayList<>(arr);
                                            reserved_data2 = arr1;
                                            Collections.sort(reserved_data2);

                                            reserving_data2.add(cal_hour_mul_min);
                                            Collections.sort(reserving_data2);

                                            HashSet<Integer> arr2 = new HashSet<>(reserving_data2);
                                            ArrayList<Integer> arr3 = new ArrayList<>(arr2);
                                            reserving_data2 = arr3;
                                            arrayAdapter2.notifyDataSetChanged();

                                            listView1.setAdapter(arrayAdapter2);


                                        } else if (now_device == 2) {
                                            arrayAdapter3 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice, reserved_data3);
                                            listView3.setAdapter(arrayAdapter3);

                                            reserved_data3.add(name);

                                            HashSet<String> arr = new HashSet<>(reserved_data3);
                                            ArrayList<String> arr1 = new ArrayList<>(arr);
                                            reserved_data3 = arr1;
                                            Collections.sort(reserved_data3);
                                            arrayAdapter3.notifyDataSetChanged();

                                            reserving_data3.add(cal_hour_mul_min);
                                            Collections.sort(reserving_data3);

                                            HashSet<Integer> arr2 = new HashSet<>(reserving_data3);
                                            ArrayList<Integer> arr3 = new ArrayList<>(arr2);
                                            reserving_data3 = arr3;

                                            listView3.setAdapter(arrayAdapter3);
                                        }

                                        Toast.makeText(getContext(), "설정 완료", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getContext(), "설정 실패", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                reserved_data1.remove(position);
                                arrayAdapter1.notifyDataSetChanged();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                new AlertDialog.Builder(getContext())
                        .setMessage("삭제하시겠습니까?")
                        .setPositiveButton("예", onClickListener)
                        .setNegativeButton("아니오", onClickListener)
                        .show();
                return false;
            }
        });

        listView2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                reserved_data2.remove(position);
                                arrayAdapter2.notifyDataSetChanged();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                new AlertDialog.Builder(getContext())
                        .setMessage("삭제하시겠습니까?")
                        .setPositiveButton("예", onClickListener)
                        .setNegativeButton("아니오", onClickListener)
                        .show();
                return false;
            }
        });

        listView3.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                reserved_data3.remove(position);
                                arrayAdapter3.notifyDataSetChanged();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                new AlertDialog.Builder(getContext())
                        .setMessage("삭제하시겠습니까?")
                        .setPositiveButton("예", onClickListener)
                        .setNegativeButton("아니오", onClickListener)
                        .show();
                return false;
            }
        });

        button2 = root.findViewById(R.id.alarm_button2);
        button2.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           if (now_device == 0) {
                                               SparseBooleanArray array = listView1.getCheckedItemPositions();
                                               for(int i=0;i<array.size();i++){
                                                   for(int j=0;j<reserved_data1.size();j++){
                                                       if(array.get(j)){
                                                           reserved_data1.remove(j);
                                                           reserving_data1.remove(j);
                                                           break;
                                                       }
                                                   }
                                               }
                                               arrayAdapter1.notifyDataSetChanged();
                                               listView1.clearChoices();
                                           } else if (now_device == 1) {
                                               SparseBooleanArray array = listView2.getCheckedItemPositions();
                                               for(int i=0;i<array.size();i++){
                                                   for(int j=0;j<reserved_data2.size();j++){
                                                       if(array.get(j)){
                                                           reserved_data2.remove(j);
                                                           reserving_data2.remove(j);
                                                           break;
                                                       }
                                                   }
                                               }
                                               arrayAdapter2.notifyDataSetChanged();
                                               listView2.clearChoices();
                                           } else if (now_device == 2) {
                                               SparseBooleanArray array = listView3.getCheckedItemPositions();
                                               for(int i=0;i<array.size();i++){
                                                   for(int j=0;j<reserved_data3.size();j++){
                                                       if(array.get(j)){
                                                           reserved_data3.remove(j);
                                                           reserving_data3.remove(j);
                                                           break;
                                                       }
                                                   }
                                               }
                                               arrayAdapter3.notifyDataSetChanged();
                                               listView3.clearChoices();
                                           }
                                       }
                                   });

        button3 = root.findViewById(R.id.alarm_button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (i){
                    case 1:
                        textView.setText(Window.name[i-1]);
                        now_device = i-1;
                        if(DeviceSettingFragment.count == 3 && index==0){
                            index++;
                        }
                        if(DeviceSettingFragment.count-index == 2){
                            i++;
                        }
                        listView1.setVisibility(View.VISIBLE);
                        listView2.setVisibility(View.INVISIBLE);
                        listView3.setVisibility(View.INVISIBLE);
                        listView1.setAdapter(arrayAdapter1);
                        break;
                    case 2:
                        textView.setText(Window.name[i-1]);
                        now_device = i-1;
                        if(DeviceSettingFragment.count == 3){
                            i++;
                        }else{
                            i=1;
                        }
                        listView1.setVisibility(View.INVISIBLE);
                        listView2.setVisibility(View.VISIBLE);
                        listView3.setVisibility(View.INVISIBLE);
                        listView2.setAdapter(arrayAdapter2);
                        break;
                    case 3:
                        textView.setText(Window.name[i-1]);
                        now_device = i-1;

                        if(index ==0){
                            index++;
                        }
                        i=1;
                        listView1.setVisibility(View.INVISIBLE);
                        listView2.setVisibility(View.INVISIBLE);
                        listView3.setVisibility(View.VISIBLE);
                        listView3.setAdapter(arrayAdapter3);
                }
            }
        });
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public int compare(Integer a, Integer b) {
        return b.compareTo(a);
    }
}