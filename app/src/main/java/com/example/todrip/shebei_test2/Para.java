package com.example.todrip.shebei_test2;

import android.content.SharedPreferences;
import android.widget.EditText;

public class Para {
    //存储配置
    int save_para(SharedPreferences sp, EditText t1,EditText t2,EditText t3,EditText t4){
        SharedPreferences.Editor edit = sp.edit();

        //写入数据,由于后续setText需要传入Sting类型,所以传入的值也为String
        int ver1 = Integer.parseInt(t1.getText().toString());
        int ver2 = Integer.parseInt(t2.getText().toString());
        double ver3 = Double.parseDouble(t3.getText().toString());
        if(ver1<1 || ver1>10)
        {
            //Toast.makeText(MainActivity.this,"计时器1的有效设置范围为1-10秒",Toast.LENGTH_SHORT).show();
            return 1;
        }
        else if (ver2<1 || ver2>10)
        {
            //Toast.makeText(MainActivity.this,"计时器2的有效设置范围为1-10秒",Toast.LENGTH_SHORT).show();
            return 2;
        }
        else if(ver3<0 || ver3>1.1)
        {
            //Toast.makeText(MainActivity.this,"阈值的有效设置范围为0-1.1",Toast.LENGTH_SHORT).show();
            return 3;
        }
        else
        {
            edit.putString("t1",t1.getText().toString());
            edit.putString("t2",t2.getText().toString());
            edit.putString("t3",t3.getText().toString());
            edit.putString("t4",t4.getText().toString());
            edit.commit();
            //Toast.makeText(MainActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
            return 4;
        }
    }
    //读取配置
    void get_para(SharedPreferences sp,EditText t1,EditText t2,EditText t3,EditText t4)
    {
        String time1_value = sp.getString("t1","1");//获取不到t1则默认为1
        t1.setText(time1_value);
        String time2_value = sp.getString("t2","5");//获取不到t2则默认为5
        t2.setText(time2_value);
        String value = sp.getString("t3","0.95");//获取不到t3则默认为0.95
        //t3获取到的是sting类型，给他赋予的也是Sting类型
        t3.setText(value);
        t4.setText(sp.getString("t4","http://youkangbao.cn/ykb/back/face/download.php"));//获取不到t4则默认为www.aaa.com
    }
}
