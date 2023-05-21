package cse.java2.project.controller;

import cse.java2.project.service.answer.AnswerService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for answer.
 */
@RestController
@RequestMapping("/answer")
@CrossOrigin(origins = "*")
@Api(tags = "AnswerController")
public class AnswerController {

    @Autowired
    AnswerService answerService;


}
