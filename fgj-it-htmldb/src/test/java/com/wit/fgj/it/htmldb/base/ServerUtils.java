package com.wit.fgj.it.htmldb.base;

public class ServerUtils {

    private static int serverPort;

    /**
     * 返回全路径。
     *
     * @param path
     * @return
     */
    public static String getServerUri(String path) {
        String uri = "http://localhost:" + serverPort + "/fgj" + path;
        return uri;
    }

    public static void setServerPort(int port) {
        serverPort = port;
    }

}
