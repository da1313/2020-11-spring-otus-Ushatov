package org.course.contollers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReactController {

    @GetMapping(value = {"/", "/login", "/books"})
    public String getIndex(){
        return "index";
    }

}
