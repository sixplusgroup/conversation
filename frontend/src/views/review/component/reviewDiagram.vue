<template>
  <div id="diagram">
    <div id="detail" style="width: 580px;height:350px;"></div>
    <div class="info">
      <a-descriptions
        title="复盘详细信息"
      >
        <a-descriptions-item label="客户名">
          {{this.reviewDetail.cname}}
        </a-descriptions-item>
        <a-descriptions-item label="产品ID">
          {{this.reviewDetail.pid}}
        </a-descriptions-item>
        <a-descriptions-item label="客户整体情绪值">
          {{this.averageEmo}}
        </a-descriptions-item>
        <a-descriptions-item label="客户极负面情绪次数" :span="1.5">
          {{this.reviewDetail.customerExtremeNegativeCount}}
        </a-descriptions-item>
        <a-descriptions-item label="客服负面情绪次数" :span="1.5">
          {{this.reviewDetail.waiterNegativeCount}}
        </a-descriptions-item>
      </a-descriptions>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions, mapMutations } from "vuex";

export default {
  name:"reviewDiagram",
  computed:{
    ...mapGetters(["userIsManager","userInfo","currentReviewId","reviewDetail"]),
    averageEmo:function(){
      return Number(this.reviewDetail.customerAverageScore).toFixed(2)
    },
    clientEmo: function() {
      let cEmo = [];
      for(let i in this.reviewDetail.sentigraph){
        if(!this.reviewDetail.sentigraph[i].waiterSend){
          cEmo.push(this.reviewDetail.sentigraph[i].emoRate)
        }else{
          cEmo.push('')
        }
      }
      // console.log(this.reviewDetail.session_id)
      return cEmo;
    },
    staffEmo: function() {
      let sEmo = [];
      for(let i in this.reviewDetail.sentigraph){
        if(this.reviewDetail.sentigraph[i].waiterSend){
          sEmo.push(this.reviewDetail.sentigraph[i].emoRate)
        }else{
          sEmo.push('')
        }
      }
      return sEmo;
    },
    time:function(){
      let t=[];
      for(let i in this.reviewDetail.sentigraph){
        t.push(this.reviewDetail.sentigraph[i].time)
      }
      return t;
    }
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
      let myChart = this.$echarts.init(document.getElementById("detail"));
      // 指定图表的配置项和数据
      let option = {
        title: {
            text: ''
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['客服情绪', '客户情绪'],
            padding:[15,0,0,100],
        },
        grid: {
            left: '2%',
            right: '4%',
            bottom: '3%',
            top:'5%',
            containLabel: true
        },
        toolbox: {
            feature: {
                // saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            show:false,
            data: this.time
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name: '客服情绪',
                type: 'line',
                data: this.staffEmo,
                connectNulls:true,
                smooth: true,
            },
            {
                name: '客户情绪',
                type: 'line',
                data: this.clientEmo,
                connectNulls:true,
                smooth: true,
            },
        ]
      };

      // 使用刚指定的配置项和数据显示图表。
      myChart.setOption(option);
    }
  },
  mounted() {
    this.drawChart();
  }
};
</script>

<style scoped lang="less">
#diagram {
  height: 100%;
  margin-left: 3%;
  width: 45%;
  padding-right: 20px;
  border-right-color:rgba(213, 214, 215,75%);
  border-right-style:solid;
  border-right-width: 1px;
}
.info{
  -moz-box-shadow: inset 0 0 10px #CCC;
  -webkit-box-shadow: inset 0 0 10px #CCC;
  box-shadow: inset 0 0 10px #CCC;
  height: 170px;
  border-radius: 15px;
  padding: 30px 0px 5px 29px;
  background-color: #f7f7f7;
}
</style>