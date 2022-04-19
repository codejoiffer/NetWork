package com.nju.HttpServer.Http.Components;

import java.nio.charset.StandardCharsets;

public class Body implements Component{

    byte[] data;

    public Body(){
        this.data = new byte[0];
    }

    public Body(String data) {
        this.data = data.getBytes(StandardCharsets.UTF_8);
    }

    public Body(byte[] data){
        this.data = data;
    }

    public void setData(String s){
        this.data = s.getBytes(StandardCharsets.UTF_8);
    }

    public void setData(byte[] b){
        this.data = b;
    }

    public void append(String s){
        String tmp = new String(this.data);
        this.data = (tmp + s).getBytes();
    }

    @Override
    public String ToString() {
        return new String(data);
    }

    @Override
    public byte[] ToBytes() {
//        byte[] ret = new byte[data.length + 1];
//        System.arraycopy(data, 0, ret, 0, data.length);
//        ret[data.length] = '\n';
//        return ret;
        return data;
    }

}
