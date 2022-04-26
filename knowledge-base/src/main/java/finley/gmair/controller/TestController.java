package finley.gmair.controller;


import finley.gmair.converter.CommentConverter;
import finley.gmair.dto.knowledgebase.CommentDTO;
import finley.gmair.enums.knowledgeBase.CommentType;
import finley.gmair.service.KnowledgeService;
import finley.gmair.util.ResultData;
import finley.gmair.vo.knowledgebase.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    KnowledgeService knowledgeService;

    @GetMapping("/get")
    public ResultData comment() {
        return ResultData.ok("hello");
    }

    @GetMapping("keyword")
    public ResultData keyword(){
        knowledgeService.scheduleGetKeywords();
        return null;
    }
}
