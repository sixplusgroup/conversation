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

    private long checkTime = 0;
    //1000*60*60*24*7;
    //@Scheduled(cron = "0 0 0 0/7 * ?")
    @Scheduled(cron = "0/30 * * * * ?")
    public void deleteUselessPic() {
        System.out.println("start--------------------");

        //删除表路径对应文件
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        List<FileMap> filemapList = (List<FileMap>) (fileMapService.fetchFileMap(condition)).getData();
        List<FileMap> tempfilemapList = (List<FileMap>) (tempFileMapService.fetchTempFileMap(condition)).getData();
        for (FileMap f1 : tempfilemapList) {
            if(new Timestamp(System.currentTimeMillis()).getTime()-f1.getCreateAt().getTime() > checkTime ) {
                boolean notfind = true;
                for (FileMap f2 : filemapList) {
                    if (f1.getFileUrl().equals(f2.getFileUrl())) {
                        notfind = false;
                        break;
                    }
                }
                if (notfind) {
                    String  actualPath= f1.getActualPath()+File.separator+f1.getFileName();
                    System.out.println("start to delete "+actualPath);
                    boolean successToDelete = FileUtil.deleteFile(actualPath);
                    if (successToDelete)
                        System.out.println("success to delete");
                    else
                        System.out.println("fail to delete");
                }
            }
        }

        //暂时还存在问题,会把最近七天的记录删掉.
        //删除表中记录
        ResultData response = tempFileMapService.deleteTempFileMap();
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            System.out.println("success!");
        } else {
            System.out.println("fail!");
        }

        System.out.println("end--------------------");
    }


}

