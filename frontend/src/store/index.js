import Vue from 'vue'
import Vuex from 'vuex'
import user from './modules/user'
import manager from './modules/manager'
import market from './modules/market'
import review from './modules/review'
import getters from './getters'
Vue.use(Vuex)

//简单理解为前端数据库，MVC里的M，避免组件传参过于混乱
export default new Vuex.Store({
  modules: {
    market,
    user,
    manager,
    review
  },
  //数据库
  state: {
  },
  //存数据,只能同步操作
  //store.commit('方法名')触发
  mutations: {
  },
  //存数据前做的处理，可以做异步操作，最终也是commit一个mutations
  //store.dispatch('方法名')触发
  actions: {
  },
  //取数据
  getters
})
