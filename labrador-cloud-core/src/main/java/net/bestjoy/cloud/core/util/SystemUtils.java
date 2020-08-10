package net.bestjoy.cloud.core.util;

import lombok.SneakyThrows;
import net.bestjoy.cloud.core.error.Errors;
import net.bestjoy.cloud.core.error.SysException;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 系统工具
 *
 * @author ray
 */
public class SystemUtils {

    /**
     * 通过地址信息获取mac信息
     */
    public static String getMac(InetAddress ia) {
        try {
            //获取网卡，获取地址
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            if (mac == null) {
                return "";
            }
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                //字节转换为整数
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                if (str.length() == 1) {
                    sb.append("0" + str);
                } else {
                    sb.append(str);
                }
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            throw new SysException(Errors.Sys.SYS_ERROR, e);
        }
    }

    /**
     * 系统中存在多个网卡的情况下获取第一个ip地址
     */
    @SneakyThrows
    public static InetAddress getFirstAddress() {
        if (getAddress().isEmpty()) {
            return Inet4Address.getByName("127.0.0.1");
        }
        return getAddress().get(0);
    }

    /**
     * 获取第一个mac
     */
    public static String getFirstMac() {
        return getMac(getFirstAddress());
    }

    /**
     * 获取系统地址信息
     */
    public static List<InetAddress> getAddress() {
        try {
            List<InetAddress> ips = new ArrayList<>();
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip instanceof Inet4Address) {
                        if (!"127.0.0.1".equals(ip.getHostAddress())) {
                            ips.add(ip);
                        }
                    }
                }
            }
            return ips;
        } catch (Exception e) {
            throw new SysException(Errors.Sys.SYS_ERROR, e);
        }
    }

    /**
     * 获取环境变量
     */
    public static String getEnv(String key) {
        return System.getProperty(key);
    }

    /**
     * 获取第一个IP
     */
    public static String getFirstIp() {
        return getFirstAddress().toString();
    }
}
