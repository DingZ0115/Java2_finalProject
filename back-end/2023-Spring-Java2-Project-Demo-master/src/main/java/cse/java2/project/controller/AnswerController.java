package cse.java2.project.controller;

import cse.java2.project.common.Result;
import cse.java2.project.service.answer.AnswerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/answer")
@CrossOrigin(origins = "*")
@Api(tags = "AnswerController")
public class AnswerController {

    @Autowired
    AnswerService answerService;


}
