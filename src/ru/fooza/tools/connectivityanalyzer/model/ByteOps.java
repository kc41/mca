package ru.fooza.tools.connectivityanalyzer.model;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 17.07.11
 * Time: 15:56
 * To change this template use File | Settings | File Templates.
 */
public class ByteOps {

    public static void intToByte(int num,byte[] data, int offset){
        for (int i = 0; i < 4; i++){
            data[offset + i] = (byte)(num>>>(3-i)*8);
        }
    }

    public static byte[] intToByte(int num){
        byte[]  data = new byte[4];
        for (int i = 0; i < 4; i++){
            data[i] = (byte)(num>>>(3-i)*8);
        }
        return data;
    }

    //Supporting methods for byte ops
    public static int getSeqNo(byte[] data){
        return (data[0] << 24)+((data[1] & 0xFF) << 16)+((data[2] & 0xFF) << 8)+(data[3] & 0xFF);
    }

    public static int byteToInt(byte[] data, int offset){
        return (data[offset+0] << 24)+((data[offset+1] & 0xFF) << 16)+
                ((data[offset+2] & 0xFF) << 8)+(data[offset+3] & 0xFF);
    }
}
