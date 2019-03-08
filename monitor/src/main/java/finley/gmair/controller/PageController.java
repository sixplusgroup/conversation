package finley.gmair.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author fan
 * @create_time 2019-2019/2/26 9:47 PM
 */
@RestController
@RequestMapping("/machine")
public class PageController {

    @GetMapping("/tv/{qrcode}")
    public ModelAndView viewOnTV(@PathVariable("qrcode") String qrcode) {
        ModelAndView view = new ModelAndView();
        view.setViewName("tv_view");
        view.addObject("qrcode", qrcode);
        return view;
    }

//    @GetMapping("/tv/test")
//    public ModelAndView viewOnTest() {
//        ModelAndView view = new ModelAndView();
//        view.setViewName("tv_test");
//        return view;
//    }

    @GetMapping("/tv/multiple")
    public ModelAndView viewOnMultiple() {
        ModelAndView view = new ModelAndView();
        view.setViewName("multiple_view");
        return view;
    }
}
