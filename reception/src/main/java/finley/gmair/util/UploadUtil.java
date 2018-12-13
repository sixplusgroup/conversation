package finley.gmair.util;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class UploadUtil {
    //利用HttpClient上传文件
    //uploadUrl 服务器接收接口的完整url
    //filePath 本地文件的路径
    //receiveFileName 服务器接收接口文件参数的名字
    public static void uploadImage(String uploadUrl, String filePath, String receiveFileName){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(uploadUrl);
        File file = new File(filePath);
        if(!file.exists()){//判断文件是否存在
            System.out.print("文件不存在");
            return;
        }
        FileBody bin = new FileBody(file, ContentType.create("image/png", Consts.UTF_8));//创建图片提交主体信息
        HttpEntity entity = MultipartEntityBuilder
                .create()
                .setCharset(Charset.forName("utf-8"))
                .addPart(receiveFileName, bin)//添加到请求
                .build();
        httpPost.setEntity(entity);
        HttpResponse response= null;//发送Post,并返回一个HttpResponse对象
        try {
            response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode()==200) {//如果状态码为200,就是正常返回
                String result = EntityUtils.toString(response.getEntity());
                System.out.print(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(response);
        System.out.print("结束");
    }
}
