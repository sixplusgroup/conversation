import com.alibaba.fastjson.JSONObject;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.request.im.ImPopSessionlistGetRequest;
import com.jd.open.api.sdk.response.im.ChatSession;
import com.jd.open.api.sdk.response.im.ChatSessionPage;
import com.jd.open.api.sdk.response.im.ImPopSessionlistGetResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class get {
    public static String SERVER_URL="http://api.jd.com/routerjson";
    public static String accessToken = "fd764b94465e45328315d1bf12029330riot";
    public static String appKey = "6861B1CF2C2FA7D763AA34F0254E6BAB";
    public static String appSecret = "f9f7f2f76f62497cb74875b930295ca9";
    public static String dateFormat = "yyyy-MM-dd HH:mm:ss";
    public static String path = "chatLog.txt";
    public static String defalutCharset ="UTF-8";

    public static DateFormat dateFormat2 = new SimpleDateFormat(dateFormat);

    public static void main(String[] args) {
        Calendar beginCal=Calendar.getInstance();
        for (int i=0;i<90;++i){
            getOneDay(beginCal.getTime());
            System.out.println(beginCal.getTime());
            beginCal.add(Calendar.DATE,-1);
        }
    }

    /**
     * 获得一天的聊天记录，定时执行这个函数即可
     *
     * @param date
     */
    public static void getOneDay(Date date){
        getAll(dateFormat2.format(new Date(date.getTime() - 1 * 24 * 60 * 60 * 1000)),dateFormat2.format(date));
    }




    /**
     * 获得开始到结束时间顾客的所有的聊天记录
     *
     * @param beginTime 需要查询数据的开始日期（取Date的年月日部分）
     * @param endTime 需要查询数据的截止日期（取Date的年月日部分）
     *                查询起止日期相差不能大于7天，查询的日期距现在不能大于3个月
     * @return 返回聊天记录列表
     */
    public static void getAll(String beginTime , String endTime){
        System.out.println(beginTime+" "+endTime);
        //获得指定时间段中所有的对话列表信息
        ImPopSessionlistGetResponse response = getSessionList(beginTime,endTime);
        ChatSessionPage chatSessionPage = response.getChatSessionPage();
        if(!isNull(chatSessionPage)){
            //获得每个客户在指定时间段的会话信息
            Set<String> myset=new HashSet<String>();
            for (ChatSession chatSession: chatSessionPage.getChatSessionList()){
                myset.add(chatSession.getCustomer());
            }
            System.out.println(myset.size());

            int time =1 ;
            try{
                BufferedWriter out = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(path,true)));
                for (String name:myset){
                    String  chatLog = getChatByAPI(beginTime,endTime,name);
                    if(!isNull(chatLog)) {
                        out.write(chatLog+"\n");
                        //todo 将其缓存到数据库
                    }
                }
                out.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取会话列表，见https://jos.jd.com/apilist?apiGroupId=53&apiId=12771&apiName=jingdong.im.pop.sessionlist
     * 日期格式为 yyyy-MM-dd HH:mm:ss
     *
     * @param beginTime 需要查询数据的开始日期（取Date的年月日部分）
     * @param endTime 需要查询数据的截止日期（取Date的年月日部分）
     *                查询起止日期相差不能大于7天，查询的日期距现在不能大于3个月
     */
    public static ImPopSessionlistGetResponse getSessionList(String beginTime , String endTime){
        try {
            JdClient client = new DefaultJdClient(SERVER_URL, accessToken, appKey, appSecret);
            ImPopSessionlistGetRequest request = new ImPopSessionlistGetRequest();

            request.setStartTime(dateFormat2.parse(beginTime));
            request.setEndTime(dateFormat2.parse(endTime));
            return client.execute(request);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获得某个顾客指定时间段的聊天记录
     *
     * @param beginTime
     * @param endTime
     * @param customer
     * @return
     */
    public static String getChatByAPI(String beginTime , String endTime, String customer){
        JSONObject object = new JSONObject();
        object.put("startTime",beginTime);
        object.put("endTime",endTime);
        object.put("customer",customer);

        String url_string =getUrl(object.toJSONString(),"jingdong.im.pop.chatlog.get","2.0");

//        System.out.println(url_string);

        try {
            return excuteUrl(url_string);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 执行拼装好的api调用url，返回结果
     *
     * @param url_string
     * @return
     * @throws Exception
     */
    public static String excuteUrl(String url_string) throws Exception {
        URL url = new URL(url_string);
        URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(uri);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String responseContent = EntityUtils.toString(entity, defalutCharset);
//        System.out.println(responseContent);
        response.close();
        httpClient.close();
        return responseContent;
    }


    /**
     *   授权API调用URL拼接
     *
     * @param object 参数
     * @param method 调用的方法
     * @param version 方法版本
     * @return
     */
    public static String getUrl(String object,String method,String version){
        Date time = new Date();
        String timeStamp = (new SimpleDateFormat(dateFormat).format(time));
        String baseUrl = "https://api.jd.com/routerjson";

        String url_string =baseUrl
                +"?360buy_param_json="+object
                +"&access_token="+accessToken
                +"&app_key="+appKey
                +"&method="+method
                +"&v="+version
                +"&timestamp="+timeStamp+"&sign=D70825340F4084360B9362B60DFD7930";
        return url_string;
    }



    /**
     * 判断对象为空的工具方法
     * @param obj
     * @return
     */
    public static boolean isNull(Object obj){
        if(obj == null) {
            return true;
        }
        return false;
    }
}
