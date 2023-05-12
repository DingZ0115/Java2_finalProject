package cse.java2.project.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Mapper
public interface AnswerMapper {

    @Select("select count(*) from question where accepted_answer is true;")
    int getAcceptedQuestionCount();

    @Select("select max(answer_count) from question;")
    int getMaxAnswerOfQuestion();

    @Select("select count(*) from question where answer_count >= #{leftRange} and answer_count < #{rightRange}")
    int getRangeDistributionOfAnswer(int leftRange, int rightRange);

    @Select("select count(*) from question;")
    int getQuestionCount();

}
