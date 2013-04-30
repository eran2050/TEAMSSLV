package net.voaideahost.sslv.mvc.gwt.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class StartPageController {
        @RequestMapping("/index")
        String index() {
                return "Application";
        }
}
