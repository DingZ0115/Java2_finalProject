package cse.java2.project.service.user;

import cse.java2.project.common.Result;
import cse.java2.project.entity.Range;
import cse.java2.project.entity.Users;
import cse.java2.project.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public Result getUserDistributionOfPost() {
        List<Range> listRange = new ArrayList<>();
        listRange.add(new Range("The number of user who post 0 question", String.valueOf(userMapper.getUserRangeDistributionOfPosting(0, 1))));
        listRange.add(new Range("The number of user who post 1 question", String.valueOf(userMapper.getUserRangeDistributionOfPosting(1, 2))));
        listRange.add(new Range("The number of user who post 2 questions", String.valueOf(userMapper.getUserRangeDistributionOfPosting(2, 3))));
        return Result.ok().code(200).message("success").addData("distribution", listRange);
    }

    public Result getUserDistributionOfAnswer() {
        int maxAnswer = userMapper.getMaxAnswerNum();
        List<Range> listRange = new ArrayList<>();
        int r = (int) Math.round((double) maxAnswer / 5);
        int left = 0;
        int right = r;
        for (int i = 0; i < 5; i++) {
            String tName = left + " - " + right;
            String tValue = String.valueOf(userMapper.getUserRangeDistributionOfAnswer(left, right));
            left += r;
            right += r;
            listRange.add(new Range(tName, tValue));
        }
        String tName = left + " - " + maxAnswer;
        String tValue = String.valueOf(userMapper.getUserRangeDistributionOfAnswer(left, maxAnswer + 1));
        listRange.add(new Range(tName, tValue));
        return Result.ok().code(200).message("success").addData("distribution", listRange);
    }

    public Result getUserDistributionOfComment() {
        int maxComment = userMapper.getMaxCommentNum();
        List<Range> listRange = new ArrayList<>();
        int r = (int) Math.round((double) maxComment / 5);
        int left = 0;
        int right = r;
        for (int i = 0; i < 4; i++) {
            String tName = left + " - " + right;
            String tValue = String.valueOf(userMapper.getUserRangeDistributionOfComment(left, right));
            left += r;
            right += r;
            listRange.add(new Range(tName, tValue));
        }
        String tName = left + " - " + maxComment;
        String tValue = String.valueOf(userMapper.getUserRangeDistributionOfComment(left, maxComment + 1));
        listRange.add(new Range(tName, tValue));
        return Result.ok().code(200).message("success").addData("distribution", listRange);
    }


    public Result getMostPostingUser() {
        int post = userMapper.getMaxPostNum();
        List<String> user_name = userMapper.getUsersByPosting(post);
        Users user = new Users(user_name.get(0), post);
        return Result.ok().code(200).message("success").addData("user", user);
    }

    public Result getMostAnswerUser() {
        int answer = userMapper.getMaxAnswerNum();
        List<String> user_name = userMapper.getUsersByAnswer(answer);
        Users user = new Users(user_name.get(0), answer);
        return Result.ok().code(200).message("success").addData("user", user);
    }

    public Result getMostCommentUser() {
        int comment = userMapper.getMaxCommentNum();
        List<String> user_name = userMapper.getUsersByComment(comment);
        Users user = new Users(user_name.get(0), comment);
        return Result.ok().code(200).message("success").addData("user", user);
    }

    public Result getMostActiveUser() {
        List<Integer> uid = userMapper.getMostActiveUser();
        return Result.ok().code(200).message("success").addData("user", uid);
    }

    public Result getUserDistributionOfCommunication() {
        int maxCommentNum = userMapper.getMaxCommNum(145);
        List<Range> listRange = new ArrayList<>();
        int r = (int) Math.round((double) maxCommentNum / 4);
        int left = 0;
        int right = r;
        for (int i = 0; i < 4; i++) {
            String tName = left + " - " + right;
            String tValue = String.valueOf(userMapper.getUserRangeDistributionOfComm(left, right));
            left += r;
            right += r;
            listRange.add(new Range(tName, tValue));
        }
        String tName = left + " - " + maxCommentNum;
        String tValue = String.valueOf(userMapper.getUserRangeDistributionOfComm(left, maxCommentNum + 1));
        listRange.add(new Range(tName, tValue));

        return Result.ok().code(200).message("success").addData("distribution", listRange);
    }

}
