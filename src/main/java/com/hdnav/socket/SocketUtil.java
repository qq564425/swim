package com.hdnav.socket;

import com.hdnav.socket.socketPool.ConnectionProvider;
import com.hdnav.socket.socketPool.MyConnectionProvider;
import com.hdnav.socket.socketPool.SocketAdapter;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @ClassName SocketUtil
 * @Description TODO
 * @author liufm
 * @Date 2019/5/8 10:53
 * @Version 0.1
 **/
public class SocketUtil {

    /**
     *  通过命令调用信号揭示器进行信号揭示
     * @param command description 执行的命令
     */
    public static void  execCommand(String command){
        Assert.notNull(command,"THAT COMMAND IS NOT NULL");
        SocketAdapter connection = null;
            try {
                ConnectionProvider  conpool = MyConnectionProvider.newInstance();
                connection=conpool.getConnection();
                OutputStream os = connection.getOutputStream();
                os.write(hexStrToBinaryStr(command));
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
    }

    /**
     * description 将字符串转化成byte数组
     * @param hexString description 字符串
     * @return  byte[]
     */
    private static byte[] hexStrToBinaryStr(String hexString) {
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        int index = 0;
        byte[] bytes = new byte[len / 2];
        while (index < len) {
            String sub = hexString.substring(index, index + 2);
            bytes[index/2] = (byte)Integer.parseInt(sub,16);
            index += 2;
        }
        return bytes;
    }
}
