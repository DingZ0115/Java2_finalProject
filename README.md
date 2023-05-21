# CS209 2023 Spring Final Project Report

## 1.	Contributors

| SID      | Name       | Contribution ratio | Github ID |
| -------- | ---------- | ------------------ | --------- |
| 12011736 | Zhe DING   | 50%                | DingZ0115 |
| 12011126 | Zexuan Jia | 50%                | Kazawaryu |



## 2.	Architecture Design

### 2.1	Front-end

This project uses **Vue3** as a front-end development framework, combined with component libraries such as **Element Plus** and **Echarts**, for data visualization. The structure is as follows:

```
Front-end
├─public
└─src
    ├─apis
    ├─assets
    ├─components
    ├─plugins
    ├─router
    ├─store
    ├─utils
    └─views
```

### 2.2	Back-end

This project adopts the **Spring SSM** & **MVC** framework at the back end, and generally divides the framework into common, config, controller, entity mapper and service layers. The structure is as follows:

```
CS209
└─project
    ├─common
    ├─config
    ├─controller
    ├─entity
    ├─mapper
    └─service
```

In SpringBoot, the project uses MyBatis to interact with the database, which facilitates decoupling and simplifies development, adopts declarative transaction processing, reduces the difficulty of using Java EE API, and performs lightweight development with outstanding effects. The basic function and structure of the four-layer framework are shown in the following figure:

<img src="images\backendFramework.png" alt="backendFramework" style="zoom:67%;" />

### 2.3	Front-end communication

#### Front-end

The front end uses axios to interact with the back end. In addition, the front end encapsulates the API code, making it easy to write and manage.

```js
import api from './apis'
app.config.globalProperties.$api = api;
```

```javascript
export function getTop5UpvoteTags() {
    return request({
        method: 'GET',
        url: '/tag/getTop5UpvoteTags'
    })
}
```

```javascript
import axios from "axios";
const request = axios.create({
    baseURL: 'http://10.24.125.235:8080',
    timeout: 5000
})
export default request
```

```javascript
this.$api.API.getTop5UpvoteTags().then((resp) => {
          _this.top5Upvote = resp.data.data.list1
          _this.xValue_upvote = resp.data.data.list2
          _this.showUpvotes()
        }).catch(err => {
          console.log(err);
        });
```

#### Back-end

```java
@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry);

    @Bean
    public Docket createRestApi();
}
```

Swagger is a specification and complete framework for generating, describing, invoking and visualizing RESTful web services. Swagger is a specification and complete framework for generating, describing, invoking and visualizing RESTful web services. To use the Swagger framework, you need to register the interface during initialization and open the relevant permissions. More details at `config\SwaggerConfig` .

### 2.4	Database

Since this project does not involve large-scale data interaction, a relational database is not used in the database, but a non-relational database is used lightly to speed up query and response. When adding or modifying data, the server judges whether the data is legal.

<img src="images\databaseFramework.png" alt="databaseFramework" style="zoom:50%;" />



## 3.	Crawler frame and ideas

```java
public void updateQuestionAndTagFromWeb() throws URISyntaxException, IOException, SQLException;
public void updateAnswerFromDB() throws SQLException, URISyntaxException, IOException;
public void updateJavaAPIFromWeb() throws IOException, SQLException;
public void updateUserFromDB() throws SQLException, IOException;
public void updateTags() throws SQLException, IOException;
```

The above four methods are used to update the database using the API interface crawler. The basic idea is: construct the access URI, apply for the return value in JSON format, process the returned data, and write to the database.  More details at `config\DBconfig` .

<img src="images\crawler.png" alt="crawler" style="zoom:80%;" />



## 4.	Code Details

### 4.1	Number of Answers

Details and implementation of the above method can be found in the **QuestionController, QuestionMapper, and QuestionService.**

#### 4.1.1 Get the ratio of questions without answer

To obtain the percentage of unanswered questions, simply count the number of questions with an answer number of 0 in the database and divide it by the total number of questions.

```java
public Result getNoAnswerRatio();
```

#### 4.1.2 Get the average and maximum

To obtain the average and maximum number of answers for a single question, simply perform a statistical analysis on the "answer number" column of the "Question" table in the database.

```java
public Result getMaxAnswer();
public Result getAverageAnswer();
```

#### 4.1.3 Get the distribution of number of answers

To perform a horizontal analysis of the number of answers for each question, divide the questions into eight sub-intervals based on the maximum number of answers, and count the number of questions in each sub-interval. Then, create a bar graph to display the results.

```java
public Result getDistributionOfAnswers() {
    int maxAnswerOfQuestion = questionMapper.getMaxAnswerOfQuestion();
    List<Range> listRange = new ArrayList<>();
    int r = (int) Math.round((double) maxAnswerOfQuestion / 8);
    int left = 0;
    int right = r;
    for (int i = 0; i < 8; i++) {
        ......
        listRange.add(new Range(tName, tValue));
    }
    return Result.ok().code(200).message("success").addData("distribution", listRange);
}
```

### 4.2	Accepted Answers 

Details and implementation of the above method can be found in the **QuestionController, QuestionMapper, and QuestionService.**

#### 4.2.1 Get the ratio of questions with accepted answer

Similar to 4.1.1, simply count the number of questions in the "Question" table of the database where the "accepted_answer" column is true.

