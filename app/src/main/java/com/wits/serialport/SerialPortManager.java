package com.wits.serialport;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * Created by Administrator on 2017-05-24.
 */

public class SerialPortManager {

    private SerialPort mSerialPort;
    private SerialPort mSerialPort4;

    //打开串口1
    public SerialPort getSerialPort() throws SecurityException, IOException,
            InvalidParameterException {
        mSerialPort = new SerialPort(new File("/dev/ttyS3"), 19200, 0);
        return mSerialPort;
    }
    //打开串口4
    public SerialPort getSerialPort4() throws SecurityException, IOException,
            InvalidParameterException {
        mSerialPort4 = new SerialPort(new File("/dev/ttyS4"), 19200, 0);
        return mSerialPort4;
    }
    //关闭串口
    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }

    public void closeSerialPort4() {
        if (mSerialPort4 != null) {
            mSerialPort4.close();
            mSerialPort4 = null;
        }
    }

}
