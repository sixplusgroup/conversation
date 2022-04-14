package finley.gmair.converter;

import finley.gmair.dto.knowledgebase.KnowledgeDTO;
import finley.gmair.model.knowledgebase.Knowledge;
import finley.gmair.util.TimeUtil;
import finley.gmair.vo.knowledgebase.KnowledgeVO;

public class KnowledgeConverter {
    public static Knowledge DTO2model(KnowledgeDTO knowledgeDTO) {
        if (knowledgeDTO == null) {
            return null;
        }
        Knowledge knowledge = new Knowledge();
        knowledge.setId(knowledgeDTO.getId());
        knowledge.setStatus(knowledgeDTO.getStatus());
        knowledge.setViews(knowledgeDTO.getViews());
        knowledge.setTitle(knowledgeDTO.getTitle());
        knowledge.setContent(knowledgeDTO.getContent());
        return knowledge;
    }

    public static KnowledgeDTO model2DTO(Knowledge knowledge) {
        if (knowledge == null) {
            return null;
        }
        KnowledgeDTO knowledgeDTO = new KnowledgeDTO();
        knowledgeDTO.setId(knowledge.getId());
        knowledgeDTO.setStatus(knowledge.getStatus());
        knowledgeDTO.setViews(knowledge.getViews());
        knowledgeDTO.setTitle(knowledge.getTitle());
        knowledgeDTO.setContent(knowledge.getContent());
        return knowledgeDTO;
    }

    public static KnowledgeVO DTO2VO(KnowledgeDTO knowledgeDTO) {
        if (knowledgeDTO == null) {
            return null;
        }
        KnowledgeVO knowledgeVO = new KnowledgeVO();
        knowledgeVO.setId(knowledgeDTO.getId());
        knowledgeVO.setStatus(knowledgeDTO.getStatus());
        knowledgeVO.setViews(knowledgeDTO.getViews());
        knowledgeVO.setTitle(knowledgeDTO.getTitle());
        knowledgeVO.setContent(knowledgeDTO.getContent());
        return knowledgeVO;
    }

    public static KnowledgeDTO VO2DTO(KnowledgeVO knowledgeVO) {
        if (knowledgeVO == null) {
            return null;
        }
        KnowledgeDTO knowledgeDTO = new KnowledgeDTO();
        knowledgeDTO.setId(knowledgeVO.getId());
        knowledgeDTO.setTitle(knowledgeVO.getTitle());
        knowledgeDTO.setContent(knowledgeVO.getContent());
        knowledgeDTO.setStatus(knowledgeVO.getStatus());
        knowledgeDTO.setViews(knowledgeVO.getViews());
        return knowledgeDTO;
    }

    public static KnowledgeVO model2VO(Knowledge knowledge){
        if (knowledge == null) {
            return null;
        }
        KnowledgeVO knowledgeVO = new KnowledgeVO();
        knowledgeVO.setContent(knowledge.getContent());
        knowledgeVO.setTitle(knowledge.getTitle());
        knowledgeVO.setCreateTime(TimeUtil.datetimeToString(knowledge.getCreateTime()));
        knowledgeVO.setModifyTime(TimeUtil.datetimeToString(knowledge.getModifyTime()));
        knowledgeVO.setStatus(knowledge.getStatus());
        knowledgeVO.setViews(knowledge.getViews());
        knowledgeVO.setId(knowledge.getId());
        return knowledgeVO;
    }

}
