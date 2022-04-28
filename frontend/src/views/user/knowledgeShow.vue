<template>
  <a-modal
      v-model="modalVisible"
      :title=currentKnowledgeInfo.title
      centered
      @ok="handleSubmitComment"
      :okText="this.okTexts"
      cancelText="关闭"
    >
      <p>{{currentKnowledgeInfo.content}}</p>
    <div style="margin-bottom:30px;width:100%">
      <a-checkable-tag
        :tag="item"
        v-for="item in currentKnowledgeInfo.tags"
        :key="item.id"
        v-model="tagChecked"
        style="float:right"
        >
          {{item.tag_name}}
      </a-checkable-tag>
    </div>
    <a-comment v-if="this.showComment">
      <div slot="author" style=""><span>评价</span></div>
      <a-avatar slot="avatar" shape="square" size="large" :src="userInfo.url" alt="Han Solo" />
      <div slot="content" style="margin-top:10px">
        <a-form-item>
          <a-textarea :rows="2" :value="commentText" @change="handleChange" />
        </a-form-item>
      </div>
    </a-comment>
  </a-modal>
</template>

<script>
import { mapGetters, mapActions, mapMutations } from "vuex";

export default {
  name:"knowledgeShow",
  data() {
    return {
      modalVisible: false,
      tagChecked:true,
      knowledgeId:-10,
      submitting: false,
      commentText: "",
      showComment:true,
      okTexts:"",
    };
  },
  computed: {
    ...mapGetters(["currentKnowledgeInfo","userIsMarket","currentKnowledgeId","userInfo"]),

  },
  async mounted() {
  },
  methods: {
    ...mapMutations(["set_currentKnowledgeId"]),
    ...mapActions(["getKnowledgeById","commentKnowledge"]),
    async setModalVisible(data) {
      this.knowledgeId=data.knowledgeId
      this.modalVisible = true;
      await this.set_currentKnowledgeId(data.knowledgeId);
      await this.getKnowledgeById(this.knowledgeId);
      this.showComment=(!data.fromMes);
      if(data.fromMes){
        this.okTexts="了解了"
      }else{this.okTexts="评论"}
    },
    handleChange(e) {
      this.commentText = e.target.value;
    },
    async handleSubmitComment() {
      if (!this.commentText) {
        this.modalVisible = false;
        return;
      }
      this.submitting = true;
      let typeA=0;
      if(this.userInfo.userType.toUpperCase() !== "MANAGER"){typeA=1}
      await this.commentKnowledge({
        knowledgeId: this.knowledgeId,
        content: this.commentText,
        responserId:this.userInfo.userId,
        type:"使用人员反馈",
      });
      console.log(this.knowledgeId)
      console.log(this.commentText)
      this.submitting = false;
      this.modalVisible = false;
      this.commentText='';

    },
  }
};

</script>

<style>
.ant-row.ant-form-item{
  margin-bottom: 0px;
}
.ant-comment-inner{
  padding-bottom:0px;
  padding-top:10px;
  width:100%
}
</style>