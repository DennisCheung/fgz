package com.wit.fgj.hall;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *  入口
 */
@Controller
@RequestMapping(value = "/hall/")
public class HallController {

//    private static final Logger LOGGER = LoggerFactory.getLogger(HallController.class);

//    @Autowired private CurrentUser currentUser;
//    @Autowired private QicClient client;
    //TODO 正式启动1.1工程后可能用到 @Autowired QicClient client;

    @GetMapping(value = "inWork")
    public ModelAndView openService(HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView("c/scan-main");
        return mav;
    }

}
