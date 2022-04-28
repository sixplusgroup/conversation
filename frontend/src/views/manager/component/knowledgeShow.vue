<template>
  <a-modal
      v-model="modalVisible"
      :title=currentKnowledgeInfo.title
      centered
      @ok="handleSubmitComment"
      okText="了解了"
      cancelText="关闭"
    >
      <p>{{currentKnowledgeInfo.content}}</p>
    <div style="margin-bottom:30px;width:100%">
      <a-checkable-tag
        :tag="item"
        v-for="item in this.currentKnowledgeInfo.tags"
        :key="item.id"
        v-model="tagChecked"
        style="float:right"
        >
          {{item.tag_name}}
      </a-checkable-tag>
    </div>
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
    },
    handleChange(e) {
      this.commentText = e.target.value;
    },
    handleSubmitComment() {
      this.modalVisible = false;
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