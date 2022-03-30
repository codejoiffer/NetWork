package RequestExecutor;

import Common.Template;
import Http.Components.Body;
import Http.Components.Headers;
import Http.Components.StatusLine;
import Http.HttpRequest;
import Http.HttpResponse;

import java.util.HashMap;
// 登录过程和注册过程基本一样
// 代码复制粘贴一下就可以
public class LoginExecutor extends BasicExecutor{

    public LoginExecutor(){
        this.url = "/login";
        this.method = "post";
    }
//    处理登录请求
    @Override
    public HttpResponse handle(HttpRequest request) {
        HashMap<String, String> db = RegisterExecutor.db;
        HttpResponse response = null;
        Headers headers = request.getHeaders();
        String contentType = headers.getValue("Content-Type").split(";")[0].trim();
        Body body = request.getBody();

        if(!contentType.equals("application/x-www-form-urlencoded")){
            response = new HttpResponse(new StatusLine(1.1, 400, "Bad Request"), new Headers(), new Body());
            return response;
        }

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
        if(username == null || password == null){
            response = new HttpResponse(new StatusLine(1.1, 400, "Bad Request"), new Headers(), new Body());
        }else {

            if (db.containsKey(username) && db.get(username).equals(password)) {
                String hint = "You have successfully login in!";
                response = Template.generateStatusCode_200(hint);
            } else {
                String hint = "login failed";
                response = Template.generateStatusCode_200(hint);
            }
        }

        return response;
    }
}
