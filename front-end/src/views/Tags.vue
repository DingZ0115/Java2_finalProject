<template>
  <div class="tags">
    <el-radio-group v-model="showTable" @change="threeTableAPI" style="color: white">
      <el-radio-button label="frequency">Frequency</el-radio-button>
      <el-radio-button label="upvotes">Upvotes</el-radio-button>
      <el-radio-button label="views">Views</el-radio-button>
    </el-radio-group>

    <div class="show" v-if="showTable === 'frequency'">
      <div style="flex-basis: 70%;">
        <div id="wordCloudChart" style="height: 600px;"></div>
      </div>
      <div style="flex-basis: 30%;">
        <el-card style="text-align: left;width:80%" class="wrapperCard">
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
    <div v-if="showTable === 'upvotes'" class="parallel">
      <div style="flex-basis: 50%">

        <div id="upvoteChart" style="height:300px;width:600px;margin:auto"></div>

        <el-card :body-style="{ display: 'flex', flexWrap: 'wrap',
                justifyContent: 'center', alignItems: 'center' }" class="wrapperCardVote">
          <div style="flex-basis:30%;">
            <img style="width:100%;display: block;" src="../assets/YiJi.png" alt="">
          </div>
          <div style="flex-basis: 70%">
            <el-card class="showCard">
              <div style="color: #d84430;font-size: 20px; font-weight: bold;margin-bottom: 2%">
                Tags receive the most upvotes
              </div>
              <div v-for="(item, index) in top5Upvote" :key="index">
                <div class="text" style="display: inline">
                  <i class="el-icon-s-flag other"></i>
                  {{ index + 1 }}.&nbsp;&nbsp; {{ item }}
                </div>
              </div>
            </el-card>
          </div>
        </el-card>
      </div>

      <div style="flex-basis: 50%">
        <div id="upvoteComChart" style="height:300px;width:600px;margin:auto"></div>
        <el-card :body-style="{ display: 'flex', flexWrap: 'wrap',
                justifyContent: 'center', alignItems: 'center' }" class="wrapperCardVote">
          <div style="flex-basis: 70%">
            <el-card class="showCard">
              <div style="color: #d84430;font-size: 20px; font-weight: bold;margin-bottom: 2%">
                Tag combinations receive the most upvotes
              </div>
              <div v-for="(item, index) in top5UpvoteCom" :key="index">
                <div class="text" style="display: inline">
                  <i class="el-icon-s-flag other"></i>
                  {{ index + 1 }}.&nbsp;&nbsp; {{ item }}
                </div>
              </div>
            </el-card>
          </div>
          <div style="flex-basis:30%;">
            <img style="width:100%;display: block;" src="../assets/1.png" alt="">
          </div>
        </el-card>
      </div>
    </div>

    <div v-if="showTable === 'views'" class="parallel">
      <div style="flex-basis: 50%">

        <div id="viewChart" style="height:300px;width:600px;margin:auto"></div>

        <el-card :body-style="{ display: 'flex', flexWrap: 'wrap',
                justifyContent: 'center', alignItems: 'center' }" class="wrapperCardVote">
          <div style="flex-basis:30%;">
            <img style="width:100%;display: block;" src="../assets/2.png" alt="">
          </div>
          <div style="flex-basis: 70%">
            <el-card class="showCard">
              <div style="color: #d84430;font-size: 20px; font-weight: bold;margin-bottom: 2%">
                Tags receive the most views
              </div>
              <div v-for="(item, index) in top5view" :key="index">
                <div class="text" style="display: inline">
                  <i class="el-icon-s-flag other"></i>
                  {{ index + 1 }}.&nbsp;&nbsp; {{ item }}
                </div>
              </div>
            </el-card>
          </div>
        </el-card>
      </div>

      <div style="flex-basis: 50%">

        <div id="viewComChart" style="height:300px;width:600px;margin:auto"></div>

        <el-card :body-style="{ display: 'flex', flexWrap: 'wrap',justifyContent: 'center', alignItems: 'center' }"
                 class="wrapperCardVote">
          <div style="flex-basis: 70%">
            <el-card class="showCard">
              <div style="color: #d84430;font-size: 20px; font-weight: bold;margin-bottom: 2%">
                Tag combination receive the most views
              </div>
              <div v-for="(item, index) in list1" :key="index" style="margin-bottom: 2%">
                <div class="text" style="display: inline">
                  {{ index + 1 }}. &nbsp;&nbsp;
                </div>
                <el-tag v-for="(xx, innerIndex) in item" :key="innerIndex" :type="types[innerIndex]">{{ xx }}</el-tag>
              </div>
            </el-card>
          </div>
          <div style="flex-basis:30%;">
            <img style="width:100%;display: block;" src="../assets/3.png" alt="">
          </div>
        </el-card>
      </div>
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
      topTags: [],
      top5Upvote: [],
      top5UpvoteCom: [],
      top5view: [],
      top5viewCom: [],
      showTable: 'frequency',
      xValue_upvote: [],
      xValue_upvoteCom: [],
      xValue_view: [],
      xValue_viewCom: [],
      types: ["warning", "success", "danger", "info", "primary", "text"],
      list1: [],
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
    showUpvotesCom() {
      const chart = echarts.init(document.getElementById('upvoteComChart'))
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
          data: this.top5UpvoteCom
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
          data: this.xValue_upvoteCom,
          itemStyle: {
            normal: {
              borderRadius: [3, 20, 20, 3],
              color: function (params) {   // 设置柱形图的颜色
                if (params.dataIndex === 4) {
                  return '#82009a'
                } else if (params.dataIndex === 3) {
                  return '#be17e0'
                } else if (params.dataIndex === 2) {
                  return '#be61e1'
                } else {
                  return '#d3a6e3'
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
                  return '#016e04'
                } else if (params.dataIndex === 3) {
                  return '#05b213'
                } else if (params.dataIndex === 2) {
                  return '#59da56'
                } else {
                  return '#b9f8b4'
                }
              }
            },
          }
        }]
      };
      chart.setOption(option);
    },
    showViewsCom() {
      const chart = echarts.init(document.getElementById('viewComChart'))
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
          data: this.top5viewCom
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
          data: this.xValue_viewCom,
          itemStyle: {
            normal: {
              borderRadius: [3, 20, 20, 3],
              color: function (params) {   // 设置柱形图的颜色
                if (params.dataIndex === 4) {
                  return '#6e2e01'
                } else if (params.dataIndex === 3) {
                  return '#b24705'
                } else if (params.dataIndex === 2) {
                  return '#da8956'
                } else {
                  return '#f8cc8b'
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
        this.$api.API.getTagsUpvoteComb().then((resp) => {
          _this.top5UpvoteCom = resp.data.data.list1
          _this.xValue_upvoteCom = resp.data.data.list2
          _this.showUpvotesCom()
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
        this.$api.API.getTagsViewComb().then((resp) => {
          _this.top5viewCom = resp.data.data.list1
          _this.xValue_viewCom = resp.data.data.list2
          _this.list1 = []
          for (const ele of _this.top5viewCom) {
            const temp = ele.split(', ');
            _this.list1.push(temp)
          }
          _this.showViewsCom()
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
.parallel {
  display: flex;
  flex-wrap: wrap;
  text-align: center;
  justify-content: center;
  align-items: center;
  margin-top: 2%;
}

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
  margin-bottom: 1.5%;
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
  width: 90%;
  margin: auto;
}

.textX {
  font-size: 18px;
  line-height: 1;
  font-weight: bold;
  text-align: left;
}
</style>