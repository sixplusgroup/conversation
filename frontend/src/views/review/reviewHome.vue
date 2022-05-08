<template>
  <div class="reviewHome">
<!-- 列表展示 -->
    <a-spin class="recommendList" :spinning="reviewListLoading">
      <div class="card-wrapper">
        <reviewCard
          :review="item"
          v-for="item in this.reviewList"
          :key="item.session_id"
          @click.native="jumpToDetails(item.session_id)"
        ></reviewCard>
      </div>
    </a-spin>
    <a-pagination 
      style="margin-top:15px"
      :current="currentPage" 
      :total="this.totalNumReviewRecommend" 
      :page-size="3"
      :show-total="total => `共 ${total} 条`"
      @change="onChangePage" />
  </div>
</template>

<script>
import { mapGetters, mapActions, mapMutations } from "vuex";
import reviewCard from "@/views/review/component/reviewCard";
export default {
  name:"reviewHome",
  components:{
    reviewCard,
  },
  data(){
    return{
      currentPage:1,
    };
  },
  async mounted(){
    this.set_reviewListLoading(true);
    console.log("loading变成true")
    this.set_reviewList([]);
    await this.getRecommendReviewList({pageNum:1,pageSize:3});
  },
  computed:{
    ...mapGetters(["reviewList", "reviewListLoading","userIsMarket","userIsManager","userInfo","currentReviewId","totalNumReviewRecommend","reviewDetail"]),
  },
  methods: {
    ...mapMutations([ "set_reviewListLoading","set_currentReviewId","set_reviewList"]),
    ...mapActions([
      "getRecommendReviewList",
      "searchReview",
      "getReviewDetail"
    ]),
    jumpToDetails(id) {
      //编程式导航，向 history 栈添加一个新的记录，当用户点击浏览器后退按钮时，则回到之前的 URL
      console.log(id)
      this.set_currentReviewId(id)
      console.log(this.currentReviewId)
      this.getReviewDetail(this.currentReviewId).then(()=>{
        console.warn(this.reviewDetail)
        this.$router.push({ name: "reviewDetail", params: { rid: id } });
      })
    },
    async onChangePage(current) {
      this.currentPage = current;
      console.log(this.currentPage)
      this.set_reviewListLoading(true);
      this.set_reviewList([]);
      console.log("loading变成true")
      await this.getRecommendReviewList({pageNum:current,pageSize:3}).then(()=>{
        this.set_reviewListLoading(false);
        console.log("loading变成false")

      });
    },
  }
}
</script>

<style scoped lang="less">
.reviewHome {
  text-align: center;
  padding-left: 20px;
  padding-right: 20px;
  padding-top: 20px;
  display: flex;
  flex-direction: column;

  .recommendList {
    display: flex;
    flex-direction: column;
    margin-left:0.4%;

    .ant-spin-container {
      display: flex;
      flex-direction: column;
      .emptyBox {
        height: 0;
        margin: 10px 10px;
      }
      .card-wrapper {
        display: flex;
        justify-content: flex-start;
        flex-wrap: wrap;
        flex-grow: 3;
      }
      .card-wrapper .card-item {
        margin: 30px;
        position: relative;
        height: 188px;
      }
    }
  }
}
</style>