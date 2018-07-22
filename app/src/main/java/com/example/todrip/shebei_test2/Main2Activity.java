package com.example.todrip.shebei_test2;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Main2Activity extends AppCompatActivity implements SurfaceHolder.Callback {
    private ImageView imageView;
    private TextView textView;
    public Bitmap bitmap;
    private static final String TAG = "XMU";
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private SurfaceView mView;
    public CountDownTimer picture_timer;
    public Facenet facenet;
    public Bitmap bitmap1;
    public Bitmap bitmap2;
    private Uri imageUri;
    public static File tempFile;
    public MTCNN mtcnn;
    public String[] file_list;
    public FaceFeature[] ff_list;
    public Bitmap[] face_bitmap;
    public MediaPlayer player;
    public double result_score;
    public int result_index;
    public boolean is_face;
    public int[] user_idlist;
    public String[] user_namelist;
    public float[][] user_featuerlist;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        //以下为原先的代码
        mtcnn = new MTCNN(getAssets());
        facenet = new Facenet(getAssets());
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        //设置屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //imageView一开始不可见
        imageView.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        //预加载所有的人脸特征
        //先获得assets中jpg文件夹下所有的文件名称
        AssetManager assetManager = getAssets();
        try {
            file_list = assetManager.list("jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        face_bitmap = new Bitmap[file_list.length];
        ff_list = new FaceFeature[file_list.length];
        Log.d("TAG", "beafore");
        get_FaceFeature();
        //相机预览所需要的surfaceView、Holder以及回调函数
        mView = (SurfaceView) findViewById(R.id.surfaceView);
        mHolder = mView.getHolder();
        mHolder.addCallback(this);

        //显示前一页传递过来的信息
        Excelop excelop =new Excelop();
        String dir = getIntent().getStringExtra("name");
        Toast.makeText(this, "传递的信息为："+dir, Toast.LENGTH_LONG).show();


        //读excel
        String filename=dir+"1111.xlsx";
        File objFile = new File(filename);
        if (!objFile.exists()) {   //文件不存在
            Log.i("xxx","121212");
        }
        user_idlist=excelop.get_ExcelFileUserId(filename);
        user_featuerlist=excelop.get_ExcelFileUserFeature(filename);
        for(int i=0;i<7;i++) {
            Log.i("xxx", Arrays.toString(user_featuerlist[i]));
        }

        if (user_featuerlist==null) {
            Log.i("xxx", "xxx");
        }

        }


    //构造一个倒计时CountDownTimer（命名为picture_timer），用来倒计时，每间隔3s保存一张图片
    public void star_picture_timer() {
        if (picture_timer == null) {
            //总倒计时时间为5s，倒计时间隔为1s
            picture_timer = new CountDownTimer(3 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    //保存当前图片,并且每一次倒计时完成要把之前显示的图片删除
                    imageView.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);


                    //takepicture很花时间，开启一个新的线程
                    final Handler mHandler = new Handler();
                    Runnable mRunnableOnSeparateThread = new Runnable() {
                        @Override
                        public void run() {
                            mCamera.takePicture(null, null, mPicture);
                            if(is_face == true)
                            {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        get_visiable(result_score);
                                    }
                                });
                            }
                            //每次倒计时结束后取消当前的计时，开始下一次倒计时
                            picture_timer.cancel();
                            star_picture_timer();
                        }
                    };
                    new Thread(mRunnableOnSeparateThread).start();
//                    //每次倒计时结束后取消当前的计时，开始下一次倒计时
//                    picture_timer.cancel();
//                    star_picture_timer();


                }
            };
            picture_timer.start();
        } else {
            picture_timer.start();
        }
    }

    @Override
    // apk暂停时执行的动作：把相机关闭，避免占用导致其他应用无法使用相机，取消倒计时
    protected void onPause() {
        super.onPause();

        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        picture_timer.cancel();
    }

    @Override
    // 恢复apk时执行的动作
    protected void onResume() {
        super.onResume();
        if (null != mCamera) {
            mCamera = getCameraInstance();
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();//开始预览
            } catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }
        //显示主界面时启动定时
        star_picture_timer();
    }

    // SurfaceHolder.Callback必须实现的方法
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = getCameraInstance();
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    // SurfaceHolder.Callback必须实现的方法
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera(); // 这一步是否多余？在以后复杂的使用场景下，此步骤是必须的。
        int rotation = getDisplayOrientation(); //获取当前窗口方向
        mCamera.setDisplayOrientation(rotation); //设定相机显示方向
    }

    // SurfaceHolder.Callback必须实现的方法
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHolder.removeCallback(this);
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    // === 以下是各种辅助函数 ===
    // 获取camera实例
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(0);
        } catch (Exception e) {
            Log.d("TAG", "camera is not available");
        }
        return c;
    }

    // 获取当前窗口管理器显示方向
    private int getDisplayOrientation() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        android.hardware.Camera.CameraInfo camInfo =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, camInfo);

        int result = (camInfo.orientation - degrees + 360) % 360;

        return result;
    }

    // 刷新相机
    private void refreshCamera() {
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
        }
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
        }
    }

    //为了实现捕捉图像需要实现的的回调方法（takepicture）
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        public void onPictureTaken(final byte[] data, Camera camera) {
            Camera.Parameters ps = camera.getParameters();
            if (ps.getPictureFormat() == PixelFormat.JPEG) {

                //拍照之前，刷新一次相机
                refreshCamera();
                //存储拍照获得的图片
                //save(data);
                //新的思路，不要将照片存取到目录中，直接把data转为bitmap
                bitmap1 = BitmapFactory.decodeByteArray(data, 0, data.length);
                //旋转图像
                Matrix m = new Matrix();
                m.postRotate(90);
                bitmap1 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), m, true);
                m.setScale(-1,1);//翻转
                bitmap1 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), m, true);
                bitmap1 = Utils.resize(bitmap1, 1000);
                //拍照之后，刷新一次相机
                refreshCamera();

                FaceFeature ff1 = compareFaces();
                if(ff1 == null)
                {
                    is_face = false;
                }
                else
                {
                    is_face = true;
                    Log.i(TAG,"onPictureTaken --> 检测到人脸");
//                    get_result(ff1);

                    get_result_excel(ff1);
                }

//                //每次倒计时结束后取消当前的计时，开始下一次倒计时
//                picture_timer.cancel();
//                star_picture_timer();
//                //

//                //开启新的进程
//                final Handler mHandler = new Handler();
//                Runnable mRunnableOnSeparateThread = new Runnable() {
//                    @Override
//                    public void run() {
////                        //存储拍照获得的图片
////                        //save(data);
////                        //新的思路，不要将照片存取到目录中，直接把data转为bitmap
////                        bitmap1 = BitmapFactory.decodeByteArray(data, 0, data.length);
////                        //旋转图像
////                        Matrix m = new Matrix();
////                        m.postRotate(90);
////                        bitmap1 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), m, true);
////                        m.setScale(-1,1);//翻转
////                        bitmap1 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), m, true);
////                        //
////
////                        bitmap1 = Utils.resize(bitmap1, 1000);
//
////                        //比较两张图片，返回结果
////                        final FaceFeature ff1 = compareFaces();
////                        if(ff1 != null)
////                        {
////                            mHandler.post(new Runnable() {
////                                @Override
////                                public void run() {
////                                    get_result(ff1);
////                                }
////                            });
////                        }
//
////                        //以下为测试，直接显示结果
////                        mHandler.post(new Runnable() {
//////                            @Override
//////                            public void run() {
//////                                imageView.setImageBitmap(face_bitmap[2]);
//////                                imageView.setVisibility(View.VISIBLE);
//////                                String[] name = file_list[2].split("\\.");
//////                                textView.setText("注册用户:"+name[0]);
//////                                textView.setVisibility(View.VISIBLE);
//////                                get_pass();
//////                                //refreshCamera();
//////                            }
//////                        });
////                        //显示结果会卡顿，这里尝试用toast
////
////                        mHandler.post(new Runnable() {
////                            @Override
////                            public void run() {
////                                Toast.makeText(Main2Activity.this, "传递的信息为：", Toast.LENGTH_SHORT).show();
////                            }
////                        });
//
//                        //每次倒计时结束后取消当前的计时，开始下一次倒计时
//                        picture_timer.cancel();
//                        star_picture_timer();
//                    }
//                };

                //new Thread(mRunnableOnSeparateThread).start();
            }
        }
    };

    //从assets中读取图片
    private Bitmap readFromAssets(String filename) {
        Bitmap bitmap;
        AssetManager asm = getAssets();
        try {
            InputStream is = asm.open("jpg/" + filename);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            Log.e("MainActivity", "[*]failed to open " + filename);
            e.printStackTrace();
            return null;
        }
        return Utils.copyBitmap(bitmap);
    }

    //根据comparaFaces(）函数写的将文件列表中所有的jpg转化成Facefeature


    public void get_FaceFeature() {
        Log.d("TAG", "here_00");
        for (int i = 0; i < file_list.length; i++) {
            Log.d("TAG", Integer.toString(i));

            face_bitmap[i] = readFromAssets(file_list[i]);
            face_bitmap[i] = Utils.resize(face_bitmap[i], 1000);


            //Bitmap bm = Utils.copyBitmap(face_bitmap[i]);
            Rect rect = mtcnn.getBiggestFace(face_bitmap[i], 40);

            //MTCNN检测到的人脸框，再上下左右扩展margin个像素点，再放入facenet中。
            int margin = 20; //20这个值是facenet中设置的。自己应该可以调整。
            Utils.rectExtend(face_bitmap[i], rect, margin);

            //要比较的两个人脸，加厚Rect
            Utils.drawRect(face_bitmap[i], rect, 1 + face_bitmap[i].getWidth() / 100);

            //(2)裁剪出人脸(只取第一张)
            Bitmap face = Utils.crop(face_bitmap[i], rect);

            //(3)特征提取
            ff_list[i] = facenet.recognizeImage(face);
            // face_bitmap[i] = bm;
        }
    }

    //两个照片对比score
    public FaceFeature compareFaces(){
        //refreshCamera();
        Rect rect1=mtcnn.getBiggestFace(bitmap1,40);
        if (rect1==null)
        {
            //refreshCamera();
            return null;
        }

        //检测到人脸，先刷新一次相机
        //refreshCamera();
        //

        //MTCNN检测到的人脸框，再上下左右扩展margin个像素点，再放入facenet中。
        int margin=20; //20这个值是facenet中设置的。自己应该可以调整。
        Utils.rectExtend(bitmap1,rect1,margin);
        //要比较的两个人脸，加厚Rect
        Utils.drawRect(bitmap1,rect1,1+bitmap1.getWidth()/100 );
        //(2)裁剪出人脸(只取第一张)
        Bitmap face1=Utils.crop(bitmap1,rect1);

        //特征提取之前先再刷新一次相机
        //refreshCamera();
        //

        //(3)特征提取
        FaceFeature ff1=facenet.recognizeImage(face1);



        //比较之前先再刷新一次相机
        //refreshCamera();
        //
        return ff1;
    }

    //在主线程获取结果并且显示结果
    public void get_result(FaceFeature ff1){
//        //(4)比较
//        double score;
//        for(int i = 0;i<file_list.length;i++){
//            score = ff1.compare(ff_list[i]);
//            if (score>=0 && score<1.1)
//            {
//                result_score = score;
//                result_index = i;
//                return;
//            }
//        }
//        result_score = 2;
//        result_index = 0;
//        //refreshCamera();
//        return;
        double score;
        double min_score = Double.MAX_VALUE;
        int possible_user_id = -1;
        for (int i = 0; i < file_list.length; i++) {
            score = ff1.compare(ff_list[i]);
            if (score >= 0 && score < 1.1) {
                if(score < min_score){
                    min_score = score;
                    possible_user_id = i;
                }
                Log.i(TAG, "和"+file_list[i]+"对比，成绩"+score);
            }

        }
        result_score = min_score;
        result_index = possible_user_id;
        Log.i(TAG, "和"+possible_user_id+"最相似，成绩"+min_score);
    }

    //在主线程获取结果并且显示结果
    public void get_result_excel(FaceFeature ff1) {
        //(4)比较
        double score;
        double min_score = Double.MAX_VALUE;
        int possible_user_id = -1;
        for (int i = 0; i < user_idlist.length; i++) {
            score = ff1.compare_float(user_featuerlist[i]);
            if (score >= 0 && score < 1.1) {
                if(score < min_score){
                    min_score = score;
                    possible_user_id = i;
                }
                Log.i(TAG, "和"+user_idlist[i]+"对比，成绩"+score);
            }

        }
        result_score = min_score;
        result_index = possible_user_id;
        Log.i(TAG, "和"+possible_user_id+"最相似，成绩"+min_score);
//        result_score = 2;
    }

        //为了多线程运行，根据得分来输出结果
    public void get_visiable(double score)
    {
        if (score>=0 && score<0.95)
        {
            imageView.setImageBitmap(bitmap1);
            imageView.setVisibility(View.VISIBLE);
            String[] name = file_list[result_index].split("\\.");
            textView.setText("注册用户:"+name[0]+'\n'+"score:"+score);
            textView.setVisibility(View.VISIBLE);
            get_pass();
            //refreshCamera();
            return;
        }
        else
        {
            textView.setText("非注册用户");
            textView.setVisibility(View.VISIBLE);
            get_verification();
            //refreshCamera();
            return;
        }
    }

    public void get_pass(){
        if(player != null)
        {
            player.release();
            player = null;
        }
        try {
            player = new MediaPlayer();
            AssetManager assetManager = getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd("tongguo.mp3");
            player.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(),
                    fileDescriptor.getStartOffset());
            player.prepare();
            player.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void get_verification(){
        if(player != null)
        {
            player.release();
            player = null;
        }
        try {
            player = new MediaPlayer();
            AssetManager assetManager = getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd("verification.mp3");
            player.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(),
                    fileDescriptor.getStartOffset());
            player.prepare();
            player.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
