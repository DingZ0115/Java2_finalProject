package cse.java2.project.mapper;

import cse.java2.project.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface QuestionMapper {

    @Select("select count(*) from question;")
    int getQuestionCount();

    @Select("select count(*) from question where answer_count = 0;")
    int getNoAnswerQuestionCount();

    @Select("select max(answer_count) from question;")
    int getMaxAnswerOfQuestion();

    @Select("select sum(answer_count) from question;")
    int getSumAnswerOfQuestion();

    @Select("select count(*) from question where accepted_answer is true;")
    int getAcceptedQuestionCount();

    @Select("select count(*) from question where answer_count >= #{leftRange} and answer_count < #{rightRange}")
    int getRangeDistributionOfAnswer(int leftRange, int rightRange);


    @Select("select coalesce(max(score),0) from answer where question_id = #{question_id} and is_accepted is false;")
    int getMaxUnAcceptedScore(int question_id);

    @Select("select score from answer where question_id = #{question_id} and is_accepted is true;")
    int getAcceptedScore(int question_id);

    @Select("select question_id from question where accepted_answer is true;")
    List<Integer> getAcceptQuestionId();

    @Select("select accept_time from question where accepted_answer is true;")
    List<Integer> getAcceptedQuestionTimes();

    @Select("select post_time from question where accepted_answer is true;")
    List<Integer> getPostQuestionTimes();

}
