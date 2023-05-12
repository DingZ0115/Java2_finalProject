<template>
  <div class="tags">
    <div class="show">
      <div style="flex-basis: 50%;">
        <div id="wordCloudChart" style="height: 600px;"></div>
      </div>
      <div style="flex-basis: 35%">
        <el-card style="text-align: left;width:80%">
          <div v-for="x in 5">
            <div class="text">
              <i class="el-icon-medal first" v-if="x===1"></i>
              <i class="el-icon-location other" v-if="x!==1"></i>
              {{ x }}. maximum number of answers
            </div>
          </div>
        </el-card>
      </div>
    </div>
    <el-divider>
      <i class="el-icon-star-on"></i>
    </el-divider>
    <!--    <div class="show" style="margin-bottom: 5%">-->
    <!--      <div style="flex-basis: 50%;display: flex; justify-content: center;">-->
    <!--        <el-card style="text-align: left; flex: 0.7;">-->
    <!--          <div v-for="x in 5">-->
    <!--            <div class="text">-->
    <!--              <i class="el-icon-medal first" v-if="x===1"></i>-->
    <!--              <i class="el-icon-s-flag other" v-if="x!==1"></i>-->
    <!--              {{ x }}. maximum number of answers-->
    <!--            </div>-->
    <!--          </div>-->
    <!--        </el-card>-->
    <!--      </div>-->
    <!--    </div>-->
    <div style="margin-bottom: 5%;">
      <el-card style="text-align: left;width: 60%;margin: auto;margin-bottom: 2%;"
               :body-style="{ display: 'flex', flexWrap: 'wrap', textAlign: 'left',
                justifyContent: 'center', alignItems: 'center' }">
        <div style="flex-basis: 60%;margin-left: 5%">
          <h3 style="color: blueviolet">tags or tag combinations receive the most upvotes</h3>
          <div v-for="(item, index) in top5Upvote" :key="index">
            <div class="text">
              <i class="el-icon-s-flag other"></i>
              {{ index + 1 }}. {{ item }}
            </div>
          </div>
        </div>
        <div style="flex-basis: 20%">
          <img style="width:200px;display: block;" src="../assets/YiJi.png" alt="">
        </div>
      </el-card>

      <el-card style="text-align: left;width: 60%;margin: auto;margin-bottom: 2%;"
               :body-style="{ display: 'flex', flexWrap: 'wrap', textAlign: 'left',
                justifyContent: 'center', alignItems: 'center' }">
        <div style="flex-basis: 60%;margin-left: 5%">
          <h3 style="color: blueviolet">tags or tag combinations receive the most views</h3>
          <div v-for="(item, index) in top5view" :key="index">
            <div class="text">
              <i class="el-icon-s-flag other"></i>
              {{ index + 1 }}. {{ item }}
            </div>
          </div>
        </div>
        <div style="flex-basis: 20%">
          <img style="width:200px;display: block;" src="../assets/YiJi.png" alt="">
        </div>
      </el-card>
    </div>
  </div>
</template>
<script>
import * as echarts from 'echarts';
import "@/plugins/echarts-wordcloud.min.js";

export default {
  name: "Tags",
  data() {
    return {
      information: [
        {value: 30, name: 'VIVO'},
        {value: 29, name: 'OPPO'},
        {value: 28, name: 'HONOR'},
        {value: 27, name: 'iPhone 12 pro max'},
        {value: 26, name: 'iPhone 12 pro max'},
        {value: 25, name: 'HUAWEI MATE 10'},
        {value: 24, name: 'ONEPLUS'},
        {value: 23, name: 'Lenova T470'},
        {value: 22, name: 'MacBook Air '},
        {value: 21, name: 'SAMSUNG'},
        {value: 20, name: 'iPad mini'},
        {value: 16, name: 'BLACKBERRY'},
        {value: 14, name: 'OPPO'},
        {value: 13, name: 'SAMSUNG'},
        {value: 12, name: '361'},
        {value: 10, name: 'Lenova'}],
      top5Upvote: [],
      top5view: [],
    }
  },

  mounted() {
    const _this = this
    this.$api.API.getTop5UpvoteTags().then((resp) => {
      console.log(resp)
      _this.top5Upvote = resp.data.data.list
    }).catch(err => {
      console.log(err);
    });
    this.$api.API.getTop5ViewTags().then((resp) => {
      console.log(resp)
      _this.top5view = resp.data.data.list
    }).catch(err => {
      console.log(err);
    });

    const wordChart = echarts.init(document.getElementById("wordCloudChart"));
    const wordOpt = {
      title: {
        text: 'Tags frequently appear together with the java tag',
        textStyle: {
          fontStyle: 'oblique',
          fontSize: 20,
          color: '#0c56ff'
        },
        left: 'center'
      },
      tooltip: {},
      series: [{
        type: 'wordCloud',
        shape: {
          cloudGrow: 0.2
        },
        sizeRange: [10, 60],
        rotationRange: [-30, 30],
        gridSize: 2,
        drawOutOfBound: false,
        layoutAnimation: true,
        keepAspect: true,
        textStyle: {
          fontWeight: 'bold',
          color: function () {
            return 'rgb(' + [
              Math.round(Math.random() * 160),
              Math.round(Math.random() * 160),
              Math.round(Math.random() * 160)
            ].join(',') + ')';
          }
        },
        emphasis: {
          textStyle: {
            shadowBlur: 15,
            shadowColor: '#333'
          }
        },
        data: this.information
      }]
    };
    wordChart.setOption(wordOpt);
  }
}
</script>

<style scoped>
.first {
  color: #ff0000;
}

.other {
  color: goldenrod;
}
</style>