package com.example.resourcesinternational;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by guodazhao on 2017/12/6 0006.
 */

public class LanguageUtil {
    private static LocaleList sLocaleList;
    private static boolean auto=true;
    private static SharedPreferences.Editor edit;

    static {
        //由于API仅支持7.0，需要判断，否则程序会crash
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sLocaleList = LocaleList.getDefault();
        }
    }


    public static void setLocale(Context context) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            if (auto) { //选择跟随系统
                conf.setLocale(sLocaleList.get(0));
            } else { //设置选择的语言
                conf.setLocale(getLocale());
            }
        } else {
            conf.locale = getCurrentLocale(context);
        }
        res.updateConfiguration(conf, dm);
    }

    private static Locale getCurrentLocale(Context context) {
       Locale locale= Locale.getDefault();
        return locale;
    }

    public static Locale getLocale() {
        Locale locale=null;
        if (auto) {
            locale = Locale.getDefault();
        }
        else {
            //从 sharedPreference 中获取 Locale
//            locale = getLocaleFromSP();
        }
        return locale;
    }

    public static void setLocaleFromSP(Context context) {
        SharedPreferences sp = context.getSharedPreferences("local", Context.MODE_PRIVATE);
        edit=sp.edit();

        sp.edit().putString("current_language", null);

    }

    public static String getLocaleFromSP(Context context) {
        SharedPreferences sp = context.getSharedPreferences("local", Context.MODE_PRIVATE);
        String currentLanguage = sp.getString("current_language", null);

        return currentLanguage;
    }


    public static void setSystemLocaleList(LocaleList localeList) {
        sLocaleList = localeList;
    }


}
