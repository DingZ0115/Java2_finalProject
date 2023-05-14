<template>
  <div class="tags">
    <el-radio-group v-model="showTable" @change="threeTableAPI" style="color: white">
      <el-radio-button label="frequency">Frequency</el-radio-button>
      <el-radio-button label="upvotes">Upvotes</el-radio-button>
      <el-radio-button label="views">Views</el-radio-button>
    </el-radio-group>

    <div class="show" v-if="showTable === 'frequency'">
      <div style="flex-basis: 65%;">
        <div id="wordCloudChart" style="height: 600px;"></div>
      </div>
      <div style="flex-basis: 35%;">
        <el-card style="text-align: left;width:70%" class="wrapperCard">
          <el-scrollbar height="560px">
            <div v-for="(item, index) in topTags" :key="index">
              <el-card class="showCard">
                <div class="textX">
                  <i class="el-icon-location other"></i>
                  {{ index + 1 }}. {{ item.name }}
                </div>
              </el-card>
            </div>
          </el-scrollbar>
        </el-card>
      </div>
    </div>
    <div v-if="showTable === 'upvotes'">
      <div id="upvoteChart" style="height:300px;width:700px;margin:auto"></div>

      <el-card :body-style="{ display: 'flex', flexWrap: 'wrap', textAlign: 'center',
                justifyContent: 'center', alignItems: 'center' }" class="wrapperCardVote">
        <div style="flex-basis: 70%">
          <el-card class="showCard">
            <h3 style="color: blueviolet">tags or tag combinations receive the most upvotes</h3>
            <div v-for="(item, index) in top5Upvote" :key="index">
              <div class="text">
                <i class="el-icon-s-flag other"></i>
                {{ index + 1 }}. {{ item }}
              </div>
            </div>
          </el-card>
        </div>
        <div style="flex-basis: 30%;">
          <img style="width:200px;display: block;" src="../assets/YiJi.png" alt="">
        </div>
      </el-card>
    </div>

    <div v-if="showTable === 'views'">
      <div id="viewChart" style="height:300px;width:700px;margin:auto"></div>
      <el-card :body-style="{ display: 'flex', flexWrap: 'wrap', textAlign: 'center',
                justifyContent: 'center', alignItems: 'center' }" class="wrapperCardVote">
        <div style="flex-basis: 70%">
          <el-card class="showCard">
            <h3 style="color: blueviolet">tags or tag combinations receive the most views</h3>
            <div v-for="(item, index) in top5view" :key="index">
              <div class="text">
                <i class="el-icon-s-flag other"></i>
                {{ index + 1 }}. {{ item }}
              </div>
            </div>
          </el-card>
        </div>
        <div style="flex-basis: 30%;">
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
        {value: 24, name: 'ONEPLUS'},],
      top5Upvote: [],
      top5view: [],
      topTags: [],
      showTable: 'frequency',
      xValue_upvote: [],
      xValue_view: [],
    }
  },
  methods: {
    drawWordCloud() {
      const wordChart = echarts.init(document.getElementById("wordCloudChart"));
      const wordOpt = {
        title: {
          text: 'Tags frequently appear together with the java tag',
          textStyle: {
            fontStyle: 'oblique',
            fontSize: 20,
            color: '#fff'
          },
          left: 'center'
        },
        visualMap: {
          type: 'continuous',
          min: 16,
          max: 80,
          color: ['#ffd500', '#32c9f6'],
          textStyle: {
            color: '#fff'
          }
        },
        tooltip: {},
        series: [{
          type: 'wordCloud',
          shape: {
            cloudGrow: 0.2,
          },
          sizeRange: [10, 70],
          rotationRange: [-30, 30],
          gridSize: 10,
          drawOutOfBound: false,
          layoutAnimation: true,
          keepAspect: true,
          textStyle: {
            fontWeight: 'bold',
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
    },
    showUpvotes() {
      const chart = echarts.init(document.getElementById('upvoteChart'))
      const option = {
        color: ['#d84430'],
        tooltip: {
          show: true
        },
        yAxis: {
          axisTick: {
            show: false
          },
          axisLine: {
            show: false,
          },
          axisLabel: {
            inside: true,
            verticalAlign: 'bottom',
            lineHeight: 40,
            color: '#DDDFEB',
            rich: {
              other: {
                color: '#DDDFEB',
                opacity: 0.57
              },
              first: {
                color: '#DDDFEB'
              }
            }
          },
          data: this.top5Upvote
        },
        xAxis: {
          nameTextStyle: {
            color: 'rgba(255, 255, 255, 0.8)',
            align: 'right'
          },
          splitLine: {
            show: false,
          },
          axisLine: {
            show: false,
          },
          axisLabel: {
            color: 'rgba(255, 255, 255, 0.8)'
          },
        },
        grid: {
          top: '0%',
          bottom: '0%',
          left: '0%',
          right: '0%'
        },
        series: [{
          name: '排行榜',
          barWidth: 15,
          type: 'bar',
          data: this.xValue_upvote,
          itemStyle: {
            normal: {
              borderRadius: [3, 20, 20, 3],
              color: function (params) {   // 设置柱形图的颜色
                if (params.dataIndex === 4) {
                  return '#d84430'
                } else if (params.dataIndex === 3) {
                  return '#f38237'
                } else if (params.dataIndex === 2) {
                  return '#e2aa20'
                } else {
                  return '#608289'
                }
              }
            },
          }
        }]
      };
      // 使用刚指定的配置项和数据显示图表。
      chart.setOption(option);
    },
    showViews() {
      const chart = echarts.init(document.getElementById('viewChart'))
      const option = {
        color: ['#d84430'],
        tooltip: {
          show: true
        },
        yAxis: {
          axisTick: {
            show: false
          },
          axisLine: {
            show: false,
          },
          axisLabel: {
            inside: true,
            verticalAlign: 'bottom',
            lineHeight: 40,
            color: '#DDDFEB',
            rich: {
              other: {
                color: '#DDDFEB',
                opacity: 0.57
              },
              first: {
                color: '#DDDFEB'
              }
            }
          },
          data: this.top5view
        },
        xAxis: {
          nameTextStyle: {
            color: 'rgba(255, 255, 255, 0.8)',
            align: 'right'
          },
          splitLine: {
            show: false,
          },
          axisLine: {
            show: false,
          },
          axisLabel: {
            color: 'rgba(255, 255, 255, 0.8)'
          },
        },
        grid: {
          top: '0%',
          bottom: '0%',
          left: '0%',
          right: '0%'
        },
        series: [{
          name: '排行榜',
          barWidth: 15,
          type: 'bar',
          data: this.xValue_view,
          itemStyle: {
            normal: {
              borderRadius: [3, 20, 20, 3],
              color: function (params) {   // 设置柱形图的颜色
                if (params.dataIndex === 4) {
                  return '#d84430'
                } else if (params.dataIndex === 3) {
                  return '#f38237'
                } else if (params.dataIndex === 2) {
                  return '#e2aa20'
                } else {
                  return '#608289'
                }
              }
            },
          }
        }]
      };
      chart.setOption(option);
    },
    threeTableAPI() {
      const _this = this
      if (this.showTable === 'frequency') {
        this.$api.API.getTop15AppearWithJavaTags().then((resp) => {
          _this.information = resp.data.data.list
          _this.topTags = resp.data.data.list
          _this.drawWordCloud()
        }).catch(err => {
          console.log(err);
        });
      } else if (this.showTable === 'upvotes') {
        this.$api.API.getTop5UpvoteTags().then((resp) => {
          _this.top5Upvote = resp.data.data.list1
          _this.xValue_upvote = resp.data.data.list2
          _this.showUpvotes()
        }).catch(err => {
          console.log(err);
        });
      } else if (this.showTable === 'views') {
        this.$api.API.getTop5ViewTags().then((resp) => {
          _this.top5view = resp.data.data.list1
          _this.xValue_view = resp.data.data.list2
          _this.showViews()
        }).catch(err => {
          console.log(err);
        });
      }
    }
  },
  mounted() {
    this.threeTableAPI()
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

.showCard {
  border: 1px solid #dccfcf;
  box-shadow: 0 0 25px #909399;
  border-radius: 20px;
  background-color: rgba(255, 255, 255, 0.75);
  margin-bottom: 2.5%;
}

.wrapperCard {
  border: 1px solid #dccfcf;
  box-shadow: 0 0 25px #909399;
  border-radius: 20px;
  background-color: rgba(255, 255, 255, 0.1);
}

.wrapperCardVote {
  border: 1px solid #dccfcf;
  box-shadow: 0 0 25px #909399;
  border-radius: 20px;
  background-color: rgba(255, 255, 255, 0.1);
  text-align: left;
  width: 55%;
  margin: auto;
}

.textX {
  font-size: 18px;
  line-height: 1;
  font-weight: bold;
  text-align: left;
}
</style>