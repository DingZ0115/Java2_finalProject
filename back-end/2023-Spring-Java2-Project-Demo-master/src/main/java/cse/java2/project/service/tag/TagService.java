package cse.java2.project.service.tag;

import cse.java2.project.common.Result;
import cse.java2.project.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TagService {
    @Autowired
    TagMapper tagMapper;

    public Result getTop5ViewTags() {
        List<String> tags = tagMapper.getTop5ViewTags();
        return Result.ok().code(200).message("success").addData("list", tags);
    }

    public Result getTop5UpvoteTags() {
        List<String> tags = tagMapper.getTop5UpvoteTags();
        return Result.ok().code(200).message("success").addData("list", tags);
    }

    public Result getTop10AppearWithJavaTags() {
        List<String> tags = tagMapper.getTop10AppearTags();
        tags.remove(0);
        return Result.ok().code(200).message("success").addData("list", tags);
    }


}
