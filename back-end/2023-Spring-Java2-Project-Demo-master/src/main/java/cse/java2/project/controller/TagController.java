package cse.java2.project.controller;

import cse.java2.project.common.Result;
import cse.java2.project.service.tag.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tag")
@CrossOrigin(origins = "*")
@Api(tags = "TagController")
public class TagController {

    @Autowired
    TagService tagService;

    @GetMapping("/getTop5ViewTags")
    @ApiOperation(value = "Top 5 View Tags")
    public Result getTop5ViewTags() {
        return tagService.getTop5ViewTags();
    }

    @GetMapping("/getTop5UpvoteTags")
    @ApiOperation(value = "Top 5 Upvote Tags")
    public Result getTop5UpvoteTags() {
        return tagService.getTop5UpvoteTags();
    }
    @GetMapping("/getTop10AppearWithJavaTags")
    @ApiOperation(value = "Top 10 Appear Together with Java Tags")
    public Result getTop10AppearWithJavaTags(){
        return tagService.getTop10AppearWithJavaTags();
    }




}
