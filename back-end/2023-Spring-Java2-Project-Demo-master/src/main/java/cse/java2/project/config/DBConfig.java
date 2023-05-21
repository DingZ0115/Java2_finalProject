package cse.java2.project.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.swagger.models.auth.In;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;

@Configuration
public class DBConfig {
    private static final String url = "jdbc:postgresql://localhost:5432/postgres";

    private static final String user = "postgres";

    private static final String pass = "472362";

    private static Connection con;

    public DBConfig() {
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, user, pass);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getCon() {
        return con;
    }

    public void updateQuestionAndTagFromWeb() throws URISyntaxException, IOException, SQLException {
        int pagesize = 100, page = 5;
        JsonParser parser = new JsonParser();
        URIBuilder builder = new URIBuilder("https://api.stackexchange.com/2.3/questions").addParameter("tagged", "java").addParameter("sort", "activity").addParameter("site", "stackoverflow").addParameter("pagesize", String.valueOf(pagesize)).addParameter("order", "desc").addParameter("access_token", "QVf1Fr4u9Gbm1WOEjvNu*Q))").addParameter("key", "4te10xQDOUjDKbmiqVOkJg((");

        PreparedStatement statement = con.prepareStatement("insert into question ( question_id, answer_count, accepted_answer, view_count, score, post_time) values (?, ?, ?, ?, ?, ?) on conflict (question_id) do nothing;");

        Map<String, Integer> tagsView = new HashMap<>();
        Map<String, Integer> tagsUpvote = new HashMap<>();
        Map<String, Integer> tagsAppear = new HashMap<>();

        for (int i = 1; i <= page; i++) {
            builder.setParameter("page", String.valueOf(i));
            String js = getJSON(builder.build(), "question");

            if (js != null) {
                JsonElement jsonElement = parser.parse(new StringReader(js));
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonElement itemsElement = jsonObject.get("items");
                JsonArray itemsArray = itemsElement.getAsJsonArray();
                for (JsonElement itemElement : itemsArray) {
                    JsonObject itemObject = itemElement.getAsJsonObject();

                    int question_id = itemObject.get("question_id").getAsInt();
                    int answer_count = itemObject.get("answer_count").getAsInt();
                    boolean is_answered = itemObject.has("accepted_answer_id");
                    int view_count = itemObject.get("view_count").getAsInt();
                    int score = itemObject.get("score").getAsInt();
                    int date = itemObject.get("creation_date").getAsInt();

                    statement.setInt(1, question_id);
                    statement.setInt(2, answer_count);
                    statement.setBoolean(3, is_answered);
                    statement.setInt(4, view_count);
                    statement.setInt(5, score);
                    statement.setInt(6, date);
                    statement.addBatch();

                    JsonArray tagsArray = itemObject.get("tags").getAsJsonArray();
                    for (JsonElement tag_name : tagsArray) {
                        String name = tag_name.getAsString();

                        tagsAppear.putIfAbsent(name, 0);
                        tagsAppear.computeIfPresent(name, (k, v) -> v + 1);

                        tagsView.putIfAbsent(name, 0);
                        tagsView.computeIfPresent(name, (k, v) -> v + view_count);

                        tagsUpvote.putIfAbsent(name, 0);
                        tagsUpvote.computeIfPresent(name, (k, v) -> v + score);
                    }
                }
                statement.executeBatch();
                statement.clearBatch();
            }
        }
        statement.close();

        System.out.println(tagsUpvote);
        System.out.println(tagsView);

        PreparedStatement updateTags = con.prepareStatement("insert into tag (tag_name, view_count, upvote_count,appear_num) values (?, ?, ?, ?) on conflict(tag_name) do update set (view_count, upvote_count, appear_num) = (?, ?, ?);");
        tagsView.forEach((name, view) -> {
            int upvote = tagsUpvote.get(name);
            int appear = tagsAppear.get(name);
            try {
                updateTags.setString(1, name);
                updateTags.setInt(2, view);
                updateTags.setInt(3, upvote);
                updateTags.setInt(4, appear);
                updateTags.setInt(5, view);
                updateTags.setInt(6, upvote);
                updateTags.setInt(7, appear);
                updateTags.addBatch();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        updateTags.executeBatch();
        updateTags.close();
    }

    public void updateAnswerFromDB() throws SQLException, URISyntaxException, IOException {

        PreparedStatement getPrepQuestionId = con.prepareStatement("select question_id from question where answer_count > 0;");
        PreparedStatement insertAnswer = con.prepareStatement("insert into answer (answer_id, is_accepted, score, question_id) values (?, ?, ?,?) on conflict(answer_id) do nothing;");
        PreparedStatement updateAcceptedTime = con.prepareStatement("update question set accept_time = ? where question_id = ?;");

        ResultSet prepQuestionId = getPrepQuestionId.executeQuery();
        JsonParser parser = new JsonParser();
        while (prepQuestionId.next()) {
            int question_id = prepQuestionId.getInt(1);
            URIBuilder builder = new URIBuilder("https://api.stackexchange.com/2.3/questions/" + question_id + "/answers").addParameter("sort", "activity").addParameter("site", "stackoverflow").addParameter("order", "desc").addParameter("access_token", "tDLfzlXAnWJXUn4pMFnc8w))").addParameter("key", "4te10xQDOUjDKbmiqVOkJg((");
            //System.out.println(builder.toString());
            String js = getJSON(builder.build(), "question");
            if (js != null) {
                JsonElement jsonElement = parser.parse(new StringReader(js));
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonElement itemsElement = jsonObject.get("items");
                JsonArray itemsArray = itemsElement.getAsJsonArray();
                for (JsonElement itemElement : itemsArray) {
                    JsonObject itemObject = itemElement.getAsJsonObject();

                    int answer_id = itemObject.get("answer_id").getAsInt();
                    int score = itemObject.get("score").getAsInt();
                    boolean is_accepted = itemObject.get("is_accepted").getAsBoolean();
                    int date = itemObject.get("creation_date").getAsInt();

                    //System.out.println(question_id + " " + answer_id + " " + score + " " + is_accepted + " " + date);

                    insertAnswer.setInt(1, answer_id);
                    insertAnswer.setBoolean(2, is_accepted);
                    insertAnswer.setInt(3, score);
                    insertAnswer.setInt(4, question_id);
                    insertAnswer.addBatch();

//                        System.out.println("success");

                    if (is_accepted) {
                        updateAcceptedTime.setInt(1, date);
                        updateAcceptedTime.setInt(2, question_id);
                        updateAcceptedTime.addBatch();
                        updateAcceptedTime.executeUpdate();
                        updateAcceptedTime.clearBatch();
                        ;
                    }

                }
                insertAnswer.executeBatch();
                insertAnswer.clearBatch();
            }

        }
    }


    public void updateJavaAPIFromWeb() throws IOException, SQLException {
        URI pointUri = URI.create("https://api.stackexchange.com/2.3/tags?pagesize=30&order=desc&sort=popular&inname=java.&site=stackoverflow");
        URI xUri = URI.create("https://api.stackexchange.com/2.3/tags?pagesize=30&order=desc&sort=popular&inname=javax.&site=stackoverflow");
        URI scoreUri = URI.create("https://api.stackexchange.com/2.3/tags?pagesize=30&order=desc&sort=popular&inname=java-&site=stackoverflow");

        PreparedStatement statement = con.prepareStatement("insert into \"APIs\" (api_name,appear_num) values (?,? ) on conflict (api_name) do nothing;");
        String jsPoint = getJSON(pointUri, "API");
        String jsX = getJSON(xUri, "API");
        String jsScore = getJSON(scoreUri, "API");
        JsonParser parser = new JsonParser();
        JsonArray pointReturn = parser.parse(new StringReader(jsPoint)).getAsJsonObject().get("items").getAsJsonArray();
        JsonArray xReturn = parser.parse(new StringReader(jsX)).getAsJsonObject().get("items").getAsJsonArray();
        JsonArray scoreReturn = parser.parse(new StringReader(jsScore)).getAsJsonObject().get("items").getAsJsonArray();

        for (JsonElement jsElement : pointReturn) {
            JsonObject questionObject = jsElement.getAsJsonObject();
            statement.setString(1, questionObject.get("name").getAsString());
            statement.setInt(2, questionObject.get("count").getAsInt());
            statement.addBatch();
        }
        statement.executeBatch();
        statement.clearBatch();

        for (JsonElement jsElement : xReturn) {
            JsonObject questionObject = jsElement.getAsJsonObject();
            statement.setString(1, questionObject.get("name").getAsString());
            statement.setInt(2, questionObject.get("count").getAsInt());
            statement.addBatch();
        }
        statement.executeBatch();
        statement.clearBatch();

        String pattern = "^java-\\d+$";

        for (JsonElement jsElement : scoreReturn) {
            JsonObject questionObject = jsElement.getAsJsonObject();
            String name = questionObject.get("name").getAsString();
            if (!name.matches(pattern)) {
                statement.setString(1, name);
                statement.setInt(2, questionObject.get("count").getAsInt());
                statement.addBatch();
            }
        }
        statement.executeBatch();
        statement.clearBatch();
    }

    public void updateUserFromDB() throws SQLException, IOException {
        PreparedStatement getQuestionId = con.prepareStatement("select question_id from question limit 100;");
        ResultSet questionId = getQuestionId.executeQuery();
        JsonParser parser = new JsonParser();

        Map<Integer, Integer> postMap = new HashMap<>();
        Map<Integer, Integer> answerMap = new HashMap<>();
        Map<Integer, Integer> commentMap = new HashMap<>();
        Map<Integer, Set<Integer>> questionComm = new HashMap<>();

        Set<Integer> uid = new HashSet<>();

        while (questionId.next()) {
            int question_id = questionId.getInt(1);
            URI questionURI = URI.create("https://api.stackexchange.com/2.3/questions/" + question_id + "?order=desc&sort=activity&site=stackoverflow");
            URI answerURI = URI.create("https://api.stackexchange.com/2.3/questions/" + question_id + "/answers?order=desc&sort=activity&site=stackoverflow");

            questionComm.computeIfAbsent(question_id, k -> new HashSet<>());
            Set<Integer> commSet = questionComm.get(question_id);
            String jsQuestion = getJSON(questionURI, "question");
            String jsAnswer = getJSON(answerURI, "answer");

            if (jsQuestion != null && jsAnswer != null) {
                JsonArray questionReturn = parser.parse(new StringReader(jsQuestion)).getAsJsonObject().get("items").getAsJsonArray();

                JsonArray answerReturn = parser.parse(new StringReader(jsAnswer)).getAsJsonObject().get("items").getAsJsonArray();

                for (JsonElement questionElement : questionReturn) {
                    JsonObject questionObject = questionElement.getAsJsonObject();
                    if (questionObject.get("owner").getAsJsonObject().get("account_id") != null &&
                            questionObject.get("owner").getAsJsonObject().get("account_id").getAsString() != null &&
                            questionObject.get("owner").getAsJsonObject().get("account_id").getAsInt() != -1) {
                        int user_question_id = questionObject.get("owner").getAsJsonObject().get("account_id").getAsInt();

                        //System.out.println("question " + user_question_id);

                        commSet.add(user_question_id);
                        uid.add(user_question_id);
                        postMap.putIfAbsent(user_question_id, 0);
                        postMap.computeIfPresent(user_question_id, (k, v) -> v + 1);
                        if (uid.size() >= 500) break;

                        for (JsonElement answerElement : answerReturn) {
                            JsonObject answerObject = answerElement.getAsJsonObject();
                            if (questionObject.get("owner").getAsJsonObject().get("account_id") != null &&
                                    questionObject.get("owner").getAsJsonObject().get("account_id").getAsString() != null &&
                                    questionObject.get("owner").getAsJsonObject().get("account_id").getAsInt() != -1) {
                                int user_answer_id = questionObject.get("owner").getAsJsonObject().get("account_id").getAsInt();
                                int answer_id = answerObject.get("answer_id").getAsInt();


                                //System.out.println("answer " + user_answer_id);

                                commSet.add(user_answer_id);
                                uid.add(user_answer_id);
                                answerMap.putIfAbsent(user_answer_id, 0);
                                answerMap.computeIfPresent(user_answer_id, (k, v) -> v + 1);
                                if (uid.size() >= 500) break;

                                URI commentURI = URI.create("https://api.stackexchange.com/2.3/answers/" + answer_id + "/comments?order=desc&sort=creation&site=stackoverflow");

                                String jsComment = getJSON(commentURI, "comment");
                                if (jsComment != null) {

                                    JsonElement jsonElement = parser.parse(new StringReader(jsComment));
                                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                                    JsonElement itemsElement = jsonObject.get("items");
                                    JsonArray itemsArray = itemsElement.getAsJsonArray();
                                    for (JsonElement itemElement : itemsArray) {
                                        JsonObject itemObject = itemElement.getAsJsonObject();
                                        if (itemObject.get("owner").getAsJsonObject().get("account_id") != null &&
                                                itemObject.get("owner").getAsJsonObject().get("account_id").getAsString() != null &&
                                                itemObject.get("owner").getAsJsonObject().get("account_id").getAsInt() != -1) {
                                            int user_comment_id = itemObject.get("owner").getAsJsonObject().get("account_id").getAsInt();
                                            //System.out.println("comment " + user_comment_id);

                                            commSet.add(user_comment_id);
                                            uid.add(user_comment_id);
                                            commentMap.putIfAbsent(user_comment_id, 0);
                                            commentMap.computeIfPresent(user_comment_id, (k, v) -> v + 1);
                                            if (uid.size() >= 500) break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        FileWriter fw = new FileWriter("test.txt");

        PreparedStatement updateQuestion = con.prepareStatement("update question set comm_num = ? where question_id = ?;");

        questionComm.forEach((k, v) -> {
            try {
                updateQuestion.setInt(1, v.size());
                updateQuestion.setInt(2, k);
                updateQuestion.addBatch();
                fw.write(updateQuestion + ";\r\n");
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        fw.close();

        updateQuestion.executeUpdate();
        updateQuestion.close();


        PreparedStatement insertUser = con.prepareStatement("insert into users (account_id,  post_num, answer_num, comment_num) values (?,?,?,?) on conflict(account_id) do update set (post_num,answer_num,comment_num) = (?,?,?);");
        AtomicInteger cnt = new AtomicInteger();
        uid.forEach(id -> {
            int postCnt = postMap.get(id) == null ? 0 : postMap.get(id);
            int answerCnt = answerMap.get(id) == null ? 0 : answerMap.get(id);
            int commentCnt = commentMap.get(id) == null ? 0 : commentMap.get(id);
            try {
                insertUser.setInt(1, id);
                insertUser.setInt(2, postCnt);
                insertUser.setInt(3, answerCnt);
                insertUser.setInt(4, commentCnt);
                insertUser.setInt(5, postCnt);
                insertUser.setInt(6, answerCnt);
                insertUser.setInt(7, commentCnt);
                insertUser.addBatch();
                if (cnt.compareAndSet(500, 0)) {
                    insertUser.executeBatch();
                    insertUser.clearBatch();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            cnt.getAndIncrement();
        });

        insertUser.executeBatch();
        insertUser.close();

    }

    public void updateTags() {
        try {
            PreparedStatement insert = con.prepareStatement("insert into tags_comb(tags_name, upvote, view) values (?,?,?) on conflict(tags_name) do nothing;");
            JsonParser parser = new JsonParser();

            URIBuilder uriBuilder = new URIBuilder("https://api.stackexchange.com/2.3/questions?&order=desc&sort=activity&tagged=java&site=stackoverflow&filter=!)4qcVpY9Tvb_cdttjBnShttVWo.l");
            for (int i = 1; i <= 10; i++) {
                uriBuilder.setParameter("page", String.valueOf(i));
                uriBuilder.setParameter("pagesize", "50");

                URI uri = uriBuilder.build();
                System.out.println(uri);
                String js = getJSON(uri, "tags");
                if (js != null) {
                    JsonElement jsonElement = parser.parse(new StringReader(js));
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    JsonElement itemsElement = jsonObject.get("items");
                    JsonArray itemsArray = itemsElement.getAsJsonArray();

                    for (JsonElement itemElement : itemsArray) {
                        JsonObject itemObject = itemElement.getAsJsonObject();

                        if (itemObject.has("answers")) {
                            JsonArray tagsArray = itemObject.get("tags").getAsJsonArray();
                            List<String> tags = new ArrayList<>();
                            for (JsonElement tagsElement :
                                    tagsArray) {
                                tags.add(tagsElement.getAsString());
                            }

                            JsonArray answers = itemObject.get("answers").getAsJsonArray();
                            int upvote = 0;
                            for (JsonElement answerElement :
                                    answers) {
                                upvote += answerElement.getAsJsonObject().get("up_vote_count").getAsInt();
                            }

                            int view = itemObject.get("view_count").getAsInt();

                            System.out.println(tags.toString()+" " + upvote+" " + view);

                            insert.setString(1, tags.toString());
                            insert.setInt(2, upvote);
                            insert.setInt(3, view);

                            insert.addBatch();
                        }

                        insert.executeBatch();
                        insert.clearBatch();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getUserName(int uid) throws IOException {
        URI uri = URI.create("https://api.stackexchange.com/2.3/users/" + uid + "?order=desc&sort=reputation&site=stackoverflow");
        String js = getJSON(uri, "username");
        // display_name
        JsonParser parser = new JsonParser();
        String name = "";
        if (js != null) {
            JsonArray questionReturn = parser.parse(new StringReader(js)).getAsJsonObject().get("items").getAsJsonArray();
            for (JsonElement questionElement : questionReturn) {
                JsonObject questionObject = questionElement.getAsJsonObject();
                name = questionObject.get("display_name").getAsString();
            }
        }
        return name;
    }

    private String getJSON(URI uri, String type) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(conn.getInputStream()), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            return sb.toString();
        } else {
            System.out.println("Failed to retrieve data from " + uri);
            return null;
        }
    }


    public void getAccessToken() throws URISyntaxException {
        URIBuilder builder = new URIBuilder("https://stackoverflow.com/oauth/dialog").addParameter("client_id", "26145").addParameter("scope", "no_expiry").addParameter("redirect_uri", "https://stackexchange.com/oauth/login_success");
        System.out.println(builder);
    }


}
