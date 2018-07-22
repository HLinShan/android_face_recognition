package com.example.todrip.shebei_test2;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wits.serialport.SerialPortManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH)+1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    Button bt_startfacenet;
    Button bt_downfeature;
    Button bt_deletefeature;
    Button bt_open;
    EditText et_geturl;
    public Excelop excelop=new Excelop();
    String outputfiledir;
    String environmentDir;
    String downlownfilename;

    private SerialPortManager mSerialPortManager;
    private InputStream mInputStream4;
    private OutputStream mOutputStream4;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        outputfiledir=get_OutPutDownLoadDir();
        environmentDir=get_EnvironmentDir();

        //get button edittext
        bt_downfeature= (Button) findViewById(R.id.bt_downfeature);
        bt_deletefeature= (Button) findViewById(R.id.bt_deletefeature);
        bt_startfacenet = (Button) findViewById(R.id.bt_startfacenet);
        bt_open=findViewById(R.id.bt_open);
        et_geturl = (EditText) findViewById(R.id.et_geturl);
        //    按键监听
        bt_downfeature.setOnClickListener(this);
        bt_deletefeature.setOnClickListener(this);
        bt_startfacenet.setOnClickListener(this);
        bt_open.setOnClickListener(this);
        handler = new Handler();

        mSerialPortManager = new SerialPortManager();



    }


    //    点击操作
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_startfacenet://打开摄像头
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                //传递字符串
                String passString = et_geturl.getText().toString();
                //给第二个界面传 文件地址
                String downloadfilename=outputfiledir+year +""+ month +"UserInfo_downloadfile"+".xlsx";

                intent.putExtra("downloadfilename", downloadfilename);

                intent.putExtra("name", get_EnvironmentDir());
                startActivity(intent);
                break;

            case R.id.bt_downfeature://下载
                String url=et_geturl.getText().toString();
                String get_url="http://youkangbao.cn/ykb/back/face/download.php";
                downlownfilename=excelop.load_DownExcelFromServer(get_url,outputfiledir);
                Log.i("downfeature","sdfs");
                break;

            case R.id.bt_deletefeature://删除所有downloadfiledir
                Log.i("deletefeature","1234");
                File objFile = new File(outputfiledir);
                if (!objFile.exists()) {   //文件不存在
                    break;
                }
                excelop.del_AllFiles(new File(outputfiledir));
                break;

            case R.id.bt_open://打开串口 继电器

                if (mOutputStream4 == null) {
                    Toast.makeText(this, "请先打开串口", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    byte[] bytes1 = SlecProtocol.hexStringToBytes(new String[]{
                                    "00555555",  //用户id
                                    "00000000",//用户卡号
                                    "0001"}//开门间隔
                            , true);
                    byte[] bytes = SlecProtocol.commandAndDataToAscii(
                            ((byte) 0x01),
                            bytes1
                    );
                    mOutputStream4.write(bytes);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;


        }
    }



    //获取存放下载的代码外部内存地址environment
    public String get_EnvironmentDir(){
        File outputDir = new File(Environment.getExternalStorageDirectory(), getPackageName());

        if (!outputDir.exists()) {
            outputDir.mkdir();
            Log.i("TAG1","create dir"+outputDir.getAbsolutePath());
        }
        String get_outputfiledir=outputDir.getAbsolutePath() + File.separatorChar;

        return get_outputfiledir ;
    }

    //    获取下载的地址envirment +downloadfiledir
    public String get_OutPutDownLoadDir(){
        String environmentdir=get_EnvironmentDir();
        String get_outputfiledir;

        get_outputfiledir=environmentdir+"DownloadFileDir";
        File outputfiledir=new File(get_outputfiledir);
        if (!outputfiledir.exists()) {
            outputfiledir.mkdir();
            Log.i("TAG1","create dir"+outputfiledir.getAbsolutePath());
        }
        return get_outputfiledir + File.separatorChar;
    }



}
