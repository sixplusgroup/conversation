package finley.gmair.util;

import com.alibaba.fastjson.JSON;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;

public class MessageUtil {

    private static Logger logger = LoggerFactory.getLogger(MessageUtil.class);

    public static ResultData sendOne(String phone, String text) {
        ResultData result = new ResultData();
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api", MessageProperties.getValue("message_api_key")));
        WebResource webResource = client.resource(
                "http://sms-api.luosimao.com/v1/send.json");
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("mobile", phone);
        formData.add("message", text);
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
                post(ClientResponse.class, formData);
        logger.info(JSON.toJSONString(response));
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
        formData.add("message", text);
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
                post(ClientResponse.class, formData);
        logger.info(JSON.toJSONString(response));
        return result;
    }
}
