package finley.gmair.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import finley.gmair.model.dto.JdConsigneeExcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Bright Chan
 * @date: 2020/11/3 14:16
 * @description:
 */
public class JdConsigneeExcelUploadListener extends AnalysisEventListener<JdConsigneeExcel> {

    private List<JdConsigneeExcel> readData = new ArrayList<>();

    @Override
    public void invoke(JdConsigneeExcel data, AnalysisContext context) {
        data.setPhone(getTruePhoneNumber(data.getPhone()));
        readData.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    public List<JdConsigneeExcel> getReadData() {
        return this.readData;
    }

    // 去掉联系电话中的非法字符
    private String getTruePhoneNumber(String phone) {
        StringBuilder res = new StringBuilder();
        if (phone == null) {
            return res.toString();
        }
        for (int i = 0; i < phone.length(); i++) {
            char cur = phone.charAt(i);
            if (cur >= '0' && cur <= '9') {
                res.append(cur);
            }
        }
        return res.toString();
    }
}
