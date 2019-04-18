package finley.gmair.controller;

import finley.gmair.model.resource.FileMap;
import finley.gmair.service.FileMapService;
import finley.gmair.service.TempFileMapService;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: PictureController
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/16 3:24 PM
 */
@RestController
@RequestMapping("/resource/image")
@PropertySource("classpath:resource.properties")
public class ImageController {
    private Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Value("${STORAGE_PATH}")
    private String tempDir;

    @Value("${IMAGE_URL}")
    private String imageBase;

    @Autowired
    private FileMapService fileMapService;

    @Autowired
    private TempFileMapService tempFileMapService;

    @PostMapping(value = "/upload")
    public ResultData upload(MultipartHttpServletRequest request) {
        ResultData result = new ResultData();
        MultipartFile file = request.getFile("image");
        String name = file.getOriginalFilename();
        String path = tempDir + File.separator + "image" + File.separator + new SimpleDateFormat("yyyyMMdd").format(new Date());
        File base = null;
        try {
            base = new File(path);
            if (!base.exists()) base.mkdirs();
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("存储文件夹创建失败，请稍后尝试");
            return result;
        }
        String filename = IDGenerator.generate("IMG") + name.substring(name.lastIndexOf('.'));
        File target = new File(path + File.separator + filename);
        try {
            file.transferTo(target);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("文件存储失败，请稍后尝试");
            return result;
        }
        String url = imageBase + File.separator + filename;
        FileMap temp = new FileMap(url, path, filename);
        ResultData response = tempFileMapService.createTempFileMap(temp);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("存储图片失败");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/image/{filename:.+}")
    public ResultData image(HttpServletResponse hsr, @PathVariable("filename") String filename) {
        ResultData result = new ResultData();
        //通过文件名称获取文件实际的存储路径
        Map<String, Object> condition = new HashMap<>();
        condition.put("fileName", filename);
        ResultData response = fileMapService.fetchFileMap(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("获取文件存储路径失败，请稍后尝试");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("当前没有该文件对应的存储路径");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("find pic by url");
        }
        //根据真实路径把图片里写到response里
        FileMap fileMap = ((List<FileMap>) response.getData()).get(0);
        String filePath = new StringBuffer(fileMap.getActualPath()).append(File.separator).append(fileMap.getFileName()).toString();
        hsr.setContentType("image/png");
        try {
            File file = new File(filePath);
            OutputStream os = hsr.getOutputStream();
            ImageIO.write(ImageIO.read(file), "png", hsr.getOutputStream());
            os.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

}
