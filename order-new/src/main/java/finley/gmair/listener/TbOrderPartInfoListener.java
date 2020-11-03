package finley.gmair.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import finley.gmair.model.dto.TbOrderPartInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Bright Chan
 * @date: 2020/11/3 14:16
 * @description: TbOrderPartInfoListener
 */
public class TbOrderPartInfoListener extends AnalysisEventListener<TbOrderPartInfo> {

    private List<TbOrderPartInfo> readData = new ArrayList<>();

    @Override
    public void invoke(TbOrderPartInfo data, AnalysisContext context) {
        data.setPhone(getTruePhoneNumber(data.getPhone()));
        readData.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    public List<TbOrderPartInfo> getReadData() {
        return this.readData;
    }

    // 去掉联系电话中的非法字符
    private String getTruePhoneNumber(String phone) {
        StringBuilder res = new StringBuilder();

        if (phone == null) return res.toString();

        for (int i = 0; i < phone.length(); i++) {
            char cur = phone.charAt(i);
            if (cur >= '0' && cur <= '9') res.append(cur);
        }

        return res.toString();
    }
}
