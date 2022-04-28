<template>
  <a-modal
    v-model="visible"
    title="新用户注册"
    centered
    @ok="loginReview"
    okText="进入"
    cancelText="取消"
  >
    <a-form :form="form" layout="vertical" hide-required-mark>
        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item label="密码">
              <a-input
                v-decorator="[
                  'password',
                  {
                    rules: [{ required: true, message:'请输入复盘密码' }],
                  },
                ]"
                placeholder="请输入复盘密码"
              />
            </a-form-item>
          </a-col>
        </a-row>
    </a-form>
  </a-modal>
</template>

<script>
import { mapGetters, mapActions, mapMutations } from "vuex";
import{message}from 'ant-design-vue';

export default {
  name:"knowledgeShow",
  data() {
    return {
      form: this.$form.createForm(this),
      visible: false,
    };
  },
  // todo 写接口
  computed: {
    ...mapGetters([]),
  },
  async mounted() {


  },
  methods: {
    ...mapMutations([]),
    ...mapActions([]),
    showDrawer() {
      this.visible = true;
    },
    onChangeTags(selectedItems) {
      this.modifyInfo.tags = selectedItems;
    },
    onClose() {
      this.visible = false;
    },
    beforeCreate() {
      // 表单名默认为“form”
      this.form = this.$form.createForm(this, {});
    },
    resetValues(){
      this.resetValues();
      this.form.resetFields();
    },
    async loginReview() {
      this.form.validateFieldsAndScroll(async (err, values) => {
        if (!err) {
          const data = {
            password: this.form.getFieldValue("password"),
          };
          console.log("进入复盘")
          console.log(data);
          this.visible=false;
        }else{
          message.error("请输入复盘密码")
        }
      });
    },

  }
};

</script>

<style>

</style>