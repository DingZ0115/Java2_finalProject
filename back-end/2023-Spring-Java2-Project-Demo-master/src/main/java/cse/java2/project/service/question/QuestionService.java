package cse.java2.project.service.question;

import cse.java2.project.common.Result;
import cse.java2.project.entity.Range;
import cse.java2.project.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionMapper questionMapper;

    public Result getNoAnswerRatio() {
        int noAnswerQuestionCount = questionMapper.getNoAnswerQuestionCount();
        int questionCount = questionMapper.getQuestionCount();
        double ret = round((double) noAnswerQuestionCount / questionCount * 100, 2);
        return Result.ok().code(200).message("success").addData("ratio", ret + "%");
    }

    public Result getMaxAnswer() {
        int maxAnswerCount = questionMapper.getMaxAnswerOfQuestion();
        return Result.ok().code(200).message("success").addData("count", maxAnswerCount);
    }

    public Result getAverageAnswer() {
        int sumAnswer = questionMapper.getSumAnswerOfQuestion();
        int questionCount = questionMapper.getQuestionCount();
        double ret = round((double) sumAnswer / questionCount, 2);
        return Result.ok().code(200).message("success").addData("count", ret);
    }

    public Result getAcceptedQuestionCount() {
        int acceptQuestionCount = questionMapper.getAcceptedQuestionCount();
        int questionCount = questionMapper.getQuestionCount();
        double ret = round((double) acceptQuestionCount / questionCount * 100, 2);
        return Result.ok().code(200).message("success").addData("ratio", ret + "%");
    }


    public Result getDistributionOfAnswers() {
        int maxAnswerOfQuestion = questionMapper.getMaxAnswerOfQuestion();

        List<Range> listRange = new ArrayList<>();
        int r = (int) Math.round((double) maxAnswerOfQuestion / 8);
        int left = 0;
        int right = r;
        for (int i = 0; i < 7; i++) {
            String tName = left + " - " + right;
            String tValue = String.valueOf(questionMapper.getRangeDistributionOfAnswer(left, right));
            left += r;
            right += r;

            listRange.add(new Range(tName, tValue));
        }

        String tName = left + " - " + maxAnswerOfQuestion;
        String tValue = String.valueOf(questionMapper.getRangeDistributionOfAnswer(left, maxAnswerOfQuestion + 1));
        listRange.add(new Range(tName, tValue));
        return Result.ok().code(200).message("success").addData("distribution", listRange);
    }

    public Result getBetterRatio() {
        List<Integer> acceptQuestion = questionMapper.getAcceptQuestionId();
        int questionCount = questionMapper.getQuestionCount();

        int validCount = 0;
        for (Integer question_id : acceptQuestion) {

            int maxUnAcceptedScore = questionMapper.getMaxUnAcceptedScore(question_id);
            int acceptedScore = questionMapper.getAcceptedScore(question_id);
            if (maxUnAcceptedScore > acceptedScore) {
                validCount++;
            }
        }
        double ret = round((double) validCount / questionCount * 100, 2);
        return Result.ok().code(200).message("success").addData("ratio", ret + "%");

    }

    public Result getDistributionOfQuestionDeltaTimes() {
        List<Integer> post = questionMapper.getPostQuestionTimes();
        List<Integer> accept = questionMapper.getAcceptedQuestionTimes();

        int[][] array = new int[post.size()][2];

        for (int i = 0; i < post.size(); i++) {
            int delta = accept.get(i) - post.get(i);
            if (delta < 3000) {
                array[i][0] = delta / 60;
                array[i][1] = delta - 60 * (delta / 60);
            }
        }
        return Result.ok().code(200).message("success").addData("distribution", array);
    }


    private static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, RoundingMode.HALF_UP).doubleValue();
    }


}
