import Vue from 'vue'
import App from './App.vue'
import router from './router'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'
import store from './store'
import '@/permission'
import * as echarts from 'echarts';
import VueLazyload from 'vue-lazyload'
Vue.prototype.$echarts = echarts
Vue.config.productionTip = false
// const options = {
//   namespace: 'pro__',
//   name: 'ls',
//   storage: 'local'
// }
Vue.use(Antd)
Vue.use(VueLazyload)
Vue.directive('title', {
  // inserted: function (el, binding) {
  //   document.title = el.dataset.title
  // }
})

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
