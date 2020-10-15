package com.example.communitytest.provider;

import com.alibaba.fastjson.JSON;
import com.example.communitytest.dto.AccessTokenDTO;
import com.example.communitytest.dto.GitHubUser;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class GitHubProvider {
    Logger logger=LoggerFactory.getLogger(GitHubProvider.class);

    public String getAccessToken(AccessTokenDTO accessTokenDTO) {


        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
            logger.error("getAccessToken error,{}", accessTokenDTO, e);
        }
        return null;
    }

    public GitHubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request=new Request.Builder()
                .url("https://api.github.com/user?access_token"+accessToken)
                .build();
        try{
            Response response=client.newCall(request).execute();
            String string = response.body().string();
            GitHubUser gitHubUser = JSON.parseObject(string, GitHubUser.class);
            return gitHubUser;

        }catch (IOException e){
            logger.error("getUser error,{}", accessToken, e);
        }
        return null;
    }
}
