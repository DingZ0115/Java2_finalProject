package cse.java2.project.controller;

import cse.java2.project.common.Result;
import cse.java2.project.service.question.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for question.
 */
@RestController
@RequestMapping("/question")
@CrossOrigin(origins = "*")
@Api(tags = "QuestionController")
public class QuestionController {
    @Autowired
    QuestionService questionService;

    @GetMapping("/getNoAnswerRatio")
    @ApiOperation(value = "No Answer Question Ratio")
    public Result getNoAnswerRatio() {
        return questionService.getNoAnswerRatio();
    }

    @GetMapping("/getMaxAnswer")
    @ApiOperation(value = "No Answer Question Ratio")
    public Result getMaxAnswer() {
        return questionService.getMaxAnswer();
    }

    @GetMapping("/getAverageAnswer")
    @ApiOperation(value = "Average Answer of Questions")
    public Result getAverageAnswer() {
        return questionService.getAverageAnswer();
    }

    @GetMapping("/getAcceptedQuestionCount")
    @ApiOperation(value = "Question with Accepted Answer Count")
    public Result getAcceptedQuestionCount() {
        return questionService.getAcceptedQuestionCount();
    }

    @GetMapping("/getDistributionOfAnswers")
    @ApiOperation(value = "Distribution Of Answers")
    public Result getDistributionOfAnswers() {
        return questionService.getDistributionOfAnswers();
    }

    @GetMapping("/getBetterRatio")
    @ApiOperation(value = "Distribution Of Answers")
    public Result getBetterRatio() {
        return questionService.getBetterRatio();
    }

    @GetMapping("/getDistrutionOfQuestionDeltaTimes")
    @ApiOperation(value = "Distribution Of Question post and accept time")
    public Result getDistrutionOfQuestionDeltaTimes() {
        return questionService.getDistributionOfQuestionDeltaTimes();
    }


}
