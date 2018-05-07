package finley.gmair.scheduler.resource;

import finley.gmair.model.resource.FileMap;
import finley.gmair.service.FileMapService;
import finley.gmair.service.TempFileMapService;
import finley.gmair.util.FileUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ResourceScheduler {
    @Autowired
    private TempFileMapService tempFileMapService;

    @Autowired
    private FileMapService fileMapService;

    //@Scheduled(cron = "0 0 0 0/7 * ?")
    @Scheduled(cron = "0/30 * * * * ?")
    public void deleteUselessPic() {
        System.out.println("start--------------------");

        //删除大于7天的临时文件
        Map<String, Object> condition = new HashMap<>();
        condition.put("createAt", "2018-1-1");
        condition.put("blockFlag",false);
        List<FileMap> tempfilemapList = (List<FileMap>) (tempFileMapService.fetchTempFileMap(condition)).getData();
        if (tempfilemapList != null) {
            for (FileMap f1 : tempfilemapList) {
                String actualPath = f1.getActualPath() + File.separator + f1.getFileName();
                System.out.println("start to delete " + actualPath);
                FileUtil.deleteFile(actualPath);
            }
        }

        //删除表中大于7天的临时数据表记录
        condition.clear();
        condition.put("createAt", "2018-1-1");
        ResultData response = tempFileMapService.deleteTempFileMap(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            System.out.println("删除表记录成功!");
        } else {
            System.out.println("删除表记录失败!");
        }
    }

}

