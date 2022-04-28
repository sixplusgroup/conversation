<template>
  <div class='login-back' :style="'background: #f0f2f5 url('+backgroundImgOnShow+') no-repeat ;'">
    <div class="main">
    <div class="top">
      <div class="header">
        <img src="../assets/favicon.svg" class="logo" alt="logo" />
        <div>  
          <span class="title">客服工单系统</span><br/><span class="title" style="font-size:35px">GMair</span>
        </div>
      </div>
      <div class="desc"></div>
    </div>
    <a-form id="formLogin" class="user-layout-login" ref="formLogin" :form="form">
          <a-form-item>
            <a-input
            class="login-email"
              size="large"
              type="text"
              placeholder="账号"
              v-decorator="[
                'username',
                {rules: [{ required: true, message: '请输入账号' }], validateTrigger: 'blur'}
              ]"
            >
              <a-icon slot="suffix" type="user" :style="{ color: 'rgba(0,0,0,.25)' }" />
            </a-input>
          </a-form-item>

          <a-form-item>
            <a-input
            class="login-password"
              size="large"
              type="password"
              autocomplete="false"
              placeholder="密码"
              v-decorator="[
                'password',
                {rules: [{ required: true, message: '请输入密码' }], validateTrigger: 'blur'}
              ]"
            >
              <a-icon slot="suffix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }" />
            </a-input>
          </a-form-item>
          <a-form-item style="margin-top:24px">
            <a-button
              size="large"
              type="primary"
              class="login-button"
              :loading="loginLoading"
              @click="handlelogin()"
            >确定</a-button>
          </a-form-item>
    </a-form>
  </div>
  </div>
</template>

<script>
import { mapGetters, mapActions, mapMutations } from "vuex";

export default {
  name: "login",
  components: {},
  data() {
    return {
      loginLoading: false,
      form: this.$form.createForm(this),
      backgroundImgOnShow:'https://s1.ax1x.com/2020/06/30/NoF9Ff.png',//缩略图
    };
  },
  computed: {

  },
  mounted() {
    let that=this
    const img = new Image();
    img.src = 'https://s1.ax1x.com/2020/05/28/tZrLxH.png';//原图
    img.onload = function() {
        that.backgroundImgOnShow = this.src;
    }
  },
  // watch: {
  //   // $route: {
  //   //   handler: function(route) {
  //   //     this.redirect = route.query && route.query.redirect;
  //   //   },
  //   //   immediate: true
  //   // }
  // },
  methods: {
    ...mapActions(["login"]),

    
    handlelogin() {
      const validateFieldsKey =
        this.customActiveKey = ["username", "password"];
      this.form.validateFields(
        validateFieldsKey,
        { force: true },
        async (err, values) => {
          if (!err) {
            this.loginLoading = true;
            const data = {
              username: this.form.getFieldValue("username"),
              password: this.form.getFieldValue("password"),
              scope:"resource:read",
              grant_type:"password",
              client_id:"client1",
              client_secret:"123456",
            };
            console.log("开始登录");
            console.log('发送数据： ')
            console.log(data)
            await this.login(data);

            this.loginLoading = false;
          }
        }
      );
    },

  }
};
</script>


<style lang="less" scoped>
.login-back{
  width: 100%;
  height: 100%;
  padding-top: 1px;
  
}
.main {
  min-width: 350px;
  width: 418px;
  padding: 15px 40px;
  margin: 150px  auto;
  margin-bottom: 0px;
  background: rgba(37, 37, 37, 0.5);
  border-radius: 25px;
  .top {
    text-align: center;

    .header {
      margin-top: 20px;
      height: 80px;
      line-height: 44px;
      display: flex;
      justify-content: center;
      align-items: center;
      .badge {
        position: absolute;
        display: inline-block;
        line-height: 1;
        vertical-align: middle;
        margin-left: -12px;
        margin-top: -10px;
        opacity: 0.8;
      }

      .logo {
        height: 80px;
        vertical-align: top;
        margin-right: 16px;
        border-style: none;
      }

      .title {
        font-size: 28px;
        color: rgba(236, 236, 236, 0.85);
        font-family: Avenir, "Helvetica Neue", Arial, Helvetica, sans-serif;
        font-weight: 600;
        position: relative;
        top: 2px;
      }
    }
    .desc {
      font-size: 14px;
      color: rgba(0, 0, 0, 0.45);
      margin-top: 12px;
      margin-bottom: 40px;
    }
  }
}
.user-layout-login {
  label {
    font-size: 14px;
  }

  .getCaptcha {
    display: block;
    width: 100%;
    height: 40px;
  }

  .forge-password {
    font-size: 14px;
  }

  button.login-button {
    padding: 0 15px;
    font-size: 16px;
    height: 40px;
    width: 100%;
  }

  .user-login-other {
    text-align: left;
    margin-top: 24px;
    line-height: 22px;

    .item-icon {
      font-size: 24px;
      color: rgba(0, 0, 0, 0.2);
      margin-left: 16px;
      vertical-align: middle;
      cursor: pointer;
      transition: color 0.3s;

      &:hover {
        color: #1890ff;
      }
    }

    .register {
      float: right;
    }
  }
}
</style>


