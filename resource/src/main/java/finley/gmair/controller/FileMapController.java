package finley.gmair.controller;

import finley.gmair.service.FileMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resource/filemap")
public class FileMapController {

    @Autowired
    private FileMapService fileMapService;


}
