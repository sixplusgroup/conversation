package finley.gmair.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class KnowledgeAuthController {
    @GetMapping("/manager")
    @PreAuthorize("hasAuthority('approveKnowledge')")
    public String manager(String name) {
        return "manager";
    }

    @GetMapping("/editor")
    @PreAuthorize("hasAuthority('updateKnowledge')")
    public String editor(String name) {
        return "editor";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('getKnowledge')")
    public String user(String name) {
        return "user";
    }
}
