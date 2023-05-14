<template>
  <div class="numberOfAnswers">
    <div class="show" style="margin-top: 7%;">
      <div style="flex-basis: 47%">
        <div class="lightline"></div>
        <ol>
          <li v-for="(item, index) in indoorParams" :key="index">
            <div class="animate-border">
              <i></i>
              <div style="color:#fff;margin: 3%">
                <div class="text">
                  <div class="el-icon-bell"></div>
                  percentage of questions don't have any answer:
                  <span style="color: #03e9f4; font-size: 25px; font-weight: bold;">{{ percentage }}</span>
                </div>

                <div class="text">
                  <div class="el-icon-bell"></div>
                  average number of answers:
                  <span style="color: #03e9f4; font-size: 25px; font-weight: bold;">{{ average }}</span>
                </div>

                <div class="text">
                  <div class="el-icon-bell"></div>
                  maximum number of answers:
                  <span style="color: #03e9f4; font-size: 25px; font-weight: bold;">{{ maximum }}</span>
                </div>
              </div>

              <i></i>
            </div>
          </li>
        </ol>
      </div>
      <div style="flex-basis: 50%">
        <div id="chart" style="height:400px;"></div>
        <!--        <span style="color: white">有0-4个answer的question有多少个</span>-->
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import {ref} from "vue";

export default {
  setup() {
    let indoorParams = ref([1]);
    return {indoorParams};
  },
  name: "NumberOfAnswers",
  data() {
    return {
      percentage: "",
      average: "",
      maximum: "",
      information: [
        {
          name: "国自然",
          value: 100,
          used: 50,
          rest: 50,
          execute_rate: "50%",
          qualify: "是"
        },
        {
          name: "中央财政支持地方高校经费",
          value: 200,
          used: 100,
          rest: 50,
          execute_rate: "50%",
          qualify: "是"
        },
        {
          name: "高水平",
          value: 300,
          used: 150,
          rest: 50,
          execute_rate: "50%",
          qualify: "是"
        }],
      names: []
    }
  },
  mounted() {
    const _this = this
    this.$api.API.getNoAnswerRatio().then((resp) => {
      _this.percentage = resp.data.data.ratio
    }).catch(err => {
      console.log(err);
    });
    this.$api.API.getAverageAnswer().then((resp) => {
      _this.average = resp.data.data.count
    }).catch(err => {
      console.log(err);
    });
    this.$api.API.getMaxAnswer().then((resp) => {
      _this.maximum = resp.data.data.count
    }).catch(err => {
      console.log(err);
    });

    this.$api.API.getDistributionOfAnswers().then((resp) => {
      console.log(resp)
      _this.information = resp.data.data.distribution
      for (const info of _this.information) {
        _this.names.push(info.name);
      }
      this.$nextTick(() => {
        const chart = echarts.init(document.getElementById("chart"));
        const option = {
          title: {
            text: 'The distribution of the number of answers',
            textStyle: {
              fontStyle: 'oblique',
              fontSize: 20,
              color: '#fff'
            },
            left: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: '{b} : {c} ({d}%)'
          },
          legend: {
            bottom: 10,
            left: 'center',
            data: _this.names,
            textStyle: {
              color: 'white' // 设置轴标签的文本颜色为白色
            }
          },
          series: [
            {
              type: 'pie',
              radius: '65%',
              center: ['50%', '50%'],
              selectedMode: 'single',
              data: _this.information,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              },
              label: {
                show: true,
                color: 'white' // 设置标签文本颜色为白色
              }
            }
          ]
        }
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
  border: 2px solid rgba(32, 254, 255, 0.3);
  /* 宽高和相对定位是一定要给的,因为这会影响.animate-border子元素的定位 */
  position: relative;
  width: 100%;
  height: 200px;
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
      #03e9f4,
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
      #03e9f4,
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
      #03e9f4,
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
      #03e9f4,
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
  background-image: linear-gradient(90deg, transparent, #03e9f4, transparent);
}
</style>