package com.example.todrip.shebei_test2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wits.serialport.SerialPort;
import com.wits.serialport.SerialPortManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
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
    Button bt_senttoserver;
    Button bt_savepara;
    Button bt_open;
    Button bt_downinfofile;
    EditText et_senttoserver;
    public EditText t0,t1,t2,t3,t4,t5;
    private static Toast myToast;


    public UpDownfile updownfile=new UpDownfile();
    String outputfiledir;
    String environmentDir;
    String info_downfilename;
    String feature_downfilename;

    public int[] user_idlist=new int[1500];
    public float[][] user_featuerlist=new float[1500][];

    public  ReadTxt readtxt=new ReadTxt();
    private SerialPortManager mSerialPortManager;
    private InputStream mInputStream4;
    private OutputStream mOutputStream4;
    private Handler handler;
//    public  String serverurl="http://youkangbao.cn/ykb/back/checkin/add.php";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint({"WrongViewCast", "WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        outputfiledir = get_OutPutDownLoadDir();
        environmentDir = get_EnvironmentDir();
        //get button edit_text
        bt_downfeature = (Button) findViewById(R.id.bt_downfeature);
        bt_deletefeature = (Button) findViewById(R.id.bt_deletefeature);
        bt_startfacenet = (Button) findViewById(R.id.bt_startfacenet);
        bt_open = findViewById(R.id.bt_open);
        bt_senttoserver = (Button) findViewById(R.id.bt_senttoserver);

        bt_downinfofile = findViewById(R.id.bt_downinfofile);
        t0 = (EditText) findViewById(R.id.ed1);//计时器1
        t1 = (EditText) findViewById(R.id.ed2);//计时器2
        t2 = (EditText) findViewById(R.id.ed3);//比对阈值
        t3 = (EditText) findViewById(R.id.et_getInfofileurl);
        t4 = (EditText) findViewById(R.id.et_getfeaturefileurl);
        t5 = (EditText) findViewById(R.id.et_getupfileurl);
        et_senttoserver = (EditText) findViewById(R.id.et_senttoserver);
        //    按键监听
        bt_downfeature.setOnClickListener(this);
        bt_deletefeature.setOnClickListener(this);
        bt_startfacenet.setOnClickListener(this);
        bt_downinfofile.setOnClickListener(this);
        bt_open.setOnClickListener(this);
        bt_senttoserver.setOnClickListener(this);


//        继电器
        handler = new Handler();
        mSerialPortManager = new SerialPortManager();
        //串口4，继电器控制
