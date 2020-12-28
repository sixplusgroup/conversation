package finley.gmair.menu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MenuConfig {

    public static String createMenu(String token) {

        //关于果麦
        JSONObject gm_introduction = new JSONObject();
        gm_introduction.put("name", "认识果麦");
        gm_introduction.put("type", "view");
        try {
            gm_introduction.put("url", "https://mp.weixin.qq.com/s/bfAo_vzy_uOeRe49aHt0aw");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //产品目录
        JSONObject product_list = new JSONObject();
        product_list.put("name", "产品目录");
        product_list.put("type", "view");
        try {
            product_list.put("url", "http://www.gmair.net/wx.html");
        } catch (Exception e) {
            e.printStackTrace();
        }



        JSONObject assessment = new JSONObject();
        assessment.put("name", "新风机评测");
        assessment.put("type", "view");
        try {
            assessment.put("url", "https://mp.weixin.qq.com/mp/homepage?__biz=MzI5MjczMDk4Mw==&hid=1&sn=7a8c06e9f97688699c622d6cc9c623b0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject detector_assessment = new JSONObject();
        detector_assessment.put("name", "甲醛检测仪评测");
        detector_assessment.put("type", "view");
        try {
            detector_assessment.put("url", "https://mp.weixin.qq.com/mp/homepage?__biz=MzI5MjczMDk4Mw==&hid=2&sn=17c8285f24eb6d8ef91e1183f0364eb3");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject cases = new JSONObject();
        cases.put("name", "安装案例");
        cases.put("type", "view");
        try {
            cases.put("url", "http://mp.weixin.qq.com/s?__biz=MzI5MjczMDk4Mw==&mid=100002619&idx=1&sn=9984b1d96439da40c6c7ba5e59327b6b&chksm=6c7daa335b0a2325d70d40efd58b538520c0d7e41622ec306806d436022fc32b2179bd26f329#rd");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONArray gm = new JSONArray();
        gm.add(gm_introduction);
        gm.add(product_list);
        gm.add(assessment);
        gm.add(detector_assessment);
        gm.add(cases);

        JSONObject gm_menu = new JSONObject();
        gm_menu.put("name", "关于果麦");
        gm_menu.put("sub_button", gm);

        //我的空气
        JSONObject mine_device = new JSONObject();
        mine_device.put("name", "我的空气");
//        mine_device.put("type", "click");
//        mine_device.put("key", "gmair");
        mine_device.put("type", "view");
        try {
            mine_device.put("url", "https://reception.gmair.net/machine/list");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //售后服务
        JSONObject contact_us = new JSONObject();
        contact_us.put("name", "联系我们");
        contact_us.put("type", "view");
        try {
            contact_us.put("url", "https://mp.weixin.qq.com/s/h8MxaTEKHKbP5y7wA3AYZg");
        } catch (Exception e) {
            e.printStackTrace();
        }



        //试用申请
        JSONObject applicant = new JSONObject();
        applicant.put("name", "试用申请");
        applicant.put("type", "view");
        try {
            applicant.put("url", "http://www.gmair.net/acquire.html");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONArray after_support = new JSONArray();
        after_support.add(applicant);
        after_support.add(contact_us);

        JSONObject as_menu = new JSONObject();
        as_menu.put("name", "购买与服务");
        as_menu.put("sub_button", after_support);

        JSONArray buttons = new JSONArray();
        buttons.add(gm_menu);
        buttons.add(mine_device);
        buttons.add(as_menu);

        JSONObject menu = new JSONObject();
        menu.put("button", buttons);
        System.out.println(JSON.toJSONString(menu));
        String link = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + token;
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
            System.setProperty("sun.net.client.defaultReadTimeout", "30000");
            connection.connect();
            OutputStream os = connection.getOutputStream();
            os.write(menu.toString().getBytes());
            os.flush();
            os.close();

            InputStream is = connection.getInputStream();
            int size = is.available();
            byte[] bytes = new byte[size];
            is.read(bytes);
            String message = new String(bytes, "UTF-8");
            return "返回信息" + message;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "创建菜单失败";


    }

    public static String deleteMenu(String token) {
        String link = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" + token;
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
            System.setProperty("sun.net.client.defaultReadTimeout", "30000");
            connection.connect();
            OutputStream os = connection.getOutputStream();
            os.flush();
            os.close();
            InputStream is = connection.getInputStream();
            int size = is.available();
            byte[] bytes = new byte[size];
            is.read(bytes);
            String message = new String(bytes, "UTF-8");
            return "返回信息:" + message;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "删除菜单失败";
    }

    public static void main(String[] args) {
        String token = "";
        String deleteMessage = MenuConfig.deleteMenu(token);
        System.out.println("删除操作: " + deleteMessage);
        String createMessage = MenuConfig.createMenu(token);
        System.out.println("创建操作" + createMessage);
    }
}