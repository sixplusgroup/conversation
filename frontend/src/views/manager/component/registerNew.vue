<template>
  <a-modal
    v-model="visible"
    title="新用户注册"
    centered
    @ok="handleSubmit"
    okText="注册"
    cancelText="取消"
  >
    <a-form :form="form" layout="vertical" hide-required-mark>
            <a-form-item label="账号">
              <a-input
                v-decorator="[
                  'name',
                  {
                    rules: [{ required: true, message:'请输入新用户账号' }],
                  },
                ]"
                placeholder="请输入账号"
              />
            </a-form-item>
            <a-form-item label="密码">
              <a-input
                v-decorator="[
                  'pwd',
                  {
                    rules: [{ required: true, message:'请输入新用户密码',trigger: 'blur' }],
                  },
                ]"
                placeholder="请输入密码"
              />
            </a-form-item>
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
  computed: {
    ...mapGetters([]),
  },
  async mounted() {


  },
  methods: {
    ...mapMutations([]),
    ...mapActions(["register"]),
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
      this.form = this.$form.createForm(this, { name:"",pwd:"" });
    },
    resetValues(){
      this.resetValues();
      this.form.resetFields();
    },
    handleSubmit() {
      this.form.validateFieldsAndScroll(async (err, values) => {
        if (!err) {
          const data = {
            name: this.form.getFieldValue("name"),
            pwd: this.form.getFieldValue("pwd"),
          };
          console.log("注册")
          console.log(data);
          await this.register(data).then(()=>{
            this.visible=false;
          })
        }else{
          message.error("请输入用户名和密码")
        }
      });
    },

  }
};

</script>

<style>

</style>