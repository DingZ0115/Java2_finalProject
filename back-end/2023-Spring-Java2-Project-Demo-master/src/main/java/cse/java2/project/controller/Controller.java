package cse.java2.project.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/demo")
@Api(tags = "用户管理")
@org.springframework.stereotype.Controller
public class Controller {

    @GetMapping({"/", "/demo"})
    public String demo() {
        return "demo";
    }

}
