package cse.java2.project.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select max(post_num) from users;")
    int getMaxPostNum();

    @Select("select max(answer_num) from users;")
    int getMaxAnswerNum();

    @Select("select max(comment_num) from users;")
    int getMaxCommentNum();

    @Select("select count(*) from users where post_num >= #{leftRange} and post_num < #{rightRange};")
    int getUserRangeDistributionOfPosting(int leftRange, int rightRange);

    @Select("select count(*) from users where answer_num >= #{leftRange} and answer_num < #{rightRange};")
    int getUserRangeDistributionOfAnswer(int leftRange, int rightRange);

    @Select("select count(*) from users where comment_num >= #{leftRange} and comment_num < #{rightRange};")
    int getUserRangeDistributionOfComment(int leftRange, int rightRange);

    @Select("select user_name from users where post_num = #{cnt}")
    List<String> getUsersByPosting(int cnt);

    @Select("select user_name from users where answer_num = #{cnt}")
    List<String> getUsersByAnswer(int cnt);

    @Select("select user_name from users where comment_num = #{cnt}")
    List<String> getUsersByComment(int cnt);

    @Select("select account_id from (SELECT account_id, SUM(post_num * 0.2 + answer_num * 0.5 + comment_num * 0.3) as ff FROM users GROUP BY account_id order by ff desc ) as b limit 3;")
    List<Integer> getMostActiveUser();

    @Select("select max(comm_num) from question;")
    int getMaxCommNum(int cnt);

    @Select("select count(*) from question where comm_num >= #{leftRange} and comm_num < #{rightRange};")
    int getUserRangeDistributionOfComm(int leftRange, int rightRange);
}
