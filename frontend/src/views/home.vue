<template>
<!-- 左侧导航栏 -->
  <a-layout id="components-layout-demo-side" style="min-height: 100vh">
    <a-layout-sider v-model="collapsed" collapsible :width="150">
      <a-menu theme="dark" :selected-keys="currentRight" mode="inline" style="margin-top:50px">
        <a-menu-item key="1" @click="jumpToKnowledgeList">
          <a-icon type="line-chart" />
          <span>知识库</span>
        </a-menu-item>
        <a-menu-item key="2" @click="jumpToMessageCenter">
          <a-icon type="info-circle" />
          <span>消息中心</span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
    <!-- header -->
    <a-layout>
      <a-layout-header class="header">
        <div class="label">
          <img src="@/assets/favicon.svg" class="logo" alt="logo" />
          <span class="title">GMair 客服工单系统</span>
        </div>
        <a-menu :selected-keys="currentUp" mode="horizontal" theme="light">
          <a-menu-item key="register" @click="jumpToRegister" class="toRegister" v-if=userIsManager>
            <a-icon type="user" />
            新用户注册
          </a-menu-item>
          <a-menu-item key="review" @click="jumpToReview" class="toReview" >
            <a-icon type="appstore" />进入复盘
          </a-menu-item>
        </a-menu>
        <div class="logout">
          <a-dropdown placement="bottomCenter">
            <div class="user">
              <a-avatar :src="userInfo.url"></a-avatar>
              <span style="font-size: 14px">{{ userInfo.userName }}</span>
              <a-icon style="margin-left: 3px; font-size: 16px" type="down"></a-icon>
            </div>
            <a-menu slot="overlay">
              <a-menu-item @click="quit()">
                <a-icon type="poweroff" class="outIcon"></a-icon>退出登录
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </div>
      </a-layout-header>
      <a-layout-content >
        <!-- 内容部分 -->
        <router-view></router-view>
      </a-layout-content>
      <a-layout-footer style="text-align: center">©2020 Created by GMair</a-layout-footer>
    </a-layout>
    <!-- 管理员展示 -->
    <register
      ref="reg"
    ></register>
    <review
      ref="rev"
    ></review>
  </a-layout>

</template>
<script>
import router from "@/router";
import register from "@/views/manager/component/registerNew";
import review from "@/components/goToReview";
import { mapGetters, mapMutations, mapActions } from "vuex";
export default {
  data() {
    return {
      collapsed: false,
    };
  },
  components: {
    register,
    review,
  },
  computed: {
    ...mapGetters(["userId", "userInfo","userIsManager","userIsMarket"]),
    currentRight: function() {
      let res = ["1"];
      if (this.$route.path === "/messageCenter") {
        res = ["2"];
      } else if (this.$route.path === "/messageCenterMarket") {
        res = ["2"];
      } else if (this.$route.path === "/messageCenterManager") {
        res = ["2"];
      }
      return res;
    },
    currentUp: function() {
      let res = ["1"];
      return res;
    }
  },
  async mounted() {
  },
  methods: {
    ...mapActions([]),
    jumpToKnowledgeList() {
      console.log("跳转到知识库页面")
      router.push("/knowledgeList");
    },
    jumpToMessageCenter() {
      console.log("跳转到消息中心")
      if(this.$store.state.user.userIsManager){
        console.log("manager")
        router.push("messageCenterManager")
      }else if(this.$store.state.user.userIsMarket){
        console.log("market")
        router.push("messageCenterMarket")
      }else{
        console.log("client")
        router.push("messageCenter")
      }
    },
    jumpToRegister(){
      console.log("跳转到管理员注册用户信息")
      this.$refs.reg.showDrawer();
    },
    jumpToReview(){
      console.log("跳转到复盘")
      router.push("review")
    },
    async quit() {
      await this.$store.dispatch("logout");
      router.push(`/login?redirect=${this.$route.fullPath}`);
    }
  }
};
</script>

<style scoped lang="less">
.ant-layout-content {
    height: 580px;
    overflow: auto;
}

.ant-layout-footer {
    padding: 10px 50px;
    color: rgba(0, 0, 0, 0.65);
    font-size: 14px;
    background: #f0f2f5;
}
.header {
  background: #fff;
  padding: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  .logout {
    margin-right: 40px;
    .user {
      cursor: pointer;
      display: flex;
      align-items: center;
      span {
        margin-left: 5px;
      }
    }
  }
}

.label {
  height: 44px;
  line-height: 44px;
  vertical-align: middle;
  min-width: 400px;
  display: flex;
  align-items: center;
  .logo {
    height: 44px;
    vertical-align: top;
    margin: 0px 16px;
    border-style: none;
    cursor: pointer;
  }
  .title {
    font-size: 30px;
    color: rgba(29, 29, 29, 0.85);
    font-family: Avenir, "Helvetica Neue", Arial, Helvetica, sans-serif;
    font-weight: 600;
    position: relative;
    top: 2px;
  }
}
.ant-layout-sider {
  // background: rgba(29, 29, 29, 0.384);
  box-shadow: 0px 2px 20px 2px rgba(145, 145, 144, 0.5);
}
</style>
