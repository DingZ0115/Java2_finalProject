<template>
  <div class="users">
    <div style="width: 60%;margin: auto">
      <div class="title" style="margin-top: 5%;">
        <i class="el-icon-circle-check"></i>
        The distribution of users who post the question in a thread
      </div>
      <div id="chart1" style="height:350px;"></div>
      <div class="title">
        <i class="el-icon-circle-check"></i>
        The distribution of users who answers in a thread
      </div>
      <div id="chart2" style="height:350px;"></div>
      <div class="title">
        <i class="el-icon-circle-check"></i>
        The distribution of users who comments in a thread
      </div>
      <div id="chart3" style="height:350px;"></div>
    </div>
    <div style="margin-bottom: 5%;">
      <el-card style="text-align: left;width: 60%;margin: auto;margin-bottom: 2%"
               :body-style="{ display: 'flex', flexWrap: 'wrap',textAlign: 'left' }">
        <div style="flex-basis: 60%;margin-left: 5%">
          <div v-for="x in 5">
            <div class="text">
              <i class="el-icon-s-flag other"></i>
              {{ x }}. maximum number of answers
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
import * as echarts from "echarts";

export default {
  name: "Users",
  data() {
    return {
      percentage: 5,
      average: 9,
      maximum: 8,
      information: [2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3],
    }
  },
  mounted() {
    const chart1 = echarts.init(document.getElementById("chart1"));
    const chart2 = echarts.init(document.getElementById("chart2"));
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
        data: ['distribution_bar', 'distribution_line']
      },
      xAxis: [
        {
          type: 'category',
          name: '# answers',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
          axisPointer: {
            type: 'shadow'
          }
        }
      ],
      yAxis: [
        {
          type: 'value',
          name: '# questions',
          min: 0,
          max: 250,
          interval: 50,
          axisLabel: {
            formatter: '{value} ml'
          }
        },
      ],
      series: [
        {
          name: 'distribution_bar',
          type: 'bar',
          tooltip: {
            valueFormatter: function (value) {
              return value + ' ml';
            }
          },
          data: this.information
        },
        {
          name: 'distribution_line',
          type: 'line',
          data: this.information
        }
      ]
    };
    chart1.setOption(option)
    chart2.setOption(option)
    chart3.setOption(option)
  }
}
</script>

<style scoped>

</style>