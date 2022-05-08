<template>
  <a-modal
      title="添加新知识"
      :visible="addKnowledge_Visible"
      cancelText="取消"
      okText="确定"
      @cancel="cancel"
      @ok="handleSubmit"
    >
      <!-- #点确定的响应⽅法handleSubmit -->
      <a-form :form="form" style="margin-top: 15px" v-bind="formItemLayout">
        <a-form-item label="标题" v-bind="formItemLayout">
          <a-input
            placeholder="请填写标题"
            v-decorator="['title', { rules: [{ required: true, message:'请输入标题' }] }]"
          ></a-input>
        </a-form-item>
        <a-form-item label="知识内容" v-bind="formItemLayout">
          <a-input
            type="textarea"
            :rows="4"
            placeholder="请填写知识内容"
            v-decorator="['content', { rules: [{ required: true,message: '请填写知识内容' }] }]"
          ></a-input>
        </a-form-item>
      </a-form>
    </a-modal>
</template>
<script>
import { mapActions,mapGetters } from "vuex";
import{message}from 'ant-design-vue';

export default {
  name: "addKnowledgeCard",
  components: {
  },
  // props: ['addKnowledge_Visible'],
  data() {
    return {
      addKnowledge_Visible:false,
      formItemLayout: {
        labelCol: {
          xs: { span: 12 },
          sm: { span: 6 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        }
      },
      form: this.$form.createForm(this),
    };
  },
  async mounted() {

  },
  computed: {
    ...mapGetters([]),

  },
  methods: {
    ...mapActions([
      "createKnowledge",
    ]),
// 添加新知识
    add() {
      this.addKnowledge_Visible = true;
    },
    beforeCreate() {
      // 表单名默认为“form”
      this.form = this.$form.createForm(this, { title: "addKnowledge" });
    },
    resetValues(){
        this.form.resetFields();
      // this.couponType=0;
    },
    cancel() {
      this.resetValues();
      this.addKnowledge_Visible=false;
    },
    async handleSubmit(e) {
      console.log(e);
      e.preventDefault();
      this.form.validateFields(async (err, values) => {
        if (!err) {
          const data = {
            title: this.form.getFieldValue("title"),
            content: this.form.getFieldValue("content"),
          };
          await this.createKnowledge(data);
          console.log(data);
          this.resetValues();
          this.addKnowledge_Visible=false;
        }else{
          message.error("请输入标题和内容")
        }
      });
    },
  }
};
</script>
<style scoped lang="less">

</style>