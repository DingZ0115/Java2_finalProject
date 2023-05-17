# CS209 2023 Spring Final Project Report

## 1.	Contributors

| SID      | Name       | Contribution ratio | Github ID |
| -------- | ---------- | ------------------ | --------- |
| 12011736 | Zhe DING   | 50%                | DingZ0115 |
| 12011126 | Zexuan Jia | 50%                | Kazawaryu |



## 2.	Architecture Design

### 2.1	Front-end

This project uses Vue3 as a front-end development framework, combined with component libraries such as Element Plus and Echarts, for data visualization. The structure is as follows:

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

### 2.3	Database

Since this project does not involve large-scale data interaction, a relational database is not used in the database, but a non-relational database is used lightly to speed up query and response. When adding or modifying data, the server judges whether the data is legal.

<img src="images\databaseFramework.png" alt="databaseFramework" style="zoom:50%;" />



## 3.	Code Details

### 3.1	Update data from Website

```java
public void updateQuestionAndTagFromWeb() throws URISyntaxException, IOException, SQLException;
public void updateAnswerFromDB() throws SQLException, URISyntaxException, IOException;
public void updateJavaAPIFromWeb() throws IOException, SQLException;
public void updateUserFromDB() throws SQLException, IOException;
```

The above four methods are used to update the database using the API interface crawler. The basic idea is: construct the access URI, apply for the return value in JSON format, process the returned data, and write to the database.  More details at `config\DBconfig` .

### 3.2	Front-end communication

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

### 3.3	Get Java API popularity

How to obtain the popularity of the Java API seems to be a relatively difficult problem, but you can find the naming rules of the Java API by consulting the Java API documentation, that is, starting with java, javax, org and so on. Therefore, you only need to process the name of the tag when querying the tag, fuzzy query, and use regular expressions to exclude tags such as java-11 to correctly obtain the number of questions to fill in the Java API. This method is to count the problems that the Java API tag is correctly filled in, and does not consider the problems that are not filled in correctly.

The following are some of the Java API popularity data crawled:

| item_id | api_name              | appear_num |
| ------- | --------------------- | ---------- |
| 42      | java-stream           | 11383      |
| 43      | java-native-interface | 9653       |
| 13      | java.util.scanner     | 6401       |
| 44      | java-me               | 5780       |
| 45      | java-ee-6             | 2033       |
| 46      | java-io               | 1750       |

