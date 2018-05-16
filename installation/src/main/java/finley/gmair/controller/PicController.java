package finley.gmair.controller;

import finley.gmair.model.installation.Pic;
import finley.gmair.model.installation.Snapshot;
import finley.gmair.service.PicService;
import finley.gmair.service.SnapshotService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/installation/pic")
public class PicController {
    @Autowired
    PicService picService;

    @Autowired
    SnapshotService snapshotService;

    //从数据表install_pic中拉取重复图片的信息
    @RequestMapping(method = RequestMethod.GET, value = "copy")
    public ResultData copy() {
        ResultData result = new ResultData();

        Map<String, Object> condition = new HashMap<>();
        condition.put("copyFlag", true);
        condition.put("blockFlag", false);
        ResultData response = picService.fetchPic(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to duplicate pic");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no duplicate pic found");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,please try again later.");
        }
        return result;
    }

    //根据snapshotId查是否重复图片
    @RequestMapping(method = RequestMethod.GET, value = "duplicate")
    public ResultData duplicate(String snapshotId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("copyFlag", 1);
        condition.put("snapshotId", snapshotId);
        condition.put("blockFlag", false);
        ResultData response = picService.fetchPic(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find pic with snapshotId");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now");
            return result;
        }

        //重复图片List
        List<Pic> picList = (List<Pic>) response.getData();
        List<String> picCopyWho = new ArrayList<>();
        for (Pic pic : picList) {
            //首先找到重复图片的第一个出现的..
            condition.clear();
            condition.put("md5", pic.getPicMd5());
            condition.put("copyFlag", 0);
            condition.put("blockFlag", false);
            response = picService.fetchPic(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK)
                continue;
            Pic firstPic = ((List<Pic>) response.getData()).get(0);
            //找到该重复图片的AssignId
            condition.clear();
            condition.put("snapshotId", firstPic.getSnapshotId());
            condition.put("blockFlag", false);
            response = snapshotService.fetchSnapshot(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK)
                continue;
            picCopyWho.add(((List<Snapshot>) response.getData()).get(0).getAssignId());
        }
        result.setData(picList);
        //TODO
        //还没有把重复图片放进去
        return result;
    }
}
