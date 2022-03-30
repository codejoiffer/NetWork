package RequestExecutor;

import Common.Template;
import Http.Components.Body;
import Http.Components.Headers;
import Http.Components.StatusLine;
import Http.HttpRequest;
import Http.HttpResponse;
import Common.StatusCode;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

// 什么是静态资源
// 静态资源是指在不同请求中访问到的数据都相同的静态文件。例如：图片、视频、网站中的文件（html、css、js）、软件安装包、apk文件、压缩包文件等
public class StaticResourceHandler extends BasicExecutor {

//    永久移动的资源 对应状态码301
//    301 Moved Permanently 永久移动。是指请求的资源已被永久的移动到新的URL，返回信息会包括新的URL，浏览器还会自动定向到新的URL。今后任何新的请求都应该使用新的URL来代替
    public static HashMap<String, String> MovedPermanentlyResource = new HashMap<>();
//    暂时移动的资源 对应状态码302
//    302 Found 临时移动。与301类似。但是资源只是临时被移动。客户端应该继续使用原有的URI
    public static HashMap<String, String> MovedTemporarilyResource = new HashMap<>();
//    所请求的资源未修改，服务器返回此状态码时，不会返回任何资源。客户端通常会缓存所访问过的资源。通过提供一个头信息指出客户端希望只返回在指定日期之后修改的资源
//    304状态码或许不应该认为是一种错误，而是对客户端有缓存情况下服务端的一种响应。
    public static HashMap<String, String> ModifiedTime = new HashMap<>();

    public StaticResourceHandler() {
        MovedPermanentlyResource.put("/movedPic.png", "/pic.png");
        MovedPermanentlyResource.put("/movedIndex.html", "/index.html");
        MovedTemporarilyResource.put("/movedPic2.png", "/pic.png");
        MovedTemporarilyResource.put("/movedIndex2.html", "/index.html");
    }

//    静态资源的判断
//    如何判断?
    public static boolean isStaticTarget(String target) {
        target = target.substring(target.lastIndexOf("/") + 1);
        return target.contains(".");
    }

    public HttpResponse handle(HttpRequest request) throws Exception{
        StatusLine statusLine = null;
        Headers headers = new Headers();
        Body body = new Body();
        String target = request.getStartLine().getTarget();

        String host = headers.getValue("Host");

//        发出301 302请求
        if (MovedPermanentlyResource.containsKey(target)) {
            return Template.generateStatusCode_301(MovedPermanentlyResource.get(target));
        }
        else if (MovedTemporarilyResource.containsKey(target)) {
            return Template.generateStatusCode_302(MovedTemporarilyResource.get(target));
        }
        else {
            statusLine = new StatusLine(1.1, 200, "OK");
        }

        if (target.endsWith(".html")) {
            headers.addHeader("Content-Type", "text/html");
        } else if (target.endsWith(".png")) {
            headers.addHeader("Content-Type", "image/png");
        }else if(target.endsWith(".js")){
            headers.addHeader("Content-Type", " text/javascript");
        }

        String path = target.substring(target.lastIndexOf("/") + 1);

        // add length
        File f = new File(path);
        headers.addHeader("Content-Length", Long.toString(f.length()));

        // add last modified
        Date fileLastModifiedTime = new Date(f.lastModified());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss z", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        System.out.println(sdf.format(fileLastModifiedTime));
        headers.addHeader("Last-Modified", sdf.format(fileLastModifiedTime));

//        这边要判断一下是不是304
        String time = request.getHeaders().getValue("If-Modified-Since");
        if (time != null){
            Date Limit =  sdf.parse(time);
            if(Limit.compareTo(fileLastModifiedTime) > 0){
                return Template.generateStatusCode_304();
            }
        }

//        不是304的化要从服务端把资源写到body里给客户端
        byte[] bytesArray = new byte[(int) f.length()];
        try {
            FileInputStream fis = new FileInputStream(f);
            fis.read(bytesArray);
            //read file into bytes[]
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpResponse(new StatusLine(1.1, StatusCode.NOT_FOUND.getCode(), "Not Found"), new Headers(), new Body());
        }


        body.setData(bytesArray);

        return new HttpResponse(statusLine, headers, body);
    }
}
