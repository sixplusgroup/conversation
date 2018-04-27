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

    //private final static String wechat_appid = "wxc23d6a94677bb85f";

    //private final static String wechat_secret = "991f23ba9d364748fe35acbb52d1f68a";
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

        JSONObject gm_guidance = new JSONObject();
        gm_guidance.put("name", "新风指南");
        gm_guidance.put("type", "view");
        try {
            gm_guidance.put("url", "https://mp.weixin.qq.com/s/4G9z1b0d33f17MWcl2ln-w");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject assessment = new JSONObject();
        assessment.put("name", "产品评测");
        assessment.put("type", "view");
        try {
            assessment.put("url", "https://mp.weixin.qq.com/s/p9hnEQwLxIc5BASZhOhivA");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject cases = new JSONObject();
        cases.put("name", "安装案例");
        cases.put("type", "view");
        try {
            cases.put("url", "http://mp.weixin.qq.com/s/ezFPKEsdL1dtUc-qUnSICw");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONArray gm = new JSONArray();
        gm.add(gm_introduction);
        gm.add(gm_guidance);
        gm.add(assessment);
        gm.add(cases);

        JSONObject gm_menu = new JSONObject();
        gm_menu.put("name", "关于果麦");
        gm_menu.put("sub_button", gm);

        //我的空气
        JSONObject mine_device = new JSONObject();
        mine_device.put("name", "我的空气");
        mine_device.put("type", "click");
        mine_device.put("key", "gmair");

        //售后服务
        JSONObject contact_us = new JSONObject();
        contact_us.put("name", "联系我们");
        contact_us.put("type", "view");
        try {
            contact_us.put("url", "https://mp.weixin.qq.com/s/h8MxaTEKHKbP5y7wA3AYZg");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject online_purchase = new JSONObject();
        online_purchase.put("name", "在线购买");
        online_purchase.put("type", "view");
        try {
            online_purchase.put("url", "https://h5.youzan.com/v2/feature/Xe2Ffvvcq6");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //卡券兑换
        JSONObject card_exchange = new JSONObject();
        card_exchange.put("name", "卡券兑换");
        card_exchange.put("type", "view");
        try{
            card_exchange.put("url", "http://one.fw1860.com/recinzaixiantihuoxitongimages/wxth/njgm/wxth.aspx");
        }catch (Exception e) {
            e.printStackTrace();
        }

        JSONArray after_support = new JSONArray();
        after_support.add(contact_us);
        after_support.add(online_purchase);
        after_support.add(card_exchange);

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