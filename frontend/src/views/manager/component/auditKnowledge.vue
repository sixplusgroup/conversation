<template>
  <a-modal
      v-model="modalVisible"
      :title=currentKnowledgeInfo.title
      centered
      @ok="handleSubmitComment"
      okText="未通过"
      cancelText="关闭"
    >
      <p>{{currentKnowledgeInfo.content}}</p>
      <div style="margin-bottom:15px;width:100%">
      <a-checkable-tag
        :tag="item"
        v-for="item in this.currentKnowledgeInfo.tags"
        :key="item.id"
        v-model="tagChecked"
        style=""
        >
          {{item.tag_name}}
      </a-checkable-tag>
    </div>
      <a-button type="primary" size="small" style="margin-left: calc(100% - 48px)" @click="publish">通过</a-button>
    <a-comment >
      <div slot="author" style=""><span>审核理由</span></div>
      <div slot="content" style="margin-top:10px">
        <a-form-item>
          <a-textarea :rows="3" :value="commentText" @change="handleChange" />
        </a-form-item>
      </div>
    </a-comment>
  </a-modal>
</template>

<script>
import { mapGetters, mapActions, mapMutations } from "vuex";
import{message}from 'ant-design-vue';

export default {
  name:"knowledgeShow",
  data() {
    return {
      modalVisible: false,
      tagChecked:true,
      knowledgeId:-10,
      submitting: false,
      commentText: "",
      okTexts:"",
    };
  },
  computed: {
    ...mapGetters(["currentKnowledgeInfo","currentKnowledgeId","userInfo"]),

  },
  async mounted() {
  },
  methods: {
    ...mapMutations(["set_currentKnowledgeId"]),
    ...mapActions(["getKnowledgeById","publishKnowledge","commentKnowledge"]),
    async setModalVisible(data) {
      this.knowledgeId=data.knowledgeId
      await this.set_currentKnowledgeId(data.knowledgeId);
      await this.getKnowledgeById(this.knowledgeId);
      this.modalVisible = true;
    },
    handleChange(e) {
      this.commentText = e.target.value;
    },
    async handleSubmitComment() {
      if (!this.commentText) {
        message.error("请输入审核未通过理由")
        return;
      }
      this.submitting = true;
      await this.commentKnowledge({
        knowledgeId: this.knowledgeId,
        content: this.commentText,
        responserId:this.userInfo.userId,
        type:"审核人员反馈",
      }).then(()=>{
        message.success("知识审核成功")
        console.log(this.knowledgeId)
        console.log(this.commentText)
        this.submitting = false;
        this.modalVisible = false;
        this.commentText='';
      })
      
    },
    async publish(){
      await this.publishKnowledge(this.knowledgeId).then(()=>{
        this.modalVisible = false;
        this.commentText='';
      })
      

    }
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