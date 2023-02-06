package com.talzzoft.socialdrinker;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FormActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    public static final String SHARED_PREFS = "sharedPrefs";
    private TextView dateText;
    Button btn_date;
    Button btn_return;
    Button btn_submit;
    Spinner sp_social;
    TextView et_litros;
    CheckBox chk_festive;
    CheckBox chk_drinking;
    String date;
    String g_year;
    String f_days;
    String week_y;
    String g_month;
    Season season;

    Calendar c;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        dateText = findViewById(R.id.tv_date);
        btn_date = findViewById(R.id.btn_date);
        btn_return = findViewById(R.id.btn_return);
        btn_submit = findViewById(R.id.btn_submit);
        sp_social = findViewById(R.id.sp_social);
        et_litros = findViewById(R.id.et_litros);
        chk_festive = findViewById(R.id.chk_festive);
        chk_drinking = findViewById(R.id.chk_drinking);

        date = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        g_year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        g_month = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        String date_text = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        date_text="Date: "+date_text;
        dateText.setText(date_text);
        week_y = getWeekOfYear(date);

        season = new Season(s2i(g_month));

        checkCheckbox(sharedPreferences);

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
                checkCheckbox(sharedPreferences);
            }
        });

        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FormActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(editor, sharedPreferences);
            }
        });

        ArrayAdapter<String> listCitiesAdapter = new ArrayAdapter<String>(FormActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.social_type));
        listCitiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_social.setAdapter(listCitiesAdapter);
    }

    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month++;
        String ms = month+"";
        String ds = dayOfMonth+"";
        String date_text = "Selected date: " + dayOfMonth + "/" + month + "/" + year;
        if(ms.length()<2){
            ms = "0"+month;
        }
        if(ds.length()<2){
            ds = "0"+dayOfMonth;
        }
        date = ""+year+ms+ds;
        g_year = year+"";
        g_month = ms;
        dateText.setText(date_text);
        week_y = getWeekOfYear(date);
    }

    public void submit(SharedPreferences.Editor editor, SharedPreferences sp){

        if( chk_festive.isChecked() && (s2i(sp.getString("w" + g_year + week_y, "0")) >= 2 || s2i(sp.getString("f"+week_y,"0")) >= getLimitFestiveDays())){
            Context context = getApplicationContext();
            CharSequence text = "You have exceeded the number of Festive / Drinking Days for this week";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }else {
            String litros = et_litros.getText() + "";
            if (!litros.equals("") && !litros.equals("0")) {
                editor.putString("l" + date, addString(sp.getString("l" + date, "0"), "0" + et_litros.getText() + ""));
                editor.putString("l" + g_year, addString(sp.getString("l" + g_year, "0"), "0" + et_litros.getText() + ""));
                editor.putString("lw" + g_year + week_y, addString(sp.getString("lw" + g_year + week_y, "0"), "0" + et_litros.getText() + ""));
                editor.putString("lm" + g_year + g_month, addString(sp.getString("lm" + g_year + g_month, "0"), "0" + et_litros.getText() + ""));
                if (!sp.getString(date, "0").equals("1")) {
                    editor.putString("w" + g_year + week_y, addString(sp.getString("w" + g_year + week_y, "0"), "1"));
                    editor.putString("m" + g_year + g_month, addString(sp.getString("m" + g_year + g_month, "0"), "1"));
                    editor.putString(g_year, addString(sp.getString(g_year, "0"), "1"));
                }
                editor.putString(date, "1");
            }
            String social = sp_social.getSelectedItem() + "";
            if (!social.equals("")) {
                if (!sp.getString("s" + date, "0").equals("1")) {
                    editor.putString("sw" + g_year + week_y, addString(sp.getString("sw" + g_year + week_y, "0"), "1"));
                    editor.putString("sm" + g_year + g_month, addString(sp.getString("sm" + g_year + g_month, "0"), "1"));
                    editor.putString("s" + g_year, addString(sp.getString("s" + g_year, "0"), "1"));
                    editor.putString(social + g_year, addString(sp.getString(social + g_year, "0"), "1"));
                }
                editor.putString("s" + date, "1");
            }

            if (chk_festive.isChecked()) {
                if (!sp.getString("f" + date, "0").equals("1")) {
                    editor.putString("f" + g_year, addString(sp.getString("f" + g_year, "0"), "1"));
                    editor.putString("f" + week_y, addString(sp.getString("f" + week_y, "0"), "1"));
                }
                editor.putString("f" + date, "1");
                //editor.putString("f"+week_y, "1");
            } else {
                if (sp.getString("f" + date, "0").equals("1")) {
                    editor.putString("f" + g_year, addString(sp.getString("f" + g_year, "0"), "-1"));
                    editor.putString("f" + week_y, addString(sp.getString("f" + week_y, "0"), "-1"));
                }
                editor.putString("f" + date, "0");
                //editor.putString("f"+week_y, "1");
            }

            if (chk_drinking.isChecked()) {
                if (!sp.getString("d" + date, "0").equals("1")) {
                    editor.putString("d" + g_year, addString(sp.getString("d" + g_year, "0"), "1"));
                }
                Calendar rightNow = Calendar.getInstance();
                int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
                int currentMinutes = rightNow.get(Calendar.MINUTE);
                if (currentMinutes == 59) currentMinutes = 58;
                onTimeSetD(currentHour, currentMinutes + 1, 0);

                onTimeSetD(23, 0, 1);
                onTimeSetD(23, 30, 2);
                onTimeSetD(0, 0, 3);
                onTimeSetD(0, 30, 4);
                onTimeSetD(1, 0, 5);
                onTimeSetD(1, 30, 6);
                onTimeSetD(2, 0, 7);
                onTimeSetD(2, 30, 8);
                onTimeSetD(3, 0, 9);
                onTimeSetD(3, 30, 10);
                onTimeSetD(4, 0, 11);
                onTimeSetD(4, 30, 12);
                onTimeSetD(5, 0, 13);
                onTimeSetD(5, 30, 14);
                onTimeSetD(6, 0, 15);
                onTimeSetD(6, 30, 16);
                onTimeSetD(7, 0, 17);

                editor.putString("d" + date, "1");
            } else {
                if (sp.getString("d" + date, "0").equals("1")) {
                    editor.putString("d" + g_year, addString(sp.getString("f" + g_year, "0"), "-1"));
                }
                editor.putString("d" + date, "0");
                for (int i = 0; i < 18; i++) {
                    cancelAlarm(i);
                }
            }

            editor.commit();
            Intent intent = new Intent(FormActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public String addString(String add1, String add2){
        return (Integer.parseInt(add1) + Integer.parseInt(add2))+"";
    }

    public void checkCheckbox(SharedPreferences sp){
        if(sp.getString("f"+date,"0").equals("1")){
            chk_festive.setChecked(true);
        }else {
            chk_festive.setChecked(false);
        }

        if(sp.getString("d"+date,"0").equals("1")){
            chk_drinking.setChecked(true);
        }else {
            chk_drinking.setChecked(false);
        }
    }

    public static String getWeekOfYear(String inputDate){
        String inputFormat = "yyyyMMdd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(inputFormat);
        Date date = null;
        try {
            date = dateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);

        if(weekDay==1){
            weekDay = 7;
        }else {
            weekDay--;
        }

        //This function is optional in case the calendar started the week in sunday
        if(weekDay == 7){
            weekNumber--;
        }
        weekNumber--;
        return weekNumber+"";
    }

    public void onTimeSetD(int hourOfDay, int minute, int id) {
        c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        startAlarm(id);
    }

    public void startAlarm(int id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        //final int id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm(int id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    public int s2i(String s){
        if(s == null){
            return 0;
        }
        return Integer.parseInt(s);
    }

    public int getLimitFestiveDays(){
        if(g_month.equals("10") || g_month.equals("06") || g_month.equals("02")){
            return 0;
        }else if(g_month.equals("12") || g_month.equals("08") || g_month.equals("04")){
            return 4;
        }
        return  1;
    }
}