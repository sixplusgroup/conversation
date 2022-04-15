package finley.gmair.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import finley.gmair.converter.CommentConverter;
import finley.gmair.converter.KnowledgeConverter;
import finley.gmair.dao.CommentMapper;
import finley.gmair.dao.KnowledgeMapper;
import finley.gmair.dto.knowledgebase.CommentDTO;
import finley.gmair.dto.knowledgebase.KnowledgeDTO;
import finley.gmair.enums.knowledgeBase.CommentStatus;
import finley.gmair.service.KnowledgeService;
import finley.gmair.util.ResultData;
import finley.gmair.utils.PageParam;
import finley.gmair.vo.knowledgebase.KnowledgePagerVO;
import finley.gmair.vo.knowledgebase.KnowledgeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import finley.gmair.model.knowledgebase.Knowledge;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KnowledgeServiceImpl implements KnowledgeService {

    @Autowired
    KnowledgeMapper knowledgeMapper;

    @Autowired
    CommentMapper commentMapper;

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
        knowledgeMapper.changeStatus(2,id);
    }

    @Override
    public void reedit(Integer id, CommentDTO commentDTO) {
        knowledgeMapper.changeStatus(1, id);
        commentMapper.insert(CommentConverter.DTO2model(commentDTO));
    }

    @Override
    public KnowledgePagerVO getPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize,"views desc");//todo test
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
    public KnowledgePagerVO getAuditPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Knowledge> knowledges = knowledgeMapper.getByState(0);

        KnowledgePagerVO knowledgePagerVO = new KnowledgePagerVO();
        List<KnowledgeVO> knowledgeVOs = knowledges.stream().map(KnowledgeConverter::model2VO).collect(Collectors.toList());
        knowledgePagerVO.setKnowledgeVOS(knowledgeVOs);

        PageInfo<Knowledge> pageInfo = new PageInfo<>(knowledges);
        Long totalNum = pageInfo.getTotal();
        knowledgePagerVO.setTotalNum(totalNum);
        return knowledgePagerVO;
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
    public List<KnowledgeVO> fulltextListSearch(List<String> keys) {

        List<Knowledge> ret = new ArrayList<>();
        for(String key: keys){
            List<Knowledge> knowledges = knowledgeMapper.search(key);
            ret.addAll(knowledges);
        }
        List<KnowledgeVO> f_ret = ret.stream().map(KnowledgeConverter::model2VO).sorted(new Comparator<KnowledgeVO>() {
            @Override
            public int compare(KnowledgeVO o1, KnowledgeVO o2) {
                return o2.getViews()-o1.getViews();
            }
        }).collect(Collectors.toList());
        return f_ret;
    }

    @Override
    public void correct(KnowledgeDTO knowledgeDTO,int commentId) {
        commentMapper.updateStatus(commentId, CommentStatus.RESOLVED.getCode());
        Knowledge knowledge = KnowledgeConverter.DTO2model(knowledgeDTO);
        knowledgeMapper.modify(knowledge);
    }
}
