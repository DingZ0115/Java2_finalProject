package cse.java2.project.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Mapper for Tag.
 */
@Mapper
public interface TagMapper {

    @Select("select tag_name from tag order by view_count desc limit 5;")
    List<String> getTop5ViewTags();

    @Select("select tag_name from tag order by upvote_count desc limit 5;")
    List<String> getTop5UpvoteTags();

    @Select("select view_count from tag order by view_count desc limit 5;")
    List<String> getTop5ViewTagsNum();

    @Select("select upvote_count from tag order by upvote_count desc limit 5;")
    List<String> getTop5UpvoteTagsNum();

    @Select("select tag_name from tag order by appear_num desc limit 31")
    List<String> getTop10AppearTags();

    @Select("select appear_num from tag order by appear_num desc limit 31;")
    List<String> getTopAppearTime();

    @Select("select api_name from \"APIs\" order by appear_num desc limit 30;")
    List<String> getJavaAPIName();

    @Select("select appear_num from \"APIs\" order by appear_num desc limit 30;")
    List<String> getJavaAPINum();

    @Select("select tags_name from tags_comb order by upvote desc limit 5;")
    List<String> getTagsUpvoteComb();

    @Select("select tags_name from tags_comb order by view desc limit 5;")
    List<String> getTagsViewComb();

    @Select("select upvote from tags_comb order by upvote desc limit 5;")
    List<Integer> getTagsUpvoteCombNum();

    @Select("select view from tags_comb order by view desc limit 5;")
    List<Integer> getTagsViewCombNum();

}
