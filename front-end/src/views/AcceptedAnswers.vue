<template>
  <div class="acceptedAnswers">
    <div class="show" style="margin-top: 7%;">
      <div style="flex-basis: 47%">
        <!--        <el-card class="showCard">-->
        <!--          <div class="text">-->
        <!--            <i class="el-icon-coordinate"></i>-->
        <!--            percentage of questions have accepted answers: {{ percentage }}-->
        <!--          </div>-->
        <!--          <div class="text">-->
        <!--            <i class="el-icon-coordinate"></i>-->
        <!--            percentage of questions have accepted answers that have received-->
        <!--          </div>-->
        <!--          <div class="text">&nbsp &nbsp more upvotes than the accepted answers: {{ moreUpvote }}-->
        <!--          </div>-->
        <!--        </el-card>-->
        <div class="lightline"></div>
        <ol>
          <li v-for="(item, index) in indoorParams" :key="index">
            <div class="animate-border">
              <i></i>
              <div style="color:#fff;margin: 3%">
                <div class="text">
                  <div class="el-icon-coordinate"></div>
                  percentage of questions have accepted answers:
                  <span style="color:#fdda2c; font-size: 25px; font-weight: bold;">{{ percentage }}</span>
                </div>

                <div class="text">
                  <div class="el-icon-coordinate"></div>
                  percentage of questions have accepted answers that have
                  <br>
                  &nbsp &nbsp received more upvotes than the accepted answers:
                  <span style="color:#fdda2c; font-size: 25px; font-weight: bold;">{{ moreUpvote }}</span>
                </div>
              </div>
              <i></i>
            </div>
          </li>
        </ol>
      </div>
      <div style="flex-basis: 50%">
        <div id="chart" style="height:400px;background-color: #00133d"></div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import {ref} from "vue";
import {getBetterRatio} from "@/apis/API";

export default {
  setup() {
    let indoorParams = ref([1]);
    return {indoorParams};
  },
  name: "AcceptedAnswers",
  data() {
    return {
      percentage: 5,
      moreUpvote: 9,
      chartData: []
    }
  },
  mounted() {
    const _this = this
    this.$api.API.getAcceptedQuestionCount().then((resp) => {
      _this.percentage = resp.data.data.ratio
    }).catch(err => {
      console.log(err);
    });
    this.$api.API.getBetterRatio().then((resp) => {
      _this.moreUpvote = resp.data.data.ratio
    }).catch(err => {
      console.log(err);
    });

    this.$api.API.getDistrutionOfQuestionDeltaTimes().then((resp) => {
      console.log(resp)
      _this.chartData = resp.data.data.distribution
      this.$nextTick(() => {
        const chart = echarts.init(document.getElementById("chart"));
        const option = {
          title: {
            text: 'The distribution of question resolution time',
            textStyle: {
              fontStyle: 'oblique',
              fontSize: 20,
              color: '#fff'
            },
            left: 'center'
          },
          tooltip: {
            show: true,
            formatter: function (params) {
              const data = params.data;
              return 'x: ' + data[0] + '<br/>y: ' + data[1];
            }
          },
          xAxis: {
            textStyle: {
              color: 'white'
            },
            name: 'minutes',
          },
          yAxis: {
            textStyle: {
              color: 'white'
            },
            name: 'seconds',
          },
          visualMap: {
            min: 0,
            max: 60,
            dimension: 1,
            orient: 'vertical',
            right: 10,
            top: 'center',
            text: ['HIGH', 'LOW'],
            calculable: true,
            inRange: {
              color: ['#f2c31a', '#8efa5b']
            },
            textStyle: {
              color: 'white'
            }
          },
          series: [{
            type: 'scatter',
            symbolSize: 10,
            data: _this.chartData,
          }],
          textStyle: {
            color: 'white'
          }
        };
        chart.setOption(option)
      })
    }).catch(err => {
      console.log(err);
    });

  }
}
</script>

<style scoped>
/*流光动画*/
ol li {
  border: 1px solid rgb(253, 218, 0);
  /* 宽高和相对定位是一定要给的,因为这会影响.animate-border子元素的定位 */
  position: relative;
  width: 98%;
  height: 180px;
  overflow: hidden;
}

ol li .animate-border {
  position: absolute;
  top: 0px;
  width: 100%;
  height: 100%;
}

ol li .animate-border::before,
ol li .animate-border::after {
  content: "";
  position: absolute;
  width: 100%;
  height: 2px;
}

ol li .animate-border i {
  position: absolute;
  display: inline-block;
  height: 100%;
  width: 2px;
}

ol li .animate-border::before {
  top: 0;
  left: -100%;
  background-image: linear-gradient(
      90deg,
      transparent,
      #fdda2c,
      transparent
  );
  animation: one 4s linear infinite;
}

ol li .animate-border i:nth-child(1) {
  top: -100%;
  right: 0;
  background-image: linear-gradient(
      180deg,
      transparent,
      #fdda2c,
      transparent
  );
  animation: two 4s linear 1s infinite;
}

ol li .animate-border::after {
  bottom: 0;
  right: -100%;
  background-image: linear-gradient(
      -90deg,
      transparent,
      #fdda2c,
      transparent
  );
  animation: three 4s linear 2s infinite;
}

ol li .animate-border i:nth-child(2) {
  bottom: -100%;
  left: 0;
  background-image: linear-gradient(
      360deg,
      transparent,
      #fdda2c,
      transparent
  );
  animation: four 4s linear 3s infinite;
}

@keyframes one {
  0% {
    left: -100%;
  }
  50%,
  100% {
    left: 100%;
  }
}

@keyframes two {
  0% {
    top: -100%;
  }
  50%,
  100% {
    top: 100%;
  }
}

@keyframes three {
  0% {
    right: -100%;
  }
  50%,
  100% {
    right: 100%;
  }
}

@keyframes four {
  0% {
    bottom: -100%;
  }
  50%,
  100% {
    bottom: 100%;
  }
}

.lightline {
  margin: auto;
  width: 250px;
  height: 2px;
  background-image: linear-gradient(90deg, transparent, #fdda2c, transparent);
}
</style>