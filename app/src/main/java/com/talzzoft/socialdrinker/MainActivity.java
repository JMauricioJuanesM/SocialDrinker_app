package com.talzzoft.socialdrinker;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    public static final String SHARED_PREFS = "sharedPrefs";

    Button btn_tracker;
    Button btn_date;
    TextView tv_year_days;
    TextView tv_festive_days;
    TextView tv_litros;
    TextView tv_week_days;
    TextView tv_month_days;
    TextView tv_dry_month;
    TextView tv_year_litros;
    TextView tv_month_litros;
    TextView tv_week_litros;
    TextView tv_year_social;
    TextView tv_month_social;
    TextView tv_week_social;
    TextView tv_friends_days;
    TextView tv_family_days;
    TextView tv_mate_days;
    TextView tv_weekend_plan_1;
    TextView tv_weekend_plan_2;
    TextView tv_weekend_plan_3;
    TextView tv_weekend_plan_4;
    TextView tv_weekend_plan_5;
    TextView tv_weekend_plan_6;
    TextView tv_date_text;

    ImageView img_social;

    String date;
    String month;
    String week_y;
    String day_week;
    String year;
    String wDays;
    String mDays;
    String yLitros;
    String wLitros;
    String mLitros;
    String ySocial;
    String wSocial;
    String mSocial;
    String yDays;
    String fDays;
    String lDay;
    String g_year;
    String g_month;

    int tLitros = 2;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        month = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        day_week = getWeekDay();
        date = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        week_y = FormActivity.getWeekOfYear(date);
        g_year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        g_month = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());

        lDay = sharedPreferences.getString("l"+date,"0");

        yDays = sharedPreferences.getString(year,"0");
        fDays = sharedPreferences.getString("f"+year,"0");
        wDays = sharedPreferences.getString("w"+year+week_y,"0");
        mDays = sharedPreferences.getString("m"+year+month,"0");

        yLitros = sharedPreferences.getString("l"+year,"0");
        wLitros = sharedPreferences.getString("lw"+year+week_y,"0");
        mLitros = sharedPreferences.getString("lm"+year+month,"0");

        ySocial = sharedPreferences.getString("s"+year,"0");
        wSocial = sharedPreferences.getString("sw"+year+week_y,"0");
        mSocial = sharedPreferences.getString("sm"+year+month,"0");

        btn_tracker = findViewById(R.id.btn_tracker);
        btn_date = findViewById(R.id.btn_date);
        tv_date_text = findViewById(R.id.tv_date);
        tv_year_days = findViewById(R.id.a5);
        tv_festive_days = findViewById(R.id.a1);
        tv_litros = findViewById(R.id.tv_litros);
        tv_week_days = findViewById(R.id.a3);
        tv_month_days = findViewById(R.id.a4);
        tv_dry_month = findViewById(R.id.a6);
        tv_week_litros = findViewById(R.id.a2);
        tv_month_litros = findViewById(R.id.a7);
        tv_year_litros = findViewById(R.id.a8);
        tv_week_social = findViewById(R.id.a11);
        tv_month_social = findViewById(R.id.a12);
        tv_year_social = findViewById(R.id.a13);
        tv_family_days = findViewById(R.id.c1);
        tv_friends_days = findViewById(R.id.c2);
        tv_mate_days = findViewById(R.id.c3);
        tv_weekend_plan_1 = findViewById(R.id.b1);
        tv_weekend_plan_2 = findViewById(R.id.b3);
        tv_weekend_plan_3 = findViewById(R.id.b4);
        tv_weekend_plan_4 = findViewById(R.id.b2);
        tv_weekend_plan_5 = findViewById(R.id.b7);
        tv_weekend_plan_6 = findViewById(R.id.b8);
        img_social = findViewById(R.id.img_social);

        tv_litros.setText("<2L");

        tv_year_days.setText("Year days: "+yDays+"/80");
        tv_festive_days.setText("Festive Days: "+fDays+"/80");
        tv_week_days.setText("Week days: "+wDays+"/2");
        tv_month_days.setText("Month days: "+mDays+"/8");

        tv_year_litros.setText("Year L: "+yLitros+"/160L");
        tv_week_litros.setText("Week L: "+wLitros+"/4L");
        tv_month_litros.setText("Month L: "+mLitros+"/16L");

        tv_year_social.setText("Year days: "+ySocial+"/125");
        tv_week_social.setText("Week days: "+wSocial+"/3");
        tv_month_social.setText("Month days: "+mSocial+"/12");

        String date_text = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        tv_date_text.setText("Date: "+date_text);


        checkWeekDay(sharedPreferences);
        checkSocial(sharedPreferences);
        checkRestrictions(sharedPreferences);
        checkCalendar();
        checkFestiveDay(sharedPreferences);
        checkDryMonth(sharedPreferences);

        btn_tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, FormActivity.class);
                startActivity(intent);
            }
        });

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

    }

    public void checkFestiveDay(SharedPreferences sp){
        if(sp.getString("f"+date,"0").equals("1")){
            if(tLitros==2){
                tv_litros.setText("<4L");
                tLitros = 4;
            }else{
                tv_litros.setText("<1L");
                tLitros=1;
            }
        }
    }


    public void checkDryMonth(SharedPreferences sp){
        if(month.equals("10") || month.equals("06") || month.equals("02")){
            tv_dry_month.setText("Dry month: YES");
            tv_litros.setText("X");
            tLitros = 0;
            /*if(sp.getString("f"+date,"0").equals("1")){
                tv_litros.setText("<1L");
                tLitros = 1;
            }*/
        }
    }

    public void checkWeekDay(SharedPreferences sp){
        if((!day_week.equals("7") && !day_week.equals("6") && !day_week.equals("5") && !day_week.equals("4")) || s2i(sp.getString("f"+week_y,"0")) > 0){
            tv_litros.setText("X");
            tLitros = 0;
            if(sp.getString("f"+date,"0").equals("1")){
                tv_litros.setText("<1L");
                tLitros = 1;
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void checkSocial(SharedPreferences sp){
        int family = s2i(sp.getString("Family"+year,"0"));
        int friends = s2i(sp.getString("Friends"+year,"0"));
        int mate = s2i(sp.getString("Date/Mate"+year,"0"));
        String min = "";

        tv_family_days.setText("Family days: "+family);
        tv_friends_days.setText("Friends days: "+friends);
        tv_mate_days.setText("Mate days: "+mate);

        if(family <= friends){
            min = "Family";
            if(mate < family){
                min = "Date/Mate";
            }
        }else {
            min = "Friends";
            if(mate < friends){
                min = "Date/Mate";
            }
        }

        if((!day_week.equals("7") && !day_week.equals("6") && !day_week.equals("5") && !day_week.equals("4")) && sp.getString("f"+date,"0").equals("0")){
            img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_code_svgrepo_com));
        }else if(min.equals("Family")){
            img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_family_svgrepo_com));
        }else if(min.equals("Friends")){
            img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_man_svgrepo_com));
        }else{
            img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_woman_profile_svgrepo_com));
        }

    }

    public void checkRestrictions(SharedPreferences sp) {
        if (s2i(yDays) >= 80){
            tv_litros.setText("X");
            tLitros = 0;
        }if (s2i(fDays) >= 80){
            tv_litros.setText("X");
            tLitros = 0;
        }if (s2i(wDays) >= 2){
            tv_litros.setText("X");
            tLitros = 0;
        }if (s2i(mDays) >= 8){
            tv_litros.setText("X");
            tLitros = 0;
        }if (s2i(yLitros) >= 160){
            tv_litros.setText("X");
            tLitros = 0;
        }if (s2i(wLitros) >= 4){
            tv_litros.setText("X");
            tLitros = 0;
        }if (s2i(mLitros) >= 16){
            tv_litros.setText("X");
            tLitros = 0;
        }if(tLitros <= s2i(lDay)){
            tv_litros.setText("X");
            tLitros = 0;
        }
    }

    public void checkCalendar(){
        switch (checkWeekendPlan()) {
            case 1:
                tv_weekend_plan_1.setTextColor(Color.RED);
                if(s2i(day_week)==4){
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_woman_profile_svgrepo_com));
                    tLitros = 2;
                }else if(s2i(day_week)==5){
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_code_svgrepo_com));
                    tv_litros.setText("X");
                    tLitros = 0;
                }else if(s2i(day_week)==6){
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_code_svgrepo_com));
                    tv_litros.setText("X");
                    tLitros = 0;
                }else if(s2i(day_week)==7){
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_family_svgrepo_com));
                    tLitros = 2;
                }
                break;
            case 2:
                tv_weekend_plan_2.setTextColor(Color.RED);
                if(s2i(day_week)==4){
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_woman_profile_svgrepo_com));
                    tLitros = 2;
                }else if(s2i(day_week)==5){
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_man_svgrepo_com));
                    tLitros = 2;
                }else if(s2i(day_week)==6){
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_code_svgrepo_com));
                    tv_litros.setText("X");
                    tLitros = 0;
                }else if(s2i(day_week)==7){
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_code_svgrepo_com));
                    tv_litros.setText("X");
                    tLitros = 0;
                }
                break;
            case 3:
                tv_weekend_plan_3.setTextColor(Color.RED);
                if(s2i(day_week)==4){
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_man_svgrepo_com));
                    tLitros = 2;
                }else if(s2i(day_week)==5){
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_code_svgrepo_com));
                    tv_litros.setText("X");
                    tLitros = 0;
                }else if(s2i(day_week)==6){
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_woman_profile_svgrepo_com));
                    tLitros = 2;
                }else if(s2i(day_week)==7) {
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_code_svgrepo_com));
                    tv_litros.setText("X");
                    tLitros = 0;
                }
                break;
            case 4:
                tv_weekend_plan_4.setTextColor(Color.RED);
                if(s2i(day_week)==4){
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_woman_profile_svgrepo_com));
                    tLitros = 2;
                }else if(s2i(day_week)==5){
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_code_svgrepo_com));
                    tv_litros.setText("X");
                    tLitros = 0;
                }else if(s2i(day_week)==6){
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_code_svgrepo_com));
                    tv_litros.setText("X");
                    tLitros = 0;
                }else if(s2i(day_week)==7) {
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_family_svgrepo_com));
                    tLitros = 2;
                }
                break;
            case 5:
                tv_weekend_plan_5.setTextColor(Color.RED);
                if(s2i(day_week)==4){
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_man_svgrepo_com));
                    tLitros = 2;
                }else if(s2i(day_week)==5){
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_woman_profile_svgrepo_com));
                    tLitros = 2;
                }else if(s2i(day_week)==6){
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_code_svgrepo_com));
                    tv_litros.setText("X");
                    tLitros = 0;
                }else if(s2i(day_week)==7) {
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_code_svgrepo_com));
                    tv_litros.setText("X");
                    tLitros = 0;
                }
                break;
            case 6:
                tv_weekend_plan_6.setTextColor(Color.RED);
                if(s2i(day_week)==4){
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_woman_profile_svgrepo_com));
                    tLitros = 2;
                }else if(s2i(day_week)==5){
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_code_svgrepo_com));
                    tv_litros.setText("X");
                    tLitros = 0;
                }else if(s2i(day_week)==6){
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_man_svgrepo_com));
                    tLitros = 2;
                }else if(s2i(day_week)==7) {
                    img_social.setImageDrawable(getResources().getDrawable(R.drawable.ic_code_svgrepo_com));
                    tv_litros.setText("X");
                    tLitros = 0;
                }
                break;
            default:
                break;
        }

    }

    public int checkWeekendPlan(){
        int wy = s2i(week_y);
        if(wy==1 || wy==7 || wy==13 || wy==19 || wy==25 || wy==31 || wy==37 || wy==43 || wy==49){
            return 1;
        }else if(wy==2 || wy==8 || wy==14 || wy==20 || wy==26 || wy==32 || wy==38 || wy==44 || wy==50){
            return 2;
        }else if(wy==3 || wy==9 || wy==15 || wy==21 || wy==27 || wy==33 || wy==39 || wy==45 || wy==51){
            return 3;
        }else if(wy==4 || wy==10 || wy==16 || wy==22 || wy==28 || wy==34 || wy==40 || wy==46 || wy==52){
            return 4;
        }else if(wy==5 || wy==11 || wy==17 || wy==23 || wy==29 || wy==35 || wy==41 || wy==47 || wy==53){
            return 5;
        }else if(wy==6 || wy==12 || wy==18 || wy==24 || wy==30 || wy==36 || wy==42 || wy==48 || wy==0){
            return 6;
        }
        return  0;
    }

    public static String getWeekDay(){
        Calendar cal = Calendar.getInstance();
        int weekDay = cal.get(Calendar.DAY_OF_WEEK);
        if(weekDay==1){
            weekDay = 7;
        }else {
            weekDay--;
        }
        return weekDay+"";
    }

    public int s2i(String s){
        if(s == null){
            return 0;
        }
        return Integer.parseInt(s);
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
        tv_date_text.setText(date_text);
        week_y = FormActivity.getWeekOfYear(date);
    }

}