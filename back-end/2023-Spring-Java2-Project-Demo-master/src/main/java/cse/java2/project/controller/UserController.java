package cse.java2.project.controller;

import cse.java2.project.common.Result;
import cse.java2.project.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@Api(tags = "UserController")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/getUserDistributionOfPost")
    @ApiOperation(value = "User Distribution Of Posting Question")
    public Result getUserDistributionOfPost(){
        return userService.getUserDistributionOfPost();
    }

    @GetMapping("/getMostPostingUser")
    @ApiOperation(value = "User Who Post Most")
    public Result getMostPostingUser(){
        return userService.getMostPostingUser();
    }

    @GetMapping("/getUserDistributionOfAnswer")
    @ApiOperation(value = "User Distribution Of Answer")
    public Result getUserDistributionOfAnswer(){
        return userService.getUserDistributionOfAnswer();
    }

    @GetMapping("/getMostAnswerUser")
    @ApiOperation(value = "User Who Answer Most")
    public Result getMostAnswerUser(){
        return userService.getMostAnswerUser();
    }

    @GetMapping("/getUserDistributionOfComment")
    @ApiOperation(value = "User Distribution Of Comment")
    public Result getUserDistributionOfComment(){
        return userService.getUserDistributionOfComment();
    }

    @GetMapping("/getMostCommentUser")
    @ApiOperation(value = "User Who Comment Most")
    public Result getMostCommentUser(){
        return userService.getMostCommentUser();
    }


}
