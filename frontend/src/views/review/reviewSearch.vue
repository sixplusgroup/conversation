<template>
  <div class="reviewHome">
<!-- 高级搜索 -->
    <div class="search-info">
      <a-row type="flex" justify="space-between" align="middle">
        <a-col :span="2">
          <span>员工：</span>
        </a-col>
        <!-- todo -->
        <a-col :span="6">
          <a-select
            placeholder="选择客服"
            :value="searchInfo.staffId === '' ? undefined : searchInfo.staffId"
            @change="onChangeStaff"
          >
            <a-select-option v-for="item in this.staff" :key="item.waiterId" :value="item.waiterId">{{ item.waiterName }}</a-select-option>
          </a-select>
        </a-col>
        <a-col :span="2">
          <span v-if="this.showCustomer">客户：</span>
        </a-col>
        <!-- todo -->
        <a-col :span="6">
          <a-select
            placeholder="选择客户"
            :value="searchInfo.customerId === '' ? undefined : searchInfo.customerId"
            @change="onChangeCustomer"
            v-if="this.showCustomer"
            show-search
            option-filter-prop="children"
            :filter-option="filterOption"
          >
            <a-select-option v-for="item in this.customer" :key="item.userId" :value="item.userId">{{ item.userName }}</a-select-option>
          </a-select>
        </a-col>
        <a-col :span="8">
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
          :key="item.session_id"
          @click.native="jumpToDetails(item.session_id)"
        ></reviewCard>
      </div>
    </a-spin>
<!-- 分页 -->
    <a-pagination 
      :current="currentPage" 
      :total="this.totalNumReviewSearch" 
      :page-size="3"
      :show-total="total => `共 ${total} 条`"
      v-show="showPage"
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
      searchInfo:{
        staffId: '',
        customerId:'',
      },
      currentPage:1,
      showCustomer:false,
      showPage:false,
    };
  },
  async mounted(){
    await this.getAllStaff();
  },
  computed:{
    ...mapGetters(["staff", "customer","userIsManager","userInfo","currentReviewId","reviewListLoading","reviewList","totalNumReviewSearch","totalNumReviewSearch"]),
  },
  methods: {
    ...mapMutations([ "set_reviewListLoading","set_currentReviewId","set_reviewList"]),
    ...mapActions([
      "getrecommendReviewList",
      "searchReview",
      "getAllCustomer",
      "getAllStaff",
      "getReviewDetail"
    ]),
    // todo 搜索时间
    async onChangeCustomer(selectedItems) {
      this.searchInfo.customerId = selectedItems;
      const cid = parseInt(this.searchInfo.customerId)
      const sid = parseInt(this.searchInfo.staffId)
      this.set_reviewListLoading(true);
      console.log("loading变成true")
      this.set_reviewList([]);
      await this.searchReview({customerId:cid,staffId:sid,pageNum:1,pageSize:3}).then(()=>{
        this.set_reviewListLoading(false);
        console.log("loading变成false")
        this.showPage=true;
      })

    },
    async onChangeStaff(selectedItems) {
      this.searchInfo.staffId = selectedItems;
      await this.getAllCustomer(selectedItems).then(()=>{
        this.showCustomer = true;
      })
    },
    async onChangePage(current) {
      this.currentPage = current;
      console.log(this.currentPage)
      this.set_reviewListLoading(true);
      console.log("loading变成true")
      await this.searchReview({...this.searchInfo,pageNo:current,pageSize:3}).then(()=>{
        this.set_reviewListLoading(false);
        console.log("loading变成false")
      })
    },
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
    handleBlur() {
      console.log('blur');
    },
    handleFocus() {
      console.log('focus');
    },
    filterOption(input, option) {
      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      );
    },
  }
}
</script>

<style scoped lang="less">
.reviewHome {
  text-align: center;
  padding-left: 20px;
  padding-right: 20px;
  padding-top: 10px;
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
  .search-result {
    height: 90%;
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