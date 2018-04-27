package finley.gmair.company.SF;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SfService {

    @Value("${SFappId}")
    private String appId;

    @Value("${SFappKey}")
    private String appKey;

    public void test(){
        System.out.println(appId);
        System.out.println(appKey);
    }

}
