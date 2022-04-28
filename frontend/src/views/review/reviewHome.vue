<template>
  <div class="reviewHome">
<!-- 高级搜索 -->
    <div class="search-info">
      <a-row type="flex" justify="space-between" align="middle">
        <a-col :span="2">
          <span>员工：</span>
        </a-col>
        <a-col :span="6">
          <a-select default-value="lucy" style="width:90%" @change="onChangeTags">
            <a-select-option value="jack">
              Jack
            </a-select-option>
            <a-select-option value="lucy">
              Lucy
            </a-select-option>
            <a-select-option value="disabled" disabled>
              Disabled
            </a-select-option>
            <a-select-option value="Yiminghe">
              yiminghe
            </a-select-option>
          </a-select>
          <!-- <a-select
            mode="multiple"
            placeholder="选择员工"
            :value="searchInfo.tags"
            @change="onChangeTags"
          >
            <a-select-option v-for="item in filteredTags" :key="item.id" :value="item.id">{{ item.tag_name }}</a-select-option>
          </a-select> -->
        </a-col>
        <a-col :span="2">
          <span>日期选择：</span>
        </a-col>
        <a-col :span="12">
          <div class="search-bar">
            <a-range-picker style="width:95%" @change="onChangeTime" />
          </div>
        </a-col>
        <a-col>
          <a-button :span="2" type="primary" style="margin-right:7px" @click="onSearch" >
            <a-icon type="search" />搜索
          </a-button>
        </a-col>
      </a-row>
      <!-- <a-divider ></a-divider> -->
    </div>
<!-- 列表展示 -->
    <a-spin class="search-result" :spinning="reviewListLoading">
      <div class="card-wrapper">
        <reviewCard
          :review="item"
          v-for="item in this.reviewList"
          :key="item.index"
          @click.native="jumpToDetails(item.rid)"
        ></reviewCard>
      </div>
    </a-spin>
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
      searchInfo:{
        startTime:"",
        endTime:"",
        tags: [],
      },
    };
  },
  async mounted(){
    await this.getrecommendReviewList();
  },
  computed:{
    ...mapGetters(["reviewList", "reviewListLoading","userIsMarket","userIsManager","userInfo","currentReviewId"]),
  },
  methods: {
    ...mapMutations([ "set_reviewListLoading","set_currentReviewId"]),
    ...mapActions([
      "getrecommendReviewList",
      "searchReview",
    ]),
    // todo 搜索时间
    onChangeTime(date, dateString) {
      console.log(dateString);
      this.searchInfo.startTime=dateString[0];
      this.searchInfo.endTime=dateString[1];
      console.log(this.searchInfo)
    },
    onChangeTags(selectedItems) {
      this.searchInfo.tags = selectedItems;
    },
    async onSearch() {
      this.set_reviewListLoading(true);
      console.log("开始搜索")
      await this.searchKnowledge(this.searchReview).then(()=>{
        this.set_reviewListLoading(false);
      })
    },
    jumpToDetails(id) {
      //编程式导航，向 history 栈添加一个新的记录，当用户点击浏览器后退按钮时，则回到之前的 URL
      this.set_currentReviewId(id)
      console.log(this.set_currentReviewId)
      this.$router.push({ name: "reviewDetailDiagram", params: { rid: id } });
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

  .search-info {
    margin-bottom:15px;
    .ant-select{
      width: 95%;
      margin-right: 5%;
    }
    .search-bar{
      width:100%;
      // margin-right: 2%;
    }
    .ant-divider ant-divider-horizontal{
      margin-bottom: 10px;
      margin-top: 10px;
    }
  }
}
</style>