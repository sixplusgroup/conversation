package finley.gmair.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import finley.gmair.converter.KnowledgeConverter;
import finley.gmair.dao.knowledgebase.CommentMapper;
import finley.gmair.dao.knowledgebase.KnowledgeMapper;
import finley.gmair.dao.knowledgebase.TagRelationMapper;
import finley.gmair.dto.knowledgebase.KnowledgeDTO;
import finley.gmair.enums.knowledgeBase.CommentStatus;
import finley.gmair.enums.knowledgeBase.KnowledgeStatus;
import finley.gmair.model.knowledgebase.TagRelation;
import finley.gmair.service.KnowledgeService;
import finley.gmair.utils.RedisUtil;
import finley.gmair.vo.knowledgebase.KnowledgePagerVO;
import finley.gmair.vo.knowledgebase.KnowledgeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import finley.gmair.model.knowledgebase.Knowledge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class KnowledgeServiceImpl implements KnowledgeService {

    @Autowired
    KnowledgeMapper knowledgeMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    TagRelationMapper tagRelationMapper;

    @Autowired
    RedisUtil redisUtil;

    @Value("${python.env}")
    String pythonEnv;

    @Value("${python.script}")
    String pythonScrip;

    @Value("${python.stopdataSource}")
    String stopdata;



    @Override
    public void create(KnowledgeVO vo) {
        Knowledge knowledge = new Knowledge();
        //knowledge.setKnowledge_type(vo.getKnowledge_type());
        knowledge.setContent(vo.getContent());
        knowledge.setTitle(vo.getTitle());
        knowledgeMapper.insert(knowledge);
    }


    @Override
    public void delete(Integer id) {
        knowledgeMapper.delete(id);
    }

    @Override
    public void publish(Integer id) {
        knowledgeMapper.changeStatus(2, id);
    }

    @Override
    public void reedit(Integer id) {
        knowledgeMapper.changeStatus(KnowledgeStatus.PENDING_DDIT.getCode(), id);
    }

    @Override
    public KnowledgePagerVO getPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, "views desc");//todo test
        List<Knowledge> knowledges = knowledgeMapper.getAll();

        KnowledgePagerVO knowledgePagerVO = new KnowledgePagerVO();
        List<KnowledgeVO> knowledgeVOs = knowledges.stream().map(KnowledgeConverter::model2VO).collect(Collectors.toList());
        knowledgePagerVO.setKnowledgeVOS(knowledgeVOs);

        PageInfo<Knowledge> pageInfo = new PageInfo<>(knowledges);
        Long totalNum = pageInfo.getTotal();
        knowledgePagerVO.setTotalNum(totalNum);
        return knowledgePagerVO;
    }

    @Override
    public List<KnowledgeVO> getAll() {
        List<Knowledge> knowledges = knowledgeMapper.getAll();
        return knowledges.stream().map(KnowledgeConverter::model2VO).sorted((o1, o2) -> o2.getViews() - o1.getViews()).collect(Collectors.toList());
    }

    @Override
    public List<KnowledgeVO> getPublished() {
        List<Knowledge> knowledges = knowledgeMapper.getByState(2);
        return knowledges.stream().map(KnowledgeConverter::model2VO).sorted((o1, o2) -> o2.getViews() - o1.getViews()).collect(Collectors.toList());
    }

    @Override
    public KnowledgePagerVO getAuditPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Knowledge> knowledges = knowledgeMapper.getByState(0);

        KnowledgePagerVO knowledgePagerVO = new KnowledgePagerVO();
        List<KnowledgeVO> knowledgeVOs = knowledges.stream().map(KnowledgeConverter::model2VO).collect(Collectors.toList());
        knowledgePagerVO.setKnowledgeVOS(knowledgeVOs);

        PageInfo<Knowledge> pageInfo = new PageInfo<>(knowledges);
        Long totalNum = pageInfo.getTotal();
        knowledgePagerVO.setTotalNum(totalNum);
        return knowledgePagerVO;
    }

    @Override
    public List<KnowledgeVO> getAudit() {
        List<Knowledge> knowledges = knowledgeMapper.getByState(0);
        return knowledges.stream().map(KnowledgeConverter::model2VO).sorted((o1, o2) -> o2.getViews() - o1.getViews()).collect(Collectors.toList());
    }

    //    @Override
//    public KnowledgePagerVO getPageByType(Integer id, Integer pageNum, Integer pageSize) {
//        PageHelper.startPage(pageNum,pageSize);
//        List<Knowledge> knowledges = knowledgeMapper.getByType(id);
//
//        KnowledgePagerVO knowledgePagerVO = new KnowledgePagerVO();
//        knowledgePagerVO.setKnowledgeVOS(knowledges);
//
//        PageInfo<Knowledge> pageInfo = new PageInfo<>(knowledges);
//        Long totalNum = pageInfo.getTotal();
//        knowledgePagerVO.setTotalNum(totalNum);
//        return knowledgePagerVO;
//    }

    @Override
    public KnowledgeVO getById(Integer id) {
        Knowledge knowledge = knowledgeMapper.getById(id);
        knowledgeMapper.increaseViews(id);
        KnowledgeVO knowledgeVO = KnowledgeConverter.model2VO(knowledge);
        return knowledgeVO;
    }

    @Override
    public void modify(KnowledgeVO knowledgeVO) {
        Knowledge knowledge = knowledgeMapper.getById(knowledgeVO.getId());
        knowledge.setContent(knowledgeVO.getContent());
        knowledge.setTitle(knowledgeVO.getTitle());
        knowledge.setModifyTime(new Date());
        knowledgeMapper.modify(knowledge);
    }

    @Override
    public List<Knowledge> fulltextSearch(String key) {
        List<Knowledge> knowledges = knowledgeMapper.search(key);
        return knowledges;
    }

    @Override
    public KnowledgePagerVO fulltextListSearch(Integer pageSize, Integer pageNum, List<String> keys) {

        List<Knowledge> ret = new ArrayList<>();
        for (String key : keys) {
            List<Knowledge> knowledges = knowledgeMapper.search(key);
            ret.addAll(knowledges);
        }
        List<KnowledgeVO> f_ret = ret.stream().map(KnowledgeConverter::model2VO)
                .sorted((o1, o2) -> o2.getViews() - o1.getViews())
                .collect(Collectors.toList());

        KnowledgePagerVO knowledgePagerVO = new KnowledgePagerVO();
        knowledgePagerVO.setTotalNum(Long.valueOf(String.valueOf(f_ret.size())));
        knowledgePagerVO.setKnowledgeVOS(f_ret.subList((pageNum - 1) * pageSize, pageNum * pageSize));
        return knowledgePagerVO;
    }

    @Override
    public List<KnowledgeVO> searchByTagsKeys(List<Integer> tagIds, String keywords) {
        Set<Knowledge> knowledgesSet = new HashSet<>();//????????????
        //??????tagIds??????????????????tagId????????????tag_Relation????????????knowledgeID????????????????????????
        if (tagIds.size()!=0){
            List<Integer> knowledgeIds = tagRelationMapper.getByTagId(tagIds.get(0)).stream().map(TagRelation::getKnowledge_id).collect(Collectors.toList());
            if (tagIds.size() > 1) {
                for (int i = 1; i < tagIds.size(); i++) {//???tag_relation?????????tag_id????????????
                    //https://www.cnblogs.com/Andya/p/14037640.html
                    List<Integer> tmp_knowledgeIds = tagRelationMapper.getByTagId(tagIds.get(i)).stream().map(TagRelation::getKnowledge_id).collect(Collectors.toList());
                    knowledgeIds = knowledgeIds.stream().filter(tmp_knowledgeIds::contains).collect(Collectors.toList());
                }
            }
            if (knowledgeIds.size()>0) {
                List<Knowledge> knowledges = knowledgeMapper.getByIdList(knowledgeIds);
//                for(Knowledge k: knowledges) {
                knowledgesSet.addAll(knowledges);
            }

        }

        String[] keys = keywords.split(" ");
        for (String key : keys) {
            List<Knowledge> tmp_knowledges = knowledgeMapper.search(key);
            knowledgesSet.addAll(tmp_knowledges);
        }//??????????????????

        //??????????????????????????????
        return knowledgesSet.stream().sorted((o1, o2) -> (o2.getViews() - o1.getViews())).map(KnowledgeConverter::model2VO).collect(Collectors.toList());
    }

    @Override
    public void correct(KnowledgeDTO knowledgeDTO, int commentId) {
        commentMapper.updateStatus(commentId, CommentStatus.RESOLVED.getCode());
        Knowledge knowledge = KnowledgeConverter.DTO2model(knowledgeDTO);
        knowledgeMapper.modify(knowledge);
    }

    @Override
    public String getKeywords() {
        String keywords = "keywords";
        if (redisUtil.redisCheckHasKey(keywords)) {
            System.out.println("redis hit keywords");
            return redisUtil.redisGet(keywords);
        }



        System.out.println("redis miss keywords");
        String keyword = knowledgeMapper.getKeywords();
        redisUtil.redisSet(keywords, keyword, 30, TimeUnit.DAYS);
        return keyword;
    }

    //????????????0???10????????? ??????python?????????????????????
    @Scheduled(cron = "0 10 0 1 * ?")
    private void scheduleGetKeywords(){
        String[] commands = new String[]{pythonEnv,pythonScrip,stopdata};
        try {
            Process process = Runtime.getRuntime().exec(commands);
            runPython(process.getErrorStream());
            runPython(process.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runPython(InputStream is) throws IOException {
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }
}