//        SerialPort serialPort4 = null;
//        try {
//            serialPort4 = mSerialPortManager.getSerialPort4();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mInputStream4 = serialPort4.getInputStream();
//        mOutputStream4 = serialPort4.getOutputStream();





        //下载info和feature
        try {
        String infourl=t3.getText().toString();

        info_downfilename=updownfile.load_DownInfoTxtFromServer(infourl,outputfiledir);
        File info_file=new File(info_downfilename);
        if(info_file.exists()){//如果文件存在读取文件
            readtxt.get_info(info_downfilename,t0,t1,t2,t3,t4,t5);}
        String downloadurl = t4.getText().toString();
        feature_downfilename = updownfile.load_DownCsvFileFromServer(downloadurl, outputfiledir);
        }catch (Exception e1)
        {//如果下载文件有异常就使用默认的。
            toast(MainActivity.this,"下载Info和feature文件异常，将使用默认配置");

        }
        //如果feature 和 info 都在的话就跳转到下一个界面人脸识别
        int para_result;
        try {
            para_result = readtxt.judge_Info(t0,t1,t2);
            //根据返回的int在toast中输出结果
            switch (para_result){
                case 1:
                    toast(MainActivity.this,"计时器1的有效设置范围为1-10秒");
                    break;
                case 2:
                    toast(MainActivity.this,"计时器2的有效设置范围为1-10秒");

                    break;
                case 3:
                    toast(MainActivity.this,"阈值的有效设置范围为0-1.1");

                    break;
                case 4:
                    toast(MainActivity.this,"参数正确");
                    Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                    //传递字符串
//                String passString = et_geturl.getText().toString();
                    //给第二个界面传 文件地址
                    String downloadfilefromserver=updownfile.set_DownCsvFileName(outputfiledir);
                    File file=new File(downloadfilefromserver);
                    if (file.exists()){
                        toast(MainActivity.this,"feature文件已下载");

                    }
                    else{
                        toast(MainActivity.this,"feature文件未下载，使用默认数据库");

                    }
                        //转界面 传参数 和 已下载的特征文件地址
                        intent.putExtra("downloadfeaturefilefromserver", downloadfilefromserver);
                        intent.putExtra("uploaduseridtoserverurl", t5.getText().toString());
                        intent.putExtra("t0", t0.getText().toString());
                        intent.putExtra("t1", t1.getText().toString());
                        intent.putExtra("t2", t2.getText().toString());
                        startActivity(intent);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    //    点击操作
    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_startfacenet://转到打开摄像头第二个界面
                int para_result;
                try {
                    para_result = readtxt.judge_Info(t0, t1, t2);

                    //根据返回的int在toast中输出结果
                    switch (para_result) {
                        case 1:
                            toast(MainActivity.this,"计时器1的有效设置范围为1-10秒");
                            break;
                        case 2:
                            toast(MainActivity.this,"计时器2的有效设置范围为1-10秒");
                            break;
                        case 3:
                            toast(MainActivity.this,"阈值的有效设置范围为0-1.1");
                            break;
                        case 4:
                            toast(MainActivity.this,"参数正确");

                            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                            String downloadfilefromserver = updownfile.set_DownCsvFileName(outputfiledir);
                            File file=new File(downloadfilefromserver);
                            if (file.exists()){

                                toast(MainActivity.this,"feature文件已下载");
                            }
                            else{

                                toast(MainActivity.this,"feature文件未下载，使用默认数据库");

                            }
                            //转界面 传参数 和 已下载的特征文件地址
                            intent.putExtra("downloadfeaturefilefromserver", downloadfilefromserver);
                            intent.putExtra("uploaduseridtoserverurl", t5.getText().toString());
                            intent.putExtra("t0", t0.getText().toString());
                            intent.putExtra("t1", t1.getText().toString());
                            intent.putExtra("t2", t2.getText().toString());
                            startActivity(intent);
                    }
                    break;

                } catch (IOException e) {
                    toast(MainActivity.this,"出现异常，不能跳跳转到人脸识别界面");

                }
                break;
            case R.id.bt_downinfofile://下载Info
                String infourl = t3.getText().toString();
//                String get_url="http://youkangbao.cn/ykb/back/face/download.php";
                try {
                    info_downfilename = updownfile.load_DownInfoTxtFromServer(infourl, outputfiledir);
                    readtxt.get_info(info_downfilename, t0, t1, t2, t3, t4, t5);
                    toast(MainActivity.this,"info文件下载成功");
//            readtxt.save_info(filenametxt,t0,t1,t2,t3,t4,t5);
                } catch (Exception e) {
                    toast(MainActivity.this,"info文件没有下载成功");

                }
                break;

            case R.id.bt_downfeature://下载featurebiao
                String url = t4.getText().toString();
                try {
//                String get_url="http://youkangbao.cn/ykb/back/face/download.php";
                    feature_downfilename = updownfile.load_DownCsvFileFromServer(url, outputfiledir);

                    toast(MainActivity.this,"feature文件下载成功");
                    File file=new File(feature_downfilename);
                    if(!file.exists()) {

                        toast(MainActivity.this, "feature文件没有下载成功");
                    }


                } catch (Exception e)
                {
                    File file=new File(feature_downfilename);
                    if(!file.exists()) {

                        toast(MainActivity.this, "feature文件没有下载成功");
                    }


                }


                break;

            case R.id.bt_deletefeature://删除所有downloadfiledir
                Log.i("deletefeature","1234");
                File objFile1 = new File(outputfiledir);
                if (!objFile1.exists()) {   //文件不存在
                    break;
                }
                //如果文件删除结束
                if(updownfile.del_AllFiles(new File(outputfiledir))){

                    toast(MainActivity.this,"目标文件已被删除");

                }else {

                    toast(MainActivity.this,"目标文件夹没有目标文件");

                };
                break;

            case  R.id.bt_senttoserver://向服务器发送签到ID
                //String url1="http://youkangbao.cn/ykb/back/checkin/add.php";
                try{
                String uploadurl=t5.getText().toString();
                int idnumber=Integer.parseInt(et_senttoserver.getText().toString());
                updownfile.uploadDataToServer(uploadurl,idnumber);

                    toast(MainActivity.this,"向服务器传送签到ID成功");

                }
                catch (Exception e)
                {

                    toast(MainActivity.this,"向服务器传送签到ID失败");

                }
                break;


            case R.id.bt_open://打开串口 继电器
                try {
                    open_door();
                    toast(MainActivity.this,"打开继电器成功");

                }catch (Exception e )
                {
//                    Toast.makeText(this, "打开继电器失败",1000).show();
                    toast(MainActivity.this,"打开继电器失败");
                }
                break;


        }
    }

    public void open_door(){
        if (mOutputStream4 == null) {

//            toast(MainActivity.this,"请先打开串口");
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

        } catch (Exception e) {
            toast(MainActivity.this,"出现异常不能打开继电器");

        }
    }
    //获取存放下载的代码外部内存地址environment+包名
    public String get_EnvironmentDir(){
        File environmentdir = new File(Environment.getExternalStorageDirectory(), getPackageName());

        if (!environmentdir.exists()) {
            environmentdir.mkdir();
            Log.i("TAG1","create dir"+environmentdir.getAbsolutePath());
        }
        String get_environmentdir=environmentdir.getAbsolutePath() + File.separatorChar;

        return get_environmentdir ;
    }

    //    获取下载的地址envirment +包名+downloadfiledir
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
    public static void toast(Context context, String text){
        if (myToast != null) {
            myToast.cancel();
            myToast=Toast.makeText(context,text,Toast.LENGTH_SHORT);
        }else{
            myToast=Toast.makeText(context,text,Toast.LENGTH_SHORT);
        }
        myToast.show();
    }


}
