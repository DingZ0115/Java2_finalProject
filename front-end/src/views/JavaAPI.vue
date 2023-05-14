<template>
  <div class="javaAPI">
    <div class="show">
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
  </div>
</template>

<script>
import * as echarts from "echarts";

export default {
  name: "JavaAPI",
  data() {
    return {
      information: []
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
  },
  mounted() {
    const _this = this
    this.$api.API.getJavaAPI().then((resp) => {
      console.log(resp)
      _this.information = []
      _this.information = resp.data.data.list
      this.$nextTick(() => {
        this.drawWordCloud()
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
  margin-bottom: 2.5%;
}

.wrapperCard {
  border: 1px solid #dccfcf;
  box-shadow: 0 0 25px #909399;
  border-radius: 20px;
  background-color: rgba(255, 255, 255, 0.1);
}
</style>