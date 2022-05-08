import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/login.vue'
// import test from '../views/test.vue'
Vue.use(VueRouter) //模块化开发用到这一句
const routes = [{
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  //   //这里是父级路由，展示的是框架（就是sidebar、header这些），直接redirect到第一个子路由（即具体内容）
  //   //“重定向（redirct）”的意思是，当用户访问 /a时，URL 将会被替换成 /b，然后匹配路由为 /b
  {
    path: '/home',
    name: 'home',
    redirect: '/knowledgeList',
    component: () => import('@/views/home'),
    children: [{
        path: '/knowledgeList',
        name: 'knowlidgeList',
        component: () => import('@/views/knowledgeList')
      },
      {
        path:'/messageCenter',
        name:'messageCenter',
        component:()=>import('@/views/user/userMessage')
      },
      {
        path:'/messageCenterMarket',
        name:'messageCenterMarket',
        component:()=>import('@/views/market/marketMessage')
      },
      {
        path:'/messageCenterManager',
        name:'messageCenterManager',
        component:()=>import('@/views/manager/managerMessage')
      }
    ]
  },
  {
    path: '/review',
    name: 'review',
    redirect: '/reviewHome',
    component: () => import('@/views/review/review'),
    children: [
      {
        path:'/reviewHome',
        name:'reviewHome',
        component:()=>import('@/views/review/reviewHome')
      },
      {
        path:'/reviewSearch',
        name:'reviewSearch',
        component:()=>import('@/views/review/reviewSearch')
      },
      {
        path: '/review/reviewDetail/:rid', //动态路由参数，用$route.params.hotelId使用
        name: 'reviewDetail',
        component: () => import('@/views/review/reviewDetail')
      },
    ]
  },
]
const createRouter = () => new VueRouter({
  // mode: 'history', // require service support
  scrollBehavior: () => ({
    y: 0
  }),
  routes
})
const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router