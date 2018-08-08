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
        <receiver
            android:name=".AutoStartBroadcastReceiver"
            tools:ignore="WrongManifestParent">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />

                <category android:name="android.intent.category.HOME" />
            </intent-filter>
        </receiver>
