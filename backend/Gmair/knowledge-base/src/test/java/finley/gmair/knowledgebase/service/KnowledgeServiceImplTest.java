package finley.gmair.knowledgebase.service;

import finley.gmair.dao.KnowledgeMapper;
import finley.gmair.model.knowledgebase.Knowledge;
import finley.gmair.vo.knowledgebase.KnowledgeVO;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
//@RunWith(JUnit4.class)
public class KnowledgeServiceImplTest{
    @Autowired
    KnowledgeMapper knowledgeMapper;

    private List<KnowledgeVO> vos=new ArrayList<>();

    @BeforeEach
    public void init() {

    }


    @Test
    public void create() {
        try {
            String encoding="UTF-8";
            File file=new File("/Users/xidao/Documents/SpringProjects/Gmair/knowledge-base/src/test/java/finley/gmair/knowledgebase/service/knowledge_data.txt");
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null) {
                    System.out.println(lineTxt);
                    String[] tmp = lineTxt.split(" & ");
                    KnowledgeVO vo = new KnowledgeVO();
                    vo.setTitle(tmp[0]);
                    vo.setContent(tmp[1]);
                    vos.add(vo);
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        System.out.println("after init");

        for(KnowledgeVO vo : vos) {
            Knowledge knowledge = new Knowledge();
            knowledge.setContent(vo.getContent());
            knowledge.setTitle(vo.getTitle());
            knowledgeMapper.insert(knowledge);
        }

    }

    public static void main(String[] args) {


//        String filePath = "/Users/xidao/Documents/SpringProjects/Gmair/knowledge-base/src/test/java/finley/gmair/knowledgebase/service/knowledge_data.txt";
//        FileInputStream fin = new FileInputStream(filePath);
//        InputStreamReader reader = new InputStreamReader(fin);
//        BufferedReader buffReader = new BufferedReader(reader);
//        String strTmp = "";
//        while((strTmp = buffReader.readLine())!=null){
//            System.out.println(strTmp);
//        }
//        buffReader.close();

    }
}
