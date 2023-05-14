package cse.java2.project.service.tag;

import cse.java2.project.common.Result;
import cse.java2.project.entity.Range;
import cse.java2.project.mapper.TagMapper;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {
    @Autowired
    TagMapper tagMapper;

    public Result getTop5ViewTags() {
        List<String> tags = tagMapper.getTop5ViewTags();
        List<String> nums = tagMapper.getTop5ViewTagsNum();
        return Result.ok().code(200).message("success").addData("list1", tags).addData("list2", nums);
    }

    public Result getTop5UpvoteTags() {
        List<String> tags = tagMapper.getTop5UpvoteTags();
        List<String> nums = tagMapper.getTop5UpvoteTagsNum();
        return Result.ok().code(200).message("success").addData("list1", tags).addData("list2", nums);
    }

    public Result getTop15AppearWithJavaTags() {
        List<String> tags = tagMapper.getTop10AppearTags();
        tags.remove(0);
        List<String> times = tagMapper.getTopAppearTime();
        times.remove(0);
        List<Range> items = new ArrayList<>();
        for (int i = 0; i < tags.size(); i++) {
            items.add(new Range(tags.get(i), times.get(i)));
        }
        return Result.ok().code(200).message("success").addData("list", items);
    }


    public Result getJavaAPI() {
        List<String> name = tagMapper.getJavaAPIName();
        List<String> num = tagMapper.getJavaAPINum();
        List<Range> items = new ArrayList<>();
        for (int i = 0; i < name.size(); i++) {
            items.add(new Range(name.get(i), num.get(i)));
        }
        return Result.ok().code(200).message("success").addData("list", items);
    }


}
