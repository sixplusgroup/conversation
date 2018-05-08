package finley.gmair.scheduler.installation;

import finley.gmair.model.resource.FileMap;
import finley.gmair.service.PicService;
import finley.gmair.service.TempFileMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InstallationScheduler {

    @Autowired
    private TempFileMapService tempFileMapService;

    @Autowired
    private PicService picService;

    @Scheduled(cron = "0 0 0 0/7 * ?")
    //@Scheduled(cron = "0/10 * * * * ?")
    public void deleteInvalid() {

        System.out.println("start--------------------");

        //删除installation模块的pic表中的临时记录
        List<Object> tempfilemapList = (ArrayList<Object>) (tempFileMapService.getInvalidMap()).getData();
        if (tempfilemapList == null) {
            System.out.println("暂时没有需要删除的临时记录");
            return;
        }
        Map<String, Object> condition = new HashMap<>();
        for (Iterator it = tempfilemapList.iterator(); it.hasNext(); ) {
            condition.put("picAddress", ((LinkedHashMap<String, String>) it.next()).get("fileUrl"));
            picService.deletePic(condition);
        }

        //调用resource接口,删除无效TempFileMap记录和对应的图片文件
        tempFileMapService.deleteInValidPicAndMap();
    }
}
