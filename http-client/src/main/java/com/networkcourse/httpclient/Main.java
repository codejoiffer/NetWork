package com.networkcourse.httpclient;

import com.networkcourse.httpclient.client.Client;
import com.networkcourse.httpclient.client.ClientPool;
import com.networkcourse.httpclient.exception.MissingHostException;
import com.networkcourse.httpclient.exception.UnsupportedHostException;
import com.networkcourse.httpclient.history.History;
import com.networkcourse.httpclient.message.HttpRequest;
import com.networkcourse.httpclient.message.HttpResponse;
import com.networkcourse.httpclient.message.component.commons.Header;
import com.networkcourse.httpclient.message.component.commons.MessageBody;
import com.networkcourse.httpclient.message.component.commons.MessageHeader;
import com.networkcourse.httpclient.message.component.request.Method;
import com.networkcourse.httpclient.message.component.request.RequsetLine;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 使用api接口进行调用
 * @author fguohao
 * @date 2021/05/28
 */
public class Main {
    public static void main(String[] args) throws ParseException, URISyntaxException {

        RequsetLine requsetLine = new RequsetLine(Method.GET,"/");
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.put(Header.Host,"www.baidu.com");
        messageHeader.put(Header.Accept,"*/*");
        messageHeader.put(Header.Connection,"keep-alive");
        messageHeader.put(Header.Accept_Encoding,"gzip, deflate, br");
        MessageBody messageBody= new MessageBody();
        HttpRequest httpRequest = new HttpRequest(requsetLine,messageHeader,messageBody);
        Client client = new Client();
        try {
            HttpResponse httpResponse =client.sendHttpRequest(httpRequest);
            httpResponse.saveBody("./123.html");
        } catch (MissingHostException e) {
            e.printStackTrace();
        } catch (UnsupportedHostException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
