package com.example.todrip.shebei_test2;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class ReadTxt {


    //从txt文件读取配置
    public Map read_TxtFile(String filename) throws IOException {

        Map<String, String > infomap = new HashMap<String, String >();
        String[] infoname={"time0","time1","threshold","downloadinfourl","downloadfeatureurl","uploadurl"};
        int i = 0;// 用于标记打印的条数

        try {
            File csv = new File(filename);// Txt文件路径
            BufferedReader br = new BufferedReader(new FileReader(csv));
            String line = "";
            while ((line = br.readLine()) != null && i<6) { // 这里读取csv文件中的前10条数据
                //System.out.println("第" + i + "行：" );// 输出每一行数据
                infomap.put(infoname[i], line);
                //System.out.println(buffer);
                i=i+1;
            }
            System.out.println(infomap.get("time1"));//取值
            Log.i("featureall","每行feature的个数");//
            br.close();
            return infomap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 将txt里的文档放在edittext里
    //读取配置get

    //读取配置
    void get_info(String filename,EditText t0,EditText t1,EditText t2,EditText t3,EditText t4,EditText t5) throws IOException {
        Map<String,String> infomapformtxt=read_TxtFile(filename);
        Log.i("info", infomapformtxt.get("time0"));
        t0.setText(infomapformtxt.get("time0"));
        t1.setText(infomapformtxt.get("time1"));
        t2.setText(infomapformtxt.get("threshold"));
        t3.setText(infomapformtxt.get("downloadinfourl"));
        t4.setText(infomapformtxt.get("downloadfeatureurl"));
        t5.setText(infomapformtxt.get("uploadurl"));
    }
    //
    //保存配置save
    public int  judge_Info(EditText t0, EditText t1, EditText t2) throws IOException {
//

        //写入数据,由于后续setText需要传入Sting类型,所以传入的值也为String
        int ver0 = Integer.parseInt(t0.getText().toString());
        int ver1 = Integer.parseInt(t1.getText().toString());
        double ver2 = Double.parseDouble(t2.getText().toString());
        if (ver0 < 1 || ver0 > 10) {
            //Toast.makeText(MainActivity.this,"计时器1的有效设置范围为1-10秒",Toast.LENGTH_SHORT).show();
            return 1;
        } else if (ver1 < 1 || ver1 > 10) {
            //Toast.makeText(MainActivity.this,"计时器2的有效设置范围为1-10秒",Toast.LENGTH_SHORT).show();
            return 2;
        } else if (ver2 < 0 || ver2 > 1.1) {
            //Toast.makeText(MainActivity.this,"阈值的有效设置范围为0-1.1",Toast.LENGTH_SHORT).show();
            return 3;
        } else {
            //Toast.makeText(MainActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
            return 4;
        }
    }
    }


