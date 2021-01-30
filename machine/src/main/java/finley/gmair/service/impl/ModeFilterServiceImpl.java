package finley.gmair.service.impl;

import finley.gmair.dao.FilterDao;
import finley.gmair.dao.ModelFilterDao;
import finley.gmair.service.ModelFilterService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ModeFilterServiceImpl implements ModelFilterService {

    @Resource
    private ModelFilterDao modelMaterialDao;

    @Resource
    private FilterDao filterDao;

    public ResultData fetch(String modelId) {
        ResultData result = new ResultData();
        List<String> filterIDs = modelMaterialDao.selectFilerIdByModelId(modelId);
        if (filterIDs == null || filterIDs.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No related filter link information found.");
            return result;
        }

        List<String> filterLinks = new ArrayList<>(filterIDs.size());
        for (String s : filterIDs) {
            filterLinks.add(filterDao.selectLinkById(s));
        }

        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(filterLinks);
        result.setDescription("Success to obtain the relevant filter link accord to the modelId");
        return result;
    }
}
