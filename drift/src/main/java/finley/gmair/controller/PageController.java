package finley.gmair.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName: PageController
 * @Description: TODO
 * @Author fan
 * @Date 2019/8/2 1:36 PM
 */
@RestController
@RequestMapping("/drift/page")
public class PageController {
    private Logger logger = LoggerFactory.getLogger(PageController.class);

    @GetMapping("/credit")
    public ModelAndView access(ModelAndView view) {
        view.setViewName("credit");
        return view;
    }
}
