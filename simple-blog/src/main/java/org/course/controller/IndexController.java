package org.course.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping(value = {"/", "/post/view/*", "/post/edit/*", "/post/new", "/post/user", "/profile", "error"})
    public String getIndex(){
        return "index";
    }

}
