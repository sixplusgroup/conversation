package finley.gmair.converter;

import finley.gmair.dto.knowledgebase.CommentPagerDTO;
import finley.gmair.vo.knowledgebase.CommentPagerVO;

import java.util.stream.Collectors;

public class CommentPagerConverter {
    public static CommentPagerDTO VO2DTO(CommentPagerVO commentPagerVO) {
        if (commentPagerVO == null) {
            return null;
        }
        CommentPagerDTO commentPagerDTO = new CommentPagerDTO();
        commentPagerDTO.setTotalNum(commentPagerVO.getTotalNum());
        commentPagerDTO.setCommentDTOS(
                commentPagerVO.getCommentVOList().stream()
                .map(CommentConverter::VO2DTO)
                .collect(Collectors.toList())
        );
        return commentPagerDTO;
    }

    public static CommentPagerVO DTO2VO(CommentPagerDTO commentPagerDTO) {
        if (commentPagerDTO == null) {
            return null;
        }
        CommentPagerVO commentPagerVO = new CommentPagerVO();
        commentPagerVO.setTotalNum(commentPagerDTO.getTotalNum());
        commentPagerVO.setCommentVOList(
                commentPagerDTO.getCommentDTOS().stream()
                .map(CommentConverter::DTO2VO)
                .collect(Collectors.toList()));
        return commentPagerVO;
    }

}
