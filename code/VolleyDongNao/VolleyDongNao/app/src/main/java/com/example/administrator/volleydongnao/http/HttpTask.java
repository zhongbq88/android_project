package com.example.administrator.volleydongnao.http;

import com.alibaba.fastjson.JSON;
import com.example.administrator.volleydongnao.http.interfaces.IHttpListener;
import com.example.administrator.volleydongnao.http.interfaces.IHttpService;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.FutureTask;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

public class HttpTask<T> implements Runnable {
    private IHttpService httpService;
    private FutureTask futureTask;
    public HttpTask(RequestHodler<T> requestHodler)
    {
        httpService=requestHodler.getHttpService();
        httpService.setHttpListener(requestHodler.getHttpListener());
        httpService.setUrl(requestHodler.getUrl());
        //增加方法
        IHttpListener httpListener=requestHodler.getHttpListener();
        httpListener.addHttpHeader(httpService.getHttpHeadMap());
        try {
            T request=requestHodler.getRequestInfo();
            if(request!=null)
            {
                String requestInfo= JSON.toJSONString(request);
                httpService.setRequestData(requestInfo.getBytes("UTF-8"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        httpService.excute();
    }
    /**
     * 新增方法
     */
    public void start()
    {
        futureTask=new FutureTask(this,null);
        try {
            ThreadPoolManager.getInstance().execte(futureTask);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 新增方法
     */
    public  void pause()
    {
        httpService.pause();
        if(futureTask!=null)
        {
            ThreadPoolManager.getInstance().removeTask(futureTask);
        }

    }
}
