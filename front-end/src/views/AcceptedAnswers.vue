<template>
  <div class="acceptedAnswers">
    <div class="show">
      <div style="flex-basis: 47%">
        <el-card>
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
      information: [
        {
          group_name: "唐博",
          fundings: [
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
        },
      ],
    }
  },
  mounted() {
    const _this = this
    this.$api.API.getAcceptedQuestionCount().then((resp) => {
      _this.percentage = resp.data.data.ratio
    }).catch(err => {
      console.log(err);
    });

    this.$nextTick(() => {
      const chart = echarts.init(document.getElementById("chart"));
      const option = {
        title: {
          text: 'The distribution of question resolution time',
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
          data: ['国自然', '中央财政支持地方高校经费', '高水平']
        },
        series: [
          {
            type: 'pie',
            radius: '65%',
            center: ['50%', '50%'],
            selectedMode: 'single',
            data: this.information[0].fundings,
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
  }
}
</script>

<style scoped>
</style>