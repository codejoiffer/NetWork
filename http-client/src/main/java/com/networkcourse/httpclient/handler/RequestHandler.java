package com.networkcourse.httpclient.handler;

import com.networkcourse.httpclient.client.*;
import com.networkcourse.httpclient.exception.InvalidHttpRequestException;
import com.networkcourse.httpclient.exception.MissingHostException;
import com.networkcourse.httpclient.exception.UnsupportedHostException;
import com.networkcourse.httpclient.history.History;
import com.networkcourse.httpclient.message.HttpRequest;
import com.networkcourse.httpclient.message.HttpResponse;
import com.networkcourse.httpclient.message.component.commons.Header;
import com.networkcourse.httpclient.message.component.commons.MessageHeader;
import com.networkcourse.httpclient.message.component.commons.URI;
import com.networkcourse.httpclient.message.component.response.ResponseLine;
import com.networkcourse.httpclient.message.factory.RedirectResponseFactory;
import com.networkcourse.httpclient.utils.ByteReader;
import com.networkcourse.httpclient.utils.ChunkReader;
import com.networkcourse.httpclient.utils.InputStreamReaderHelper;
import com.networkcourse.httpclient.utils.TimeUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * @author fguohao
 * @date 2021/05/30
 */
public class RequestHandler {

    private ClientRedirectCache clientRedirectCache ;
    private ClientModifiedCache clientModifiedCache ;
    private History history;

    public RequestHandler(ClientRedirectCache clientRedirectCache, ClientModifiedCache clientModifiedCache,History history) {
        this.clientRedirectCache = clientRedirectCache;
        this.clientModifiedCache = clientModifiedCache;
        this.history = history;
    }



    public HttpRequest handle(HttpRequest httpRequest) throws MissingHostException, UnsupportedHostException, URISyntaxException, IOException, ParseException, InvalidHttpRequestException {
        //未提供有效的request请求，抛出异常
        if(httpRequest==null){
            throw new InvalidHttpRequestException("invaild httpRequest, the request is null");
        }

        if(httpRequest.getMessageHeader().get(Header.Content_Length)==null){
            httpRequest.getMessageHeader().put(Header.Content_Length,String.valueOf(httpRequest.getMessageBody().getBody().length));
            history.addLog("Missing Length was auto added, length="+String.valueOf(httpRequest.getMessageBody().getBody().length),History.LOG_LEVEL_WARNING);
        }

        //refering redirect
//        查一下有没有重定向的
        httpRequest = findRedirectCache(httpRequest);

        //refering localStorage
//        询问服务端有没有更新过存在本地的资源
        httpRequest = findLastModifiedCache(httpRequest);

        return httpRequest;


    }

//    这边应该是客户端收到了301请求 之后的每次请求都使用重定向之后的新的url请求
    private HttpRequest findRedirectCache(HttpRequest httpRequest){
        String host = httpRequest.getHost();
        String path = httpRequest.getPath();
        URI newURI = clientRedirectCache.getRedirect(host, path);
//        如果由新的URI 需要更新 没有的话直接返回
        if(newURI!=null){
            history.addHistory(httpRequest, RedirectResponseFactory.getINSTANCE().getRedirectResponse(newURI.toString()));
            httpRequest = httpRequest.clone();
            httpRequest.getRequsetLine().setRequestURI(newURI.getPath());
            httpRequest.getMessageHeader().put(Header.Host, newURI.getHost());
            history.addLog("Successfully find Redirect Cache entry, oldPath="+host+path+" , newPath="+newURI.toString(),History.LOG_LEVEL_INFO);
        }
        return httpRequest;
    }

    private HttpRequest findLastModifiedCache(HttpRequest httpRequest){
        String host = httpRequest.getHost();
        String path = httpRequest.getPath();
        Long timestamp =  clientModifiedCache.getModifiedTime(host,path);
        if(timestamp!=null){
            history.addLog("Successfully find Last Modified Cache entry, Path="+host+path+" , modifiedTime="+TimeUtil.toTimeString(timestamp),History.LOG_LEVEL_INFO);
//            If-Modified-Since是标准的HTTP请求头标签，在发送HTTP请求时，把浏览器端缓存页面的最后修改时间一起发到服务器去，服务器会把这个时间与服务器上实际文件的最后修改时间进行比较。
//            如果时间一致，那么返回HTTP状态码304（不返回文件内容），客户端接到之后，就直接把本地缓存文件显示到浏览器中。
//            如果时间不一致，就返回HTTP状态码200和新的文件内容，客户端接到之后，会丢弃旧文件，把新文件缓存起来，并显示到浏览器中。
            httpRequest.getMessageHeader().put(Header.If_Modified_Since, TimeUtil.toTimeString(timestamp));
        }
        return httpRequest;
    }


}
