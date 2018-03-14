package com.example.resourcesinternational;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.registerReceiver(localeChangedReceiver, new IntentFilter(Intent.ACTION_LOCALE_CHANGED));

        String local = Locale.getDefault().toString();


        setContentView(R.layout.activity_main);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        TextView textView = (TextView) findViewById(R.id.textView);
        Button button = (Button) findViewById(R.id.button);
        imageView.setImageResource(R.drawable.slide_phone_bind_pressed);

//        switchLanguage(local);//只要在设置资源前调用即可生效
        textView.setText(R.string.session_message_get91IdFail);

        final String language = System.getProperty("language");


    }


    @Override
    protected void onDestroy() {
        this.unregisterReceiver(localeChangedReceiver);
        super.onDestroy();
    }

    BroadcastReceiver localeChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_LOCALE_CHANGED.equals(intent.getAction())) {
                Log.d("tag", "onReceive: ACTION_LOCALE_CHANGED=====================");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    LanguageUtil.setSystemLocaleList(LocaleList.getDefault());
//                    LanguageUtil.setLocale(MainActivity.this);
                    Resources res = context.getResources();
                    Configuration config = res.getConfiguration();
                    //                    DisplayMetrics dm = res.getDisplayMetrics();
                    //                    Locale locale = Locale.getDefault();
//                    Locale local = localeList.get(0);
                    LocaleList localeList = LocaleList.getDefault();
                    Locale locale;
                    Locale simple = null;
                    Locale traditional = null;
                    for (int i = 0; i < localeList.size(); i++) {

                        locale = localeList.get(i);
                        if (Locale.SIMPLIFIED_CHINESE.getCountry().equals(locale.getCountry()) && Locale.SIMPLIFIED_CHINESE.getLanguage().equals(locale.getLanguage())) {
                            simple = locale;
                        }


                        if (Locale.TRADITIONAL_CHINESE.getCountry().equals(locale.getCountry()) && Locale.TRADITIONAL_CHINESE.getLanguage().equals(locale.getLanguage())) {
                            traditional = locale;
                        }

                    }

                    Locale currentLocal = localeList.get(0);

//                    if (Locale.SIMPLIFIED_CHINESE.getLanguage().equals(currentLocal.getLanguage()) && Locale.SIMPLIFIED_CHINESE.getCountry().equals(currentLocal.getCountry())) {
//                        config.setLocale(simple);
//                    } else {//其他语言环境都用繁体资源
//                        config.setLocale(traditional);
//                    }
                    if (!(Locale.SIMPLIFIED_CHINESE.getCountry().equals(currentLocal.getCountry()) && Locale.SIMPLIFIED_CHINESE.getLanguage().equals(currentLocal.getLanguage()))) {
                        if (traditional != null) {
                            currentLocal = traditional;
                        } else {
                            currentLocal = Locale.TRADITIONAL_CHINESE;
                        }
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        config.setLocale(currentLocal);

                        context.createConfigurationContext(config);
                    } else {
                        config.locale = currentLocal;

                        res.updateConfiguration(config, res.getDisplayMetrics());
                    }

                } else {
                    setLocalBelowN(context);
                }
            }
        }
    };

    public static Context setLocalBelowN(Context base) {
        Resources res = base.getResources();
        Configuration config = res.getConfiguration();
        Locale locale = Locale.getDefault();


        if (Locale.SIMPLIFIED_CHINESE.equals(locale)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLocale(Locale.SIMPLIFIED_CHINESE);

            } else
                config.locale = Locale.SIMPLIFIED_CHINESE;

        } else {//其他语言环境都用繁体资源
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLocale(Locale.TRADITIONAL_CHINESE);
            } else {
                config.locale = Locale.TRADITIONAL_CHINESE;

            }

        }

//        config.setLocale(locale); // getLocale() should return a Locale

        Context newContext = base;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            newContext = base.createConfigurationContext(config);
        } else {
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return newContext;
    }

    protected void switchLanguage(String lang) {
        Resources resources = getResources();// 获得res资源对象
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (lang.equals("zh_CN")) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else {
            config.locale = Locale.TRADITIONAL_CHINESE;
        }
        Toast.makeText(this, "已切换到" + lang, Toast.LENGTH_SHORT).show();
        resources.updateConfiguration(config, dm);
        String cu_lan = Locale.getDefault().toString();
        System.setProperty("language", cu_lan);
        Log.d("", "=========================switchLanguage: cu_lan=" + cu_lan);

    }

}
