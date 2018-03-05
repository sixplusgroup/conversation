package finley.gmair.util;

import com.alibaba.fastjson.JSON;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.ws.rs.core.MediaType;

public class MessageUtil {

    public static ResultData sendOne(String phone, String text) {
        ResultData result = new ResultData();
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api", MessageProperties.getValue("message_api_key")));
        WebResource webResource = client.resource(
                "http://sms-api.luosimao.com/v1/send.json");
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("mobile", phone);
        formData.add("message", new StringBuffer(text).append(MessageProperties.getValue("message_signature")).toString());
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
                post(ClientResponse.class, formData);
        System.out.println(JSON.toJSONString(response));
        return result;
    }

    public static ResultData sendGroup(String phones, String text) {
        ResultData result = new ResultData();
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api", MessageProperties.getValue("message_api_key")));
        WebResource webResource = client.resource(
                "http://sms-api.luosimao.com/v1/send_batch.json");
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("mobile_list", phones);
        formData.add("message", new StringBuffer(text).append(MessageProperties.getValue("message_signature")));
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
                post(ClientResponse.class, formData);
        System.out.println("haha" + JSON.toJSONString(response));
        return result;
    }
}
