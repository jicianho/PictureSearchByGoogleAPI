package com.takepickpicturedemo;

import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by 10411024 on 2016/3/10.
 */
public class CalorieComparison{

    String fileName = getSDPath() + "/" + "caroies";
    protected double matching_database(String predict_item) throws IOException {
        double match_item = 0;
        JSONArray match_JArray = new JSONArray();
        File match_f = new File(fileName + "food_database.json");
        String json_string = "";

        FileReader fr = new FileReader(match_f);
        BufferedReader br = new BufferedReader(fr);
        while (br.ready()) {
            json_string = br.readLine();
        }
        fr.close();

        try {
            match_JArray = new JSONArray(json_string);
        } catch (JSONException e) {
            Log.e("read json array", e.toString());
        }
        String name = "";
        for (int i = 0; i < match_JArray.length(); i++) {
            try {
                name = match_JArray.getJSONObject(i).getString("name");
                match_item = match_JArray.getJSONObject(i).getDouble("cal");
            } catch (JSONException e) {
                Log.e("Catch obj", e.toString());
            }

            if (name.compareTo(predict_item) > 5) {
                break;
            }

        }
        System.out.print(name);
        return match_item;
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); //判斷sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//獲取跟目錄
        }
        return sdDir.toString();
    }

    //  解决java中compareTo函数在比较中文时结果错误的问题
    private static String __ENCODE__ = "GBK"; //一定要是GBK
    private static String __SERVER_ENCODE__ = "GB2312";//服务器上的缺省编码
    //  比较两字符串
    public int compare(String s1, String s2){
        String m_s1 = null, m_s2 = null;
        try {
            //先将两字符串编码成GBK
            m_s1 = new String ( s1.getBytes(__SERVER_ENCODE__), __ENCODE__);
            m_s2 = new String ( s2.getBytes(__SERVER_ENCODE__), __ENCODE__);
        }
        catch( Exception ex){
            return s1.compareTo(s2);
        }
        int res = chineseCompareTo(m_s1, m_s2);
//          比较结果输出
//          System.out.println("比较：" + s1 + " | " + s2 + "==== Result: " + res);
        return res;
    }
    //获取一个汉字/字母的Char值
    public static int getCharCode(String s){
        if (s==null && s.equals("")) return -1;                     //保护代码
        byte [] b = s.getBytes();
        int value = 0;                                              //保证取第一个字符（汉字或者英文）
        for (int i = 0; i < b.length && i <= 2; i ++)
        {
            value = value * 100 + b[i];
        }
        return value;
    }
    //比较两个字符串
    public int chineseCompareTo(String s1, String s2){
        int len1 = s1.length();
        int len2 = s2.length();
        int n = Math.min(len1, len2);
        for (int i = 0; i < n; i ++)
        {
            int s1_code = getCharCode(s1.charAt(i) + "");
            int s2_code = getCharCode(s2.charAt(i) + "");
            if (s1_code != s2_code)
                return s1_code - s2_code;
        }
        return len1 - len2;
    }
}
