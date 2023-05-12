package cse.java2.project.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TagMapper {

    @Select("select tag_name from tag order by view_count desc limit 5;")
    List<String> getTop5ViewTags();

    @Select("select tag_name from tag order by upvote_count desc limit 5;")
    List<String> getTop5UpvoteTags();

    @Select("select tag_name from tag order by appear_num desc limit 11")
    List<String> getTop10AppearTags();

}
