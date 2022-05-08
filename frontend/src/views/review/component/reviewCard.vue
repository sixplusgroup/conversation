<template>
    <a-card hoverable class="reviewCard">
    <div :id="review.session_id" style="width: 300px;height:300px;"></div>
    <a-card-meta :title="review.rate">
      <template slot="description">
        产品：{{review.pid}}
      </template>
    </a-card-meta>
  </a-card>
</template>
<script>

export default {
  name: "",
  components: {
  },
  props: {
    review: {}
  },
  data() {
    return {
    };
  },
  computed:{
    clientEmo: function() {
      let cEmo = [];
      for(let i in this.review.sentigraph){
        if(!this.review.sentigraph[i].waiterSend){
          cEmo.push(this.review.sentigraph[i].emoRate)
        }else{
          cEmo.push('')
        }
      }
      console.log(this.review.session_id)
      return cEmo;
    },
    staffEmo: function() {
      let sEmo = [];
      for(let i in this.review.sentigraph){
        if(this.review.sentigraph[i].waiterSend){
          sEmo.push(this.review.sentigraph[i].emoRate)
        }else{
          sEmo.push('')
        }
      }
      return sEmo;
    },
    time:function(){
      let t=[];
      for(let i in this.review.sentigraph){
        t.push(this.review.sentigraph[i].time)
      }
      return t;
    }
  },
  async mounted() {
      this.drawChart();
  },
  methods:{
    drawChart() {
      // 基于准备好的dom，初始化echarts实例【这里存在一个问题，请看到最后】
      let myChart = this.$echarts.init(document.getElementById(this.review.session_id));
      // 指定图表的配置项和数据
      let option = {
        title: {
            text: ''
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['客服情绪', '客户情绪']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
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
  }
};
</script>

<style lang="less">
.reviewCard {
  margin:  1.1%;
  width: 31%;
  // max-height: 350px;
  // display: flex;
  border-radius: 15px;
  padding:15px;
  .ant-card-body{
    padding-left: 0px;
    padding-right: 0px;
    .ant-card-meta-title{
    width: 100%;
    border-bottom-style:solid;
    border-bottom-color:rgb(188, 186, 186);
    border-bottom-width: 0.5px;
    height: 30%;
    }
    .ant-card-meta-description{
      width: 100%;
      height: 70%;
    }
  }
  
}
.reviewCard:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.4);
}
</style>