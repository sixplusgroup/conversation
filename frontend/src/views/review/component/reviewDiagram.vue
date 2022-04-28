<template>
  <div id="diagram">
    <div id="myChart" style="width: 600px;height:400px;"></div>
  </div>
</template>

<script>
import { mapGetters, mapActions, mapMutations } from "vuex";

export default {
  computed:{
    ...mapGetters(["reviewList", "reviewListLoading","userIsManager","userInfo","currentReviewId"]),
  },
  data(){
    return{
      
    };
  },
  methods: {
    ...mapActions([
      "getReviewDetail",
    ]),
    drawChart() {
      // 基于准备好的dom，初始化echarts实例【这里存在一个问题，请看到最后】
      let myChart = this.$echarts.init(document.getElementById("myChart"));
      // 指定图表的配置项和数据
      let option = {
        title: {
            text: '折线图堆叠'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['邮件营销', '联盟广告', '视频广告', '直接访问', '搜索引擎']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name: '邮件营销',
                type: 'line',
                stack: '总量',
                data: [120, 132, 101, 134, 90, 230, 210]
            },
            {
                name: '联盟广告',
                type: 'line',
                stack: '总量',
                data: [220, 182, 191, 234, 290, 330, 310]
            },
            {
                name: '视频广告',
                type: 'line',
                stack: '总量',
                data: [150, 232, 201, 154, 190, 330, 410]
            },
            {
                name: '直接访问',
                type: 'line',
                stack: '总量',
                data: [320, 332, 301, 334, 390, 330, 320]
            },
            {
                name: '搜索引擎',
                type: 'line',
                stack: '总量',
                data: [820, 932, 901, 934, 1290, 1330, 1320]
            }
        ]
      };

      // 使用刚指定的配置项和数据显示图表。
      myChart.setOption(option);
    }
  },
  async mounted() {
    await this.getReviewDetail({
      rid:Number(this.$route.params.rid),
    }).then(()=>{
      this.drawChart();
    })
  }
};
</script>

<style scoped lang="less">
#diagram {
  padding-left: 5%;
  padding-right: 5%;
  padding-top: 2%;
  padding-bottom: 2%;
  height: 100%;
  width: 92%;
  margin-left: 3%;
  margin-right: 3%;
  #myChart{
    width: 900px;
    height: 100%;
  }
}
</style>