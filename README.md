# android_face_recognition
face recognition android using feature csvfile 

Excelop.java  ___使用 poi excel 读写 excel 2007 后缀是 "xlsx", 需要在build.gradle
            compile group: 'org.apache.poi', name: 'poi', version: '3.17'
            compile group: 'org.apache.poi', name: 'poi-ooxml', version: '3.17'  
            如果运行报错，" implementation 'com.fasterxml:aalto-xml:1.0.0' " 再在相同的位置填写，如果还有其他问题根据报错信息，再去解决。
            excel poi 正常读取文件读取只能读写比较小的文件。
            当文件稍微大的时候将出现OMM错误，有三种解决方案：读写sax模式，easyexcel（主要运行在服务器端），换文件格式，csv等。
     
Readcsv.java ___读取excel出现OOM错误，直接使用csv，方便读取。

ReadTxt.java ___读取配置文本信息。
    
Para.java _____保存参数在APP中，当你打开APP可以获取参数是上一次保存。

AutoStarBroadcastReceive.java ____开机自动启动APP，需要在Androidmanifeasts.xml application中填写

Box,Utils,Facenet,MTCNN,FaceFeature  ____人脸识别的主要方法。mtcnn检测人脸，facenet导出特征参数，facefeature特征参数操作。
UpDownfile_java ____与服务器上传下载文件和数据。使用okhttp3
SlecProtpcol.java  __设备继电器控制。
MainActivity  ___ 主界面进行设置参数，上传，下载文件，以及一些操作。
Main2Activity  ___ 人脸识别界面，使用摄像机获取拍摄图片，实时获取照片在与数据库中特征进行比较。




