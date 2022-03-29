package finley.gmair.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import finley.gmair.converter.CommentConverter;
import finley.gmair.dao.CommentMapper;
import finley.gmair.dao.KnowledgeMapper;
import finley.gmair.dto.knowledgebase.CommentDTO;
import finley.gmair.service.KnowledgeService;
import finley.gmair.util.ResultData;
import finley.gmair.utils.PageParam;
import finley.gmair.vo.knowledgebase.KnowledgePagerVO;
import finley.gmair.vo.knowledgebase.KnowledgeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import finley.gmair.model.knowledgebase.Knowledge;

import java.util.List;

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
        PageHelper.startPage(pageNum,pageSize);
        List<Knowledge> knowledges = knowledgeMapper.getAll();

        KnowledgePagerVO knowledgePagerVO = new KnowledgePagerVO();
        knowledgePagerVO.setKnowledgeVOS(knowledges);

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
        knowledgePagerVO.setKnowledgeVOS(knowledges);

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
    public Knowledge getById(Integer id) {
        Knowledge knowledge = knowledgeMapper.getById(id);
        knowledgeMapper.increaseViews(id);
        return knowledge;
    }

    @Override
    public void modify(KnowledgeVO knowledgeVO) {
        Knowledge knowledge = knowledgeMapper.getById(knowledgeVO.getId());
        knowledge.setContent(knowledgeVO.getContent());
        knowledge.setTitle(knowledgeVO.getTitle());
        //knowledge.setKnowledge_type(knowledgeVO.getKnowledge_type());
        knowledgeMapper.modify(knowledge);
    }

    @Override
    public List<Knowledge> fulltextSearch(String key) {
        List<Knowledge> knowledges = knowledgeMapper.search(key);
        return knowledges;
    }

    @Override
    public void correct(Integer id, String comment) {
        Knowledge knowledge = knowledgeMapper.getById(id);
        knowledge.setStatus(1);
        knowledgeMapper.modify(knowledge);
    }
}
