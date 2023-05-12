package cse.java2.project.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
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
import java.util.HashMap;
import java.util.HashSet;
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
        int pagesize = 100, page = 8;
        JsonParser parser = new JsonParser();
        URIBuilder builder = new URIBuilder("https://api.stackexchange.com/2.3/questions").addParameter("tagged", "java").addParameter("sort", "activity").addParameter("site", "stackoverflow").addParameter("pagesize", String.valueOf(pagesize)).addParameter("order", "desc").addParameter("access_token", "QVf1Fr4u9Gbm1WOEjvNu*Q))").addParameter("key", "4te10xQDOUjDKbmiqVOkJg((");

        PreparedStatement statement = con.prepareStatement("insert into question ( question_id, answer_count, accepted_answer, view_count, score) values (?, ?, ?, ?, ?) on conflict (question_id) do nothing;");

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

                    statement.setInt(1, question_id);
                    statement.setInt(2, answer_count);
                    statement.setBoolean(3, is_answered);
                    statement.setInt(4, view_count);
                    statement.setInt(5, score);
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
        PreparedStatement insertAnswer = con.prepareStatement("insert into answer (answer_id, is_accepted, score,question_id) values (?, ?, ?,?) on conflict(answer_id) do nothing;");

        ResultSet prepQuestionId = getPrepQuestionId.executeQuery();
        JsonParser parser = new JsonParser();
        while (prepQuestionId.next()) {
            int question_id = prepQuestionId.getInt(1);
            URIBuilder builder = new URIBuilder("https://api.stackexchange.com/2.3/questions/" + question_id + "/answers").addParameter("sort", "activity").addParameter("site", "stackoverflow").addParameter("order", "desc").addParameter("access_token", "tDLfzlXAnWJXUn4pMFnc8w))").addParameter("key", "4te10xQDOUjDKbmiqVOkJg((");

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

                    insertAnswer.setInt(1, answer_id);
                    insertAnswer.setBoolean(2, is_accepted);
                    insertAnswer.setInt(3, score);
                    insertAnswer.setInt(4, question_id);
                    insertAnswer.addBatch();

                }
                insertAnswer.executeBatch();
                insertAnswer.clearBatch();

            }

        }

    }

    public void updateUserFromDB() throws SQLException, IOException {
        PreparedStatement getQuestionId = con.prepareStatement("select question_id from question limit 100;");
        ResultSet questionId = getQuestionId.executeQuery();
        JsonParser parser = new JsonParser();

        Map<Integer, Integer> postMap = new HashMap<>();
        Map<Integer, Integer> answerMap = new HashMap<>();
        Map<Integer, Integer> commentMap = new HashMap<>();
        Set<Integer> uid = new HashSet<>();

        while (questionId.next()) {
            int question_id = questionId.getInt(1);
            URI questionURI = URI.create("https://api.stackexchange.com/2.3/questions/" + question_id + "?order=desc&sort=activity&site=stackoverflow");
            URI answerURI = URI.create("https://api.stackexchange.com/2.3/questions/" + question_id + "/answers?order=desc&sort=activity&site=stackoverflow");

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
                        System.out.println("question " + user_question_id);

                        uid.add(user_question_id);
                        postMap.putIfAbsent(user_question_id, 0);
                        postMap.computeIfPresent(user_question_id, (k, v) -> v + 1);

                        for (JsonElement answerElement : answerReturn) {
                            JsonObject answerObject = answerElement.getAsJsonObject();
                            if (questionObject.get("owner").getAsJsonObject().get("account_id") != null &&
                                    questionObject.get("owner").getAsJsonObject().get("account_id").getAsString() != null &&
                                    questionObject.get("owner").getAsJsonObject().get("account_id").getAsInt() != -1) {
                                int user_answer_id = questionObject.get("owner").getAsJsonObject().get("account_id").getAsInt();
                                int answer_id = answerObject.get("answer_id").getAsInt();
                                System.out.println("answer " + user_answer_id);

                                uid.add(user_answer_id);
                                answerMap.putIfAbsent(user_answer_id, 0);
                                answerMap.computeIfPresent(user_answer_id, (k, v) -> v + 1);

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
                                            System.out.println("comment " + user_comment_id);

                                            uid.add(user_comment_id);
                                            commentMap.putIfAbsent(user_comment_id, 0);
                                            commentMap.computeIfPresent(user_comment_id, (k, v) -> v + 1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

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
        insertUser.clearBatch();

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
