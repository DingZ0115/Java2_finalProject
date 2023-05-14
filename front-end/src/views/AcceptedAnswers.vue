<template>
  <div class="acceptedAnswers">
    <div class="show">
      <div style="flex-basis: 47%">
        <el-card class="showCard">
          <div class="text">
            <i class="el-icon-coordinate"></i>
            percentage of questions have accepted answers: {{ percentage }}
          </div>
          <div class="text">
            <i class="el-icon-coordinate"></i>
            percentage of questions have accepted answers that have received
          </div>
          <div class="text">&nbsp &nbsp more upvotes than the accepted answers: {{ moreUpvote }}
          </div>
        </el-card>
      </div>
      <div style="flex-basis: 50%">
        <div id="chart" style="height:400px;"></div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";

export default {
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
            position: 'top'
          },
          xAxis: {
            textStyle: {
              color: 'white'
            },
            name: '分钟',
          },
          yAxis: {
            textStyle: {
              color: 'white'
            },
            name: '秒',
          },
          series: [{
            type: 'scatter',
            symbolSize: 9,
            data: _this.chartData,
            itemStyle: {
              color: function (params) {   // 设置柱形图的颜色
                return '#ffd500'
              },
            }
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
.showCard {
  border: 1px solid #dccfcf;
  box-shadow: 0 0 25px #909399;
  border-radius: 20px;
  background-color: rgba(255, 255, 255, 0.75);
}
</style>