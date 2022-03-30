package RequestExecutor;

import Common.Template;
import Http.Components.Body;
import Http.Components.Headers;
import Http.HttpRequest;
import Http.HttpResponse;

import java.util.HashMap;

public class RegisterExecutor extends BasicExecutor{

    public static HashMap<String, String> db = new HashMap<>();

    public RegisterExecutor(){
        this.url = "/register";
        this.method = "post";
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        HashMap<String, String> db = RegisterExecutor.db;
        HttpResponse response = null;
        Headers headers = request.getHeaders();
//        得到请求数据类型和请求数据
        String contentType = headers.getValue("Content-Type").split(";")[0].trim();
        Body body = request.getBody();
//         application/x-www-form-urlencoded：是最常见的 POST 提交数据的方式，浏览器的原生表单如果不设置 enctype 属性，那么最终就会以 application/x-www-form-urlencoded 方式提交数据，它是未指定属性时的默认值
        if(!contentType.equals("application/x-www-form-urlencoded")){
//            对应的是405请求
//            405 错误经常和 POST 方法同时出现。 您可能在您的网站上尝试引入某种输入表格，但并非所有的互联网服务供应商 (ISPs) 都 允许处理该表格所需的 POST 方法。
            response = Template.generateStatusCode_405();
            return response;
        }

//        post请求的数据像这样: username=XXX&password=XXX
        String[] key_val = body.ToString().split("&");
        assert (key_val.length == 2);
        String username = null;
        String password = null;
        for(int i = 0; i < key_val.length; i++){
            String[] tmp = key_val[i].split("=");
            assert (tmp.length == 2);
            if(tmp[0].equals("username")){
                username = tmp[1].trim();
            }else if (tmp[0].equals("password")){
                password = tmp[1].trim();
            }
        }
//        啥都没有 405
        if(username == null || password == null){
            response = Template.generateStatusCode_405();
        }else {
//            注册成功 放到数据库中
            if (!db.containsKey(username)) {
                db.put(username, password);
                String hint = "You have successfully register!";
                response = Template.generateStatusCode_200(hint);
            } else {
                String hint = "You have successfully register!";
                response = Template.generateStatusCode_200(hint);
            }
        }
//        最后返回http响应
        return response;
    }
}
