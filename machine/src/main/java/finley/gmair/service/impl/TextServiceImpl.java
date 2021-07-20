package finley.gmair.service.impl;

import finley.gmair.dao.TextDao;
import finley.gmair.service.TextService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author ：CK
 * @date ：Created in 2020/11/2 11:35
 * @description：
 */
@Service
public class TextServiceImpl implements TextService {

    @Autowired
    private TextDao textDao;

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        return textDao.query(condition);
    }
}
