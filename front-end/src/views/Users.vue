<template>
  <div class="users">
    <el-radio-group v-model="showTable" @change="threeTableAPI" style="color: white;margin-bottom: 2%">
      <el-radio-button label="post">Post</el-radio-button>
      <el-radio-button label="answer">Answer</el-radio-button>
      <el-radio-button label="comment">Comment</el-radio-button>
      <el-radio-button label="communication">Communication</el-radio-button>
      <el-radio-button label="active">Active Top3</el-radio-button>
    </el-radio-group>

    <div style="width: 60%;margin: auto" v-if="showTable === 'post'">
      <div class="title">
        <i class="el-icon-circle-check"></i>
        The distribution of users who post the question in a thread
      </div>
      <div id="chart1" style="height:500px;"></div>
    </div>
    <div style="width: 60%;margin: auto" v-if="showTable === 'answer'">
      <div class="title">
        <i class="el-icon-circle-check"></i>
        The distribution of users who answers in a thread
      </div>
      <div id="chart2" style="height:500px;"></div>
    </div>
    <div style="width: 60%;margin: auto" v-if="showTable === 'comment'">
      <div class="title">
        <i class="el-icon-circle-check"></i>
        The distribution of users who comments in a thread
      </div>
      <div id="chart3" style="height:500px;"></div>
    </div>
    <div style="width: 60%;margin: auto" v-if="showTable === 'communication'">
      <div class="title">
        <i class="el-icon-circle-check"></i>
        The distribution of users who communicate in a thread
      </div>
      <div id="chart4" style="height:500px;"></div>
    </div>
    <div style="width: 60%;margin: auto" v-if="showTable === 'active'">
      <div class="title">
        <i class="el-icon-circle-check"></i>
        The most active users discussed in java thread
      </div>
      <span style="color: white">0.2* ( # post questions) +0.5*( # answer questions) +0.3* (# comment)</span>
      <div id="chart5" style="height:400px;width:500px;margin:auto"></div>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";

export default {
  name: "Users",
  data() {
    return {
      percentage: 5,
      average: 9,
      maximum: 8,
      information: [2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3],
      showTable: "post",
      postTable: [],
      answerName: [],
      answerValue: [],
      commentName: [],
      commentValue: [],
      communicationName: [],
      communicationValue: [],
      topActiveUser: []
    }
  },
  methods: {
    threeTableAPI() {
      let _this = this
      if (this.showTable === 'post') {
        this.$api.API.getUserDistributionOfPost().then(resp => {
          _this.postTable = resp.data.data.distribution
          _this.visualizePostTable()
        }).catch(err => {
          console.log(err);
        });
      } else if (this.showTable === 'answer') {
        this.$api.API.getUserDistributionOfAnswer().then(resp => {
          _this.answerName = []
          _this.answerValue = []
          for (const ele of resp.data.data.distribution) {
            _this.answerName.push(ele.name)
            _this.answerValue.push(ele.value)
          }
          _this.visualizeAnswerTable()
        }).catch(err => {
          console.log(err);
        });
      } else if (this.showTable === 'comment') {
        this.$api.API.getUserDistributionOfComment().then(resp => {
          console.log(resp)
          _this.commentName = []
          _this.commentValue = []
          for (const ele of resp.data.data.distribution) {
            _this.commentName.push(ele.name)
            _this.commentValue.push(ele.value)
          }
          _this.visualizeCommentTable()
        }).catch(err => {
          console.log(err);
        });
      } else if (this.showTable === 'communication') {
        this.$api.API.getUserDistributionOfCommunication().then(resp => {
          console.log(resp)
          _this.communicationName = []
          _this.communicationValue = []
          for (const ele of resp.data.data.distribution) {
            _this.communicationName.push(ele.name)
            _this.communicationValue.push(ele.value)
          }
          _this.visualizeCommunicationTable()
        }).catch(err => {
          console.log(err);
        });
      } else {
        this.$api.API.getMostActiveUser().then(resp => {
          _this.topActiveUser = []
          const data = resp.data.data.user
          _this.topActiveUser.push("🥈" + data[1])
          _this.topActiveUser.push("🥇" + data[0])
          _this.topActiveUser.push("🥉" + data[2])
          _this.showActiveTop3()
        }).catch(err => {
          console.log(err);
        });

      }
    },
    visualizePostTable() {
      const chart1 = echarts.init(document.getElementById("chart1"));
      const option = {
        legend: {
          top: 'bottom',
          textStyle: {
            color: 'white'
          }
        },
        toolbox: {
          show: true,
          feature: {
            mark: {show: true},
            dataView: {show: true, readOnly: false},
            restore: {show: true},
            saveAsImage: {show: true}
          }
        },
        series: [
          {
            name: 'Nightingale Chart',
            type: 'pie',
            radius: [25, 180],
            center: ['50%', '50%'],
            roseType: 'area',
            itemStyle: {
              borderRadius: 8
            },
            data: this.postTable,
            label: {
              show: true,
              color: 'white' // 设置标签文本颜色为白色
            }
          }],
      };
      chart1.setOption(option)
    },
    visualizeAnswerTable() {
      const chart2 = echarts.init(document.getElementById("chart2"));
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            crossStyle: {
              color: '#999'
            }
          }
        },
        toolbox: {
          feature: {
            restore: {show: true},
            saveAsImage: {show: true}
          }
        },
        legend: {
          data: ['distribution_bar', 'distribution_line'],
          textStyle: {
            color: 'white'
          }
        },
        xAxis: [
          {
            type: 'category',
            name: '# answers',
            data: this.answerName,
            axisPointer: {
              type: 'shadow'
            },
            textStyle: {
              color: 'white' // 设置轴标签的文本颜色为白色
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: '# users',
            min: 0,
            max: 120,
            interval: 20,
            textStyle: {
              color: 'white' // 设置轴标签的文本颜色为白色
            }
          },
        ],
        series: [
          {
            name: 'distribution_bar',
            type: 'bar',
            data: this.answerValue,
            itemStyle: {
              // 设置渐变色
              color: new echarts.graphic.LinearGradient(
                  0, 0, 0, 1,
                  [
                    {offset: 0, color: '#c845ff'},
                    {offset: 0.5, color: '#c60df1'},
                    {offset: 1, color: '#f500fd'}
                  ]
              )
            },
          },
          {
            name: 'distribution_line',
            type: 'line',
            data: this.answerValue,
            itemStyle: {
              color: '#f0be18'
            }
          }],
        textStyle: {
          color: 'white'
        }
      };
      chart2.setOption(option)
    },
    visualizeCommentTable() {
      const chart3 = echarts.init(document.getElementById("chart3"));
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            crossStyle: {
              color: '#999'
            }
          }
        },
        toolbox: {
          feature: {
            restore: {show: true},
            saveAsImage: {show: true}
          }
        },
        legend: {
          data: ['distribution_bar', 'distribution_line'],
          textStyle: {
            color: 'white'
          }
        },
        xAxis: [
          {
            type: 'category',
            name: '# comments',
            data: this.commentName,
            axisPointer: {
              type: 'shadow'
            },
            textStyle: {
              color: 'white' // 设置轴标签的文本颜色为白色
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: '# users',
            min: 0,
            max: 120,
            interval: 20,
            textStyle: {
              color: 'white' // 设置轴标签的文本颜色为白色
            }
          },
        ],
        series: [
          {
            name: 'distribution_bar',
            type: 'bar',
            data: this.commentValue,
            itemStyle: {
              // 设置渐变色
              color: new echarts.graphic.LinearGradient(
                  0, 0, 0, 1,
                  [
                    {offset: 0, color: '#f5d90c'},
                    {offset: 0.5, color: '#f0be18'},
                    {offset: 1, color: '#f09218'}
                  ]
              )
            },
          },
          {
            name: 'distribution_line',
            type: 'line',
            data: this.commentValue,
            itemStyle: {
              color: '#79f018'
            }
          }],
        textStyle: {
          color: 'white'
        }
      };
      chart3.setOption(option)
    },
    visualizeCommunicationTable() {
      const chart4 = echarts.init(document.getElementById("chart4"));
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            crossStyle: {
              color: '#999'
            }
          }
        },
        toolbox: {
          feature: {
            restore: {show: true},
            saveAsImage: {show: true}
          }
        },
        legend: {
          data: ['distribution_bar', 'distribution_line'],
          textStyle: {
            color: 'white'
          }
        },
        xAxis: [
          {
            type: 'category',
            name: '# users',
            data: this.communicationName,
            axisPointer: {
              type: 'shadow'
            },
            textStyle: {
              color: 'white' // 设置轴标签的文本颜色为白色
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: '# problems discussed by x users',
            min: 0,
            max: 500,
            interval: 50,
            textStyle: {
              color: 'white' // 设置轴标签的文本颜色为白色
            }
          },
        ],
        series: [
          {
            name: 'distribution_bar',
            type: 'bar',
            data: this.communicationValue,
            itemStyle: {
              // 设置渐变色
              color: new echarts.graphic.LinearGradient(
                  0, 0, 0, 1,
                  [
                    {offset: 0, color: '#83bff6'},
                    {offset: 0.5, color: '#188df0'},
                    {offset: 1, color: '#188df0'}
                  ]
              )
            },
          },
          {
            name: 'distribution_line',
            type: 'line',
            data: this.communicationValue,
            itemStyle: {
              color: '#f0be18'
            }
          },
        ],
        textStyle: {
          color: 'white'
        }
      };
      chart4.setOption(option)
    },
    showActiveTop3() {
      const chart = echarts.init(document.getElementById('chart5'))
      const option = {
        yAxis: {
          type: 'value',
          splitLine: {
            show: false,
          },
          axisLabel: {
            show: false,
          },
        },
        xAxis: {
          type: 'category',
          data: this.topActiveUser,
          splitLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
          axisLabel: {
            show: false,
          },
        },
        series: [{
          name: '排行榜',
          barWidth: 120,
          type: 'bar',
          data: [1.5, 2, 1],
          label: {
            show: true,
            position: 'top',
            formatter: '{b}',
            textStyle: {
              color: '#fff',
              fontSize: 16,
            },
          },
          itemStyle: {
            color: function (params) {   // 设置柱形图的颜色
              if (params.dataIndex === 1) {
                return '#d84430'
              } else if (params.dataIndex === 0) {
                return '#e36c1a'
              } else {
                return '#e2aa20'
              }
            },
          }
        }],
        textStyle: {
          color: '#fff'
        }
      };
      chart.setOption(option);
    },
  },
  mounted() {
    this.threeTableAPI()
  }
}
</script>

<style scoped>
</style>