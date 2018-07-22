package com.example.todrip.shebei_test2;

public class SlecProtocol {
    public static final byte bHead = 2;
    public static final byte bTail = 3;
    public static byte[] hexStringToBytes(String[] hex, boolean lowInFirst) {
        if (hex != null && hex.length > 0) {
            int len = 0;
            for (int i = 0; i < hex.length; i++) {
                len += hex[i].length();
            }
            byte[] data = new byte[len / 2];
            int tempLen = 0;
            try {
                for (int i = 0; i < hex.length; i++) {
                    for (int j = 0; j < (hex[i].length()/2); j++) {
                        String hexString="";
                        if (lowInFirst) {
                            hexString = hex[i].substring((hex[i].length()-j * 2-2), (hex[i].length()-j * 2));
                        } else {
                            hexString = hex[i].substring(j * 2, (j * 2) + 2);
                        }
                        int temp = Integer.parseInt(hexString, 16);
                        data[tempLen + j] = (byte) (temp & 0xFF);
                    }
                    tempLen += (hex[i].length()/2);
                }
            } catch (Exception e) {
                return null;
            }

            return data;
        }
        return null;

    }
    public static byte[] commandAndDataToAscii(byte command, byte[] data) {
        int len = 0;
        if (null != data) {
            len = data.length;
        }
        byte[] b = new byte[8 + len];
        b[0] = bHead;
        b[1] = 0x20;
        b[2] = 0;
        b[3] = command;
        b[4] = 0;
        b[5] = (byte) len;

        for (int i = 0; i < len; i++) {
            b[i + 6] = data[i];
        }
        int crc = 0;
        for (int i = 1; i < b.length - 2; i++) {

            crc ^= b[i];
        }
        b[b.length - 2] = (byte) crc;
        b[b.length - 1] = bTail;
        return hexToAscii(b);
    }
    public static byte[] hexToAscii(byte[] hexBytes) {

        if (hexBytes != null) {
            byte[] asciiBytes = new byte[hexBytes.length * 2 - 2];
            asciiBytes[0] = hexBytes[0];
            asciiBytes[asciiBytes.length - 1] = hexBytes[hexBytes.length - 1];
            for (int i = 1; i < hexBytes.length - 1; i++) {
                if (((hexBytes[i]&0xff) >> 4) >= 0X0A) {
                    asciiBytes[i * 2 - 1] = (byte) (((hexBytes[i]&0xff) >> 4) + 0X37);
                } else {
                    asciiBytes[i * 2 - 1] = (byte) (((hexBytes[i]&0xff) >> 4) + 0X30);
                }
                if (((hexBytes[i]&0xff) & 0X0F) >= 0X0A) {
                    asciiBytes[i * 2] = (byte) ((hexBytes[i] & 0X0F) + 0X37);
                } else {
                    asciiBytes[i * 2] = (byte) ((hexBytes[i] & 0X0F) + 0X30);
                }
            }
            return asciiBytes;
        }
        return null;
    }
    public static String bytesToHexString2(byte[] bArray, int lenth) {
        StringBuffer sb = new StringBuffer(lenth);
        if (bArray == null || bArray.length <= 0) {
            return null;
        }
        String sTemp;
        for (int i = 0; i < lenth; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] asciiToHex(byte[] asciiBytes) {
        if (asciiBytes != null) {
            byte[] hexBytes = new byte[(asciiBytes.length) / 2 + 1];
            hexBytes[0] = asciiBytes[0];
            hexBytes[hexBytes.length - 1] = asciiBytes[asciiBytes.length - 1];
            for (int i = 1; i < hexBytes.length - 1; i++) {
                if (asciiBytes[i * 2 - 1] >= 0X41) {
                    hexBytes[i] = (byte) ((asciiBytes[i * 2 - 1] - 0X37) << 4);
                } else {
                    hexBytes[i] = (byte) ((asciiBytes[i * 2 - 1] - 0X30) << 4);
                }

                if (asciiBytes[i * 2] >= 0X41) {
                    hexBytes[i] |= (byte) (asciiBytes[i * 2] - 0X37);
                } else {
                    hexBytes[i] |= (byte) (asciiBytes[i * 2] - 0X30);
                }
            }

            return hexBytes;
        }

        return null;
    }
    public static byte[] hexToByteArray(String hex) {
        hex = hex.replace(" ", "");
        byte byteArr[] = new byte[hex.length() / 2];
        for (int i = 0; i < byteArr.length; i++) {
            int temp = Integer.parseInt(hex.substring(i * 2, (i * 2) + 2), 16);
            byteArr[i] = (byte) (temp & 0xFF);
        }
        return byteArr;
    }
}
