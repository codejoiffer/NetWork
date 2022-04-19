package com.nju.HttpServer.Http.Components;

/**
 * 接口：组成http请求和响应的部件
 **/
public interface Component {
    public String ToString();

    public byte[] ToBytes();
}
