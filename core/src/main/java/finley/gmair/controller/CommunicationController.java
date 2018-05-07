package finley.gmair.controller;

import finley.gmair.netty.GMRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class CommunicationController {

    @Autowired
    @Qualifier("repository")
    private GMRepository repository;

    @GetMapping("/one")
    public String test() {
        return null;
    }
}
