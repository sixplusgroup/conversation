// <template>
  <div id="app" v-title data-title="GMair">
    <!-- 过渡效果，生成css类fade-transform，本页下面查看 -->
    <transition name="fade-transform" mode="out-in">
      <!-- 最顶层的出口，渲染最高级路由匹配到的组件 -->
      <router-view/>
    </transition>
  </div>
</template>

<script>

export default {
  name: 'App',
  components: {
    
  },
  created(){
    //   在页面加载时读取sessionStorage里的状态信息
    if ( sessionStorage.getItem('state') ) {
      this.$store.replaceState( Object.assign( {}, this.$store.state,
      JSON.parse(sessionStorage.getItem('state') ) ) )
    }

    //   页面刷新时将state数据存储到sessionStorage中
    window.addEventListener('beforeunload',()=>{
      sessionStorage.setItem('state',JSON.stringify(this.$store.state) )
    })
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  overflow: hidden;/*隐藏最外围的滚动条，固定header*/
  width: 100%;
  height: 100%;
  padding-top: 0px;/* 这一句一定要有，父元素的第一个子元素（如login）的上边距margin-top如果碰不到有效的border或者padding，
  会假传圣旨，把自己的margin当父元素的margin执行，使得父元素被往下顶掉一截*/


}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}

/* fade-transform */
.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all .5s;
}

.fade-transform-enter {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
.ant-descriptions-title {
  margin-bottom: 20px;
  color: rgba(0, 0, 0, 0.85);
  font-weight: 700;
  font-size: 16px;
  line-height: 1.5;
}
.ant-descriptions-view {
  width: 100%;
  overflow: hidden;
  border-radius: 4px;
}
.ant-descriptions-bordered .ant-descriptions-view > table {
  table-layout: auto;
}
.ant-descriptions-view table {
  width: 100%;
  table-layout: fixed;
}
.ant-descriptions-bordered .ant-descriptions-view {
  border-top: 1px solid #e8e8e8;
  border-left: 1px solid #e8e8e8;
}
.ant-descriptions-bordered .ant-descriptions-row {
  border-bottom: 1px solid #e8e8e8;
  width: 100%;
}
.ant-descriptions-bordered .ant-descriptions-item-label {
  background-color: #fafafa;
}
.ant-descriptions-bordered .ant-descriptions-item-content,
.ant-descriptions-bordered .ant-descriptions-item-label {
  padding: 16px 24px;
  border-right: 1px solid #e8e8e8;
}
.ant-descriptions-item-label {
  color: rgba(0, 0, 0, 0.85);
  font-weight: 400;
  font-size: 14px;
  line-height: 1.5;
}
.ant-descriptions-item-content {
  display: table-cell;
  color: rgba(0, 0, 0, 0.65);
  font-size: 14px;
  line-height: 1.5;
}
</style>