```java
public Result getAcceptedQuestionCount();
```

#### 4.2.2 Get the distribution of answer accepted time to question post time

To calculate the distribution of the time difference between accepting an answer and posting a question, only questions that have accepted answers need to be considered. For each question with an accepted answer, retrieve the submission time of the question and the posting time of the accepted answer, calculate their difference, and perform a statistical analysis similar to 4.1.3.

For the small percentage of questions where the time difference exceeds 3000 hours, we consider them to be "zombie" questions. To ensure the data is more readable and analyzable, we will not discuss them here.

When performing the analysis, the time difference can be converted into minutes and seconds to create a scatter plot for display.

```java
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
```

#### 4.2.3 Get the ratio answers upvote better than accepted answers

For this question, only questions with accepted answers need to be considered. Find the ID of the question that has an accepted answer, and then find all other answers to that question by using the question ID. Calculate the maximum upvote value for each of the other answers, and compare it with the upvote value of the accepted answer to determine the highest upvote value among all answers to the question.

```java
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
```

### 4.3	Tags

Details and implementation of the above method can be found in the **TagController, TagMapper, and TagService.**

#### 4.3.1 Get tags appearing with Java

The answer to this question can be obtained by performing a statistical analysis on the "Question" table. When querying questions with the "Java" tag, other tags that appear together with the "Java" tag can be considered as co-occurring tags. Count the frequency of these co-occurring tags and sort them in descending order.

```java
public Result getTop15AppearWithJavaTags();
```

#### 4.3.2 Get maximum upvote count of tag or tags combination

To obtain statistics on upvote count, a filter of `!)4qcVpY9Tvb_cdttjBnShttVWo.l` was used. The upvote count can be found and counted in the "answers" field of the resulting data.

To obtain statistics and query data for a single question, you can use the following URL with the question ID:

```
https://api.stackexchange.com/2.2/questions/{question_id}/answers?order=desc&sort=votes&site=stackoverflow&filter=!)4qcVpY9Tvb_cdttjBnShttVWo.l&votes>=100
```

This will return information about the question with the specified ID, including the number of answers, the score of the question, and other relevant data. You can adjust the filter as needed to include or exclude certain fields from the response.

```java
public Result getTop5UpvoteTags();
public Result getTagsUpvoteComb();
```

#### 4.3.3 Get maximum view count of tag or tags combination

Similar to 4.3.2, the same method can be used to perform the statistics.

```java
public Result getTop5ViewTags();
public Result getTagsViewComb();
```

### 4.4	Users

Details and implementation of the above method can be found in the **UserController, UserMapper, and UserService.**

#### 4.4.1 Get the distribution of number of users who participated in thread discussions

The answer to this question can be obtained by performing a statistical analysis on the "Question" table. For each question, count the number of unique users who participated in the discussion, including users who posted answers and comments.

Note: Due to certain restrictions and limitations, it may not be possible to obtain a large amount of data for this analysis. The results should be considered as reference only.

The method for calculating the distribution is similar to that in 4.1.3.

```java
public Result getUserDistributionOfCommunication();
```

#### 4.4.2 Get the question answerers and the commenters

Similarly, the answer to this question is obtained from the "Question" table. When counting the number of participants in a question, the "comment count" and "answer count" fields of the corresponding users in the "User" table can be updated and counted. The method for calculating the distribution is the same as in 4.1.3.

```java
public Result getUserDistributionOfAnswer();
public Result getUserDistributionOfComment();
```

#### 4.4.3 Get the most active user

The statistics for this question come from three sources: the number of questions posted, the number of questions answered, and the number of questions discussed. For each user, calculate an activity score using the formula. Sort the users by their activity score to obtain a ranking of user activity.
$$
A_u = post\_num \times 0.2 + answer\_num \times 0.5 + comment\_num \times 0.3
$$
In addition, it is also possible to query for the users with the highest number of posted questions, answered questions, and discussed questions.

```java
public Result getMostActiveUser();
```

### 4.5	Frequently discussed  Java APIs 

How to obtain the popularity of the Java API seems to be a relatively difficult problem, but you can find the naming rules of the Java API by consulting the Java API documentation, that is, starting with java, javax, org and so on. Therefore, you only need to process the name of the tag when querying the tag, fuzzy query, and use regular expressions to exclude tags such as java-11 to correctly obtain the number of questions to fill in the Java API. 

This method is to count the problems that the Java API tag is correctly filled in, and does not consider the problems that are not filled in correctly.

The following are some of the Java API popularity data crawled:

| item_id | api_name              | appear_num |
| ------- | --------------------- | ---------- |
| 42      | java-stream           | 11383      |
| 43      | java-native-interface | 9653       |
| 13      | java.util.scanner     | 6401       |
| 44      | java-me               | 5780       |
| 45      | java-ee-6             | 2033       |
| 46      | java-io               | 1750       |

In the service layer, the method is simplified as follows:

```java
public Result getJavaAPI() {
    List<String> name = tagMapper.getJavaAPIName();
    List<String> num = tagMapper.getJavaAPINum();
    List<Range> items = new ArrayList<>();
    for (int i = 0; i < name.size(); i++) {
        items.add(new Range(name.get(i), num.get(i)));
    }
    return Result.ok().code(200).message("success").addData("list", items);
}
```

### 4.6	REST services 









