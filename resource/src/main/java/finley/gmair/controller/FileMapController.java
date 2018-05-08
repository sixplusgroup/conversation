package finley.gmair.controller;

import com.netflix.discovery.converters.Auto;
import finley.gmair.model.resource.FileMap;
import finley.gmair.service.FileMapService;
import finley.gmair.service.TempFileMapService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/resource/filemap")
public class FileMapController {

    @Autowired
    private FileMapService fileMapService;

    @Autowired
    private TempFileMapService tempFileMapService;

    @RequestMapping(method = RequestMethod.POST, value = "/createpic")
    public ResultData createPicMap(String fileUrl) {
        ResultData result = new ResultData();
        //check empty input
        if (StringUtils.isEmpty(fileUrl)) {
            result.setDescription("url is empty");
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }

        result.setData(new StringBuffer(""));
        String[] urls = fileUrl.split(",");
        Map<String, Object> condition = new HashMap<>();
        for (int i = 0; i < urls.length; i++) {

            //从tempFileMap表里获取对应url的map
            condition.clear();
            condition.put("fileUrl", urls[i]);
            condition.put("blockFlag", false);
            ResultData response = tempFileMapService.fetchTempFileMap(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                continue;
            }

            //把获取到的map保存到fileMap表里
            String actualPath = ((List<FileMap>) response.getData()).get(0).getActualPath();
            String fileName = ((List<FileMap>) response.getData()).get(0).getFileName();
            FileMap fileMap = new FileMap(urls[i], actualPath, fileName);
            response = fileMapService.createFileMap(fileMap);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setData(new StringBuffer(urls[i] + ",") + result.getData().toString());
            }
        }
        result.setDescription("success to create the map for urls in data.");
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/pic/{filename:.+}")
    public ResultData getPicByUrl(HttpServletRequest request, HttpServletResponse response, @PathVariable("filename") String filename) {
        ResultData result=new ResultData();
        //通过fileUrl得到文件真实路径
        Map<String, Object> condition = new HashMap<>();
        condition.put("fileName",filename);
        ResultData res = fileMapService.fetchFileMap(condition);
        if(res.getResponseCode() == ResponseCode.RESPONSE_ERROR)
        {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now");
            return result;
        }
        else if(res.getResponseCode() == ResponseCode.RESPONSE_NULL)
        {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no pic found by url");
            return result;
        }
        else if(res.getResponseCode() == ResponseCode.RESPONSE_OK)
        {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("find pic by url");
        }

        //根据真实路径把图片里写到response里
        FileMap fileMap = ((List<FileMap>)res.getData()).get(0);
        String filePath = new StringBuffer(fileMap.getActualPath()).append(File.separator).append(fileMap.getFileName()).toString();
        response.setContentType("image/jpg");
        try {
            File f = new File(filePath);
            BufferedImage bi = ImageIO.read(f);
            OutputStream out = response.getOutputStream();
            ImageIO.write(bi, "jpg", out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
