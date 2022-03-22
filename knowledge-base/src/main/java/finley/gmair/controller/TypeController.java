package finley.gmair.controller;

import finley.gmair.model.knowledgebase.Type;
import finley.gmair.service.TypeService;
import finley.gmair.util.ResultData;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/type")
@AllArgsConstructor
@Validated
public class TypeController {
    @Autowired
    TypeService typeService;

    @PostMapping("/getAllTypes")
    public ResultData getAllTypes() {
        List<Type> types = typeService.getAllTypes();
        return ResultData.ok(types, null);
    }
}
