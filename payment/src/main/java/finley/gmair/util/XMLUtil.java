package finley.gmair.util;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLUtil {
    public static Map<String, String> doXMLParse(String strxml) {
        if (StringUtils.isBlank(strxml)) {
            return null;
        }
        Map<String, String> map = new TreeMap<>();
        SAXReader reader = new SAXReader();
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(strxml.getBytes());
            InputStreamReader iStreamReader= new InputStreamReader(byteArrayInputStream, "UTF-8");
            Document document = reader.read(iStreamReader,"UTF-8");
            // 通过document对象获取根节点xmlEle
            Element xmlEle = document.getRootElement();
            // 通过element对象的elementIterator方法获取迭代器
            Iterator<Element> it = xmlEle.elementIterator();
            // 遍历迭代器，获取根节点中的信息
            while (it.hasNext()) {
                Element param = it.next();
                map.put(param.getName(), param.getStringValue());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();;
        }
        return map;
    }
}
