<template>
  <div class="knowledgeList">
<!-- 高级搜索 -->
    <div class="search-info">
      <a-row type="flex" justify="space-between" align="middle">
        <a-col :span="2">
          <span>知识标签：</span>
        </a-col>
        <a-col :span="6">
          <a-select
            mode="multiple"
            placeholder="选择知识标签"
            :value="searchInfo.tags"
            @change="onChangeTags"
          >
            <a-select-option v-for="item in this.tagList" :key="item.id" :value="item.id">{{ item.tag_name }}</a-select-option>
          </a-select>
        </a-col>
        <a-button :span="2" type="primary" @click="editTag" v-if=userIsMarket>
          <a-icon type="edit" />编辑
        </a-button>
        <a-col :span="2">
          <span>关键词搜索：</span>
        </a-col>
        <a-col :span="12">
          <div class="search-bar">
            <a-input-search  placeholder="输入搜索关键词，请用空格分隔开" enter-button @search="onSearch" v-model="searchInfo.keywords" />
          </div>
        </a-col>
      </a-row>
      <!-- <a-divider ></a-divider> -->
    </div>
<!-- 编辑和添加 -->
    <div class="editAndAdd">
      <div style="padding-top:3px;width:17%;height:28px">搜索结果：共找到 {{knowledgeList.length}} 条知识</div>
      <a-button style="margin-right:15px;width:13%;height:28px" type="primary" @click="add" v-if=userIsMarket>
          <a-icon type="plus" />添加新知识
      </a-button>
      <div v-if=userIsMarket style="padding-top:3px;width:70%;height:28px;text-align: left">可添加关键词：{{this.key}}</div>
      <!-- <a-divider type="vertical"></a-divider> -->
    </div>
<!-- 搜索结果 -->
    <a-spin class="search-result" :spinning="knowledgeListLoading">
      <div class="card-wrapper">
        <KnowledgeCard
          :knowledge="item"
          v-for="item in this.knowledgeList"
          :key="item.index"
          @click.native="jumpToDetails(item.id)"
        ></KnowledgeCard>
        <div
          v-for="item in emptyBox"
          :key="item.name"
          class="emptyBox ant-col-xs-7 ant-col-lg-5 ant-col-xxl-3"
        ></div>
      </div>
    </a-spin>
<!-- 添加知识 -->
    <AddKnowledgeCard
          ref="addKnowledge"
        ></AddKnowledgeCard>
<!-- 编辑tags -->
    <EditTagsAll
      ref="editTags"
    ></EditTagsAll>
<!-- 普通用户知识展示 -->
    <KnowledgeShowUser
      ref="knowledgeShow"
    ></KnowledgeShowUser>
<!-- 采编人员展示 -->
    <KnowledgeShowMarket
      ref="marketKnowledgeShow"
    ></KnowledgeShowMarket>
<!-- 管理员展示 -->
    <KnowledgeShowManager
      ref="marnagerKnowledgeShow"
    ></KnowledgeShowManager>
  </div>
</template>
<script>
import KnowledgeCard from "@/components/knowledgeCard";
import { mapGetters, mapActions, mapMutations } from "vuex";
import{message}from 'ant-design-vue';
import AddKnowledgeCard from "@/views/market/component/AddKnowledgeCard";
import KnowledgeShowUser from "@/views/user/knowledgeShow";
import EditTagsAll from "@/views/market/component/editTagsAll";
import KnowledgeShowMarket from "@/views/market/knowledgeShow";
import KnowledgeShowManager from "@/views/manager/component/knowledgeShow";
import store from "@/store";
const moment = require("moment");

export default {
  name: "home",
  components: {
    KnowledgeCard,
    AddKnowledgeCard,
    KnowledgeShowUser,
    EditTagsAll,
    KnowledgeShowMarket,
    KnowledgeShowManager,
  },
  data() {
    return {
      emptyBox: [{ name: "box1" }, { name: "box2" }, { name: "box3" }],
      searchInfo: {
        keywords: "",
        tags: [],
      },
      currentKnowledgeId:0,

    };
  },
  async mounted() {
    await this.getAllTags();
    await this.getKnowledgeList();
    await this.getKeywords();
  },
  
  computed: {
    ...mapGetters(["knowledgeList", "knowledgeListLoading","knowledgeTotalNum","userIsMarket","tagList","userIsManager","userInfo","key"]),
    filteredTags() {
      return this.$store.getters.tagList.filter(o => !this.searchInfo.tags.includes(o));
    },
  },
  methods: {
    ...mapMutations([ "set_knowledgeListLoading"]),
    ...mapActions([
      "getKnowledgeList",
      "getAllTags",
      "searchKnowledge",
      "getKeywords",
    ]),
    
// 添加知识
    add() {
      this.$refs.addKnowledge.add();
    },
//搜索知识
    async onSearch(value) {
      this.set_knowledgeListLoading(true);
      console.log("开始搜索")
      await this.searchKnowledge(this.searchInfo);
      this.set_knowledgeListLoading(false);
    },
    onChangeTags(selectedItems) {
      this.searchInfo.tags = selectedItems;
    },
// 跳转详情
    jumpToDetails(id) {
      console.log(id)
      this.currentKnowledgeId=id;
      // //编程式导航，向 history 栈添加一个新的记录，当用户点击浏览器后退按钮时，则回到之前的 URL
      // this.$router.push({ name: "knowledgeDetail", params: { knowledgeId: id } });
      if(this.userIsMarket){
        console.log("采编人员的知识展示")
        this.$refs.marketKnowledgeShow.showDrawer({knowledgeId:this.currentKnowledgeId,fromMes:false,commentId:-1});
      }
      else if(this.userIsManager){
        this.$refs.marnagerKnowledgeShow.setModalVisible({knowledgeId:this.currentKnowledgeId});
      }
      else{
        console.log("普通用户的知识展示")
        this.$refs.knowledgeShow.setModalVisible({knowledgeId:this.currentKnowledgeId,fromMes:false});
      }
    },
// 添加标签
    editTag(){
      this.$refs.editTags.editTag();
    },
  }
};
</script>
<style scoped lang="less">
.knowledgeList {
  text-align: center;
  padding: 20px;
  display: flex;
  flex-direction: column;


  .search-info {
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
  .editAndAdd{
    display: flex;
    flex-direction: row-reverse;
    margin-top: 15px;
  }
  .search-result {
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