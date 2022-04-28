<template>
  <div>
    <a-drawer
      title="知识信息"
      :width="400"
      :visible="visible"
      :closable="true"
      forceRender="true"
      :body-style="{ paddingBottom: '80px' }"
      @close="onClose"
    >
      <a-form :form="form" layout="vertical" hide-required-mark>
        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item label="标题">
              <a-input
                v-decorator="[
                  'title',
                  {
                    rules: [{ required: true, message:'请输入知识标题' }],
                  },
                ]"
                placeholder="请输入标题"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item label="标签">
              <a-select
                mode="multiple"
                placeholder="选择知识标签"
                @change="onChangeTags"
                v-decorator="[
                      'tags',{
                        rules: [{ required: false, message: '请选择知识标签' }],
                      },
                    ]"
              >
                <a-select-option v-for="item in this.tagList" :key="item.id" :value="item.id">{{ item.tag_name }}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item label="内容">
              <a-textarea
                v-decorator="[
                  'content',
                  {
                    rules: [{ required: true, message: '请输入知识内容' }],
                  },
                ]"
                :rows="6"
                placeholder="请输入知识内容"
              />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
      <div
        :style="{
          position: 'absolute',
          right: 0,
          bottom: 0,
          width: '100%',
          borderTop: '1px solid #e9e9e9',
          padding: '10px 16px',
          background: '#fff',
          textAlign: 'right',
          zIndex: 1,
        }"
      >
        <a-button :style="{ marginRight: '8px' }" v-if=this.showDelete type="danger" @click="doDeleteKnowledge">
          删除此知识
        </a-button>
        <a-button type="primary" @click="doModifyKnowledge">
          修改并提交
        </a-button>
      </div>
    </a-drawer>
  </div>
</template>

<script>
import { mapGetters, mapActions, mapMutations } from "vuex";
import{message}from 'ant-design-vue';
import moment from "moment";

export default {
  name:"knowledgeShow",
  data() {
    return {
      knowledgeId:-10,
      commentText: "",
      form: this.$form.createForm(this),
      visible: false,
      modifyInfo: {
        title: "",
        content:"",
        tags: [],
      },
      oldtags:[],
      commentId:0,
      showDelete:true,
    };
  },
  computed: {
    ...mapGetters(["currentKnowledgeInfo","userIsMarket","currentKnowledgeId","userInfo","tagList"]),
    filteredTags() {
      return this.$store.getters.tagList.filter(o => !this.modifyInfo.tags.includes(o));
    },
  },
  watch: {
    currentKnowledgeId() {
      console.warn(this.currentKnowledgeId);
      console.warn(this.currentKnowledgeInfo);
    },
  },
  methods: {
    ...mapMutations(["set_currentKnowledgeId"]),
    ...mapActions(["getAllTags","getKnowledgeById","deleteKnowledge","modifyKnowledge","modifyKnowledgeTags","correctKnowledge"]),
    async showDrawer(data) {
      this.knowledgeId=data.knowledgeId;
      this.visible = true;
      await this.set_currentKnowledgeId(data.knowledgeId);
      await this.getKnowledgeById(this.knowledgeId).then(()=>{
        this.oldtags=this.currentKnowledgeInfo.tags.map(e => e.id);
        this.modifyInfo.tags=this.oldtags;
        this.$nextTick(()=>{
          this.form.setFieldsValue({'content': this.currentKnowledgeInfo.content, 'title':this.currentKnowledgeInfo.title,'tags':this.modifyInfo.tags });
        })
      });
      this.commentId=data.commentId;
      this.showDelete=(!data.fromMes);
    },
    onChangeTags(selectedItems) {
      this.modifyInfo.tags = selectedItems;
    },
    onClose() {
      this.visible = false;
    },
    beforeCreate() {
      // 表单名默认为“form”
      this.form = this.$form.createForm(this, { title: "" });
    },
    resetValues(){
      this.resetValues();
      this.form.resetFields();
    },
    async doModifyKnowledge(e) {
      console.log(e);
      e.preventDefault();
      this.form.validateFieldsAndScroll(async (err, values) => {
        if (!err) {
          const data = {
            title: this.form.getFieldValue("title"),
            content: this.form.getFieldValue("content"),
            id:this.knowledgeId,
          };
          if(this.showDelete){
            console.log("知识修改")
            await this.modifyKnowledge(data);
          }
          else{
            console.log("采编根据评论修改")
            console.log(data)
            console.log(this.commentId)
            let date=new Date();
            await this.correctKnowledge({commentId:this.commentId,modifyTime:moment(date).format('YYYY-MM-DD:HH:MM:SS'),...data});
          }
          console.log("修改数据")
          console.log(data);
          let add_tag=this.modifyInfo.tags.filter(o => !this.oldtags.includes(o));
          let delete_tag=this.oldtags.filter(o=> !this.modifyInfo.tags.includes(o));
          if(add_tag.length !== 0 || delete_tag.length !== 0){
            const data2={
              add_tags:add_tag,
              delete_tags:delete_tag,
              knowledge_id:this.knowledgeId,
            }
            await this.modifyKnowledgeTags(data2);
            console.log("修改该知识标签")
            console.log(this.oldtags)
            console.log(data2);
          }
          this.visible=false;
        }else{
          message.error("请输入标题和内容")
        }
      });
    },
    async doDeleteKnowledge(){
      const that=this;
      this.$confirm({
        title: '你要删除这个知识吗',
        content: h => <div style="color:red;">删除后将不复存在</div>,
        onOk:async() => {
          await that.deleteKnowledge(that.knowledgeId)
          console.log('deleteOK');
          console.log(that.knowledgeId);
          await that.$parent.getKnowledgeList();
          that.visible=false;
        },
        onCancel() {
          console.log('Cancel');
        },
        class: 'test',
      });
    }
  }
};

</script>

<style>

</style>