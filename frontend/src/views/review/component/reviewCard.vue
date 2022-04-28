<template>
  <div hoverable class="reviewCard" >
    <a-row type="flex" justify="space-between" align="middle">
      <a-col :span="1.5">
        <p>产品id:{{(review.pid)}}</p>
        <p>用户id:{{(review.uid)}}</p>
      </a-col>
      <a-col :span="5.5">
        <p>开始时间:{{(review.startTime)}}</p>
        <p>结束时间:{{(review.endTime)}}</p>
      </a-col>
      <a-col :span="2">
        <span style="font-size:20px;font-weight:bold">{{(review.rate)}}</span>
      </a-col>
    </a-row>
  </div>

</template>
<script>
export default {
  name: "",
  props: {
    review: {}
  },
  data() {
    return {};
  },
  computed:{
  },
  methods:{
    
  }
};
</script>

<style lang="less">
.reviewCard {
  margin-bottom:5px;
  margin-left: 3%;
  width: 95%;
  height: 70px;
  border-radius: 15px;
  padding-left:20px;
  padding-right:15px;
  padding-top:25px;
  background-color: rgb(255, 255, 255);
  
}

.reviewCard:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.4);
}
</style>