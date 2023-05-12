<template>
  <div class="numberOfAnswers">
    <div class="show">
      <div style="flex-basis: 47%">
        <el-card>
          <div class="text">
            <i class="el-icon-bell"></i>
            percentage of questions don't have any answer: {{ percentage }}
          </div>
          <div class="text">
            <i class="el-icon-bell"></i>
            average number of answers: {{ average }}
          </div>
          <div class="text">
            <i class="el-icon-bell"></i>
            maximum number of answers: {{ maximum }}
          </div>
        </el-card>
      </div>
      <div style="flex-basis: 50%">
        <div id="chart" style="height:400px;"></div>
        <span>有0-4个answer的question有多少个</span>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";

export default {
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
              color: '#0c56ff'
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
            data: _this.names
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
</style>