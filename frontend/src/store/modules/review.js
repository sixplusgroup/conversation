import {
  loginReviewAPI,
  getrecommendReviewListAPI,
  searchReviewAPI,
  getReviewDetailAPI,
} from '@/api/review'
import router, {
  resetRouter
} from '@/router'
import {
  message
} from 'ant-design-vue'

const review = {
  state: {
      reviewList:[],
      reviewListLoading: false,
      chatlog:[],
      chatlogLoading:false,
      currentReviewId:1,
      reviewPageParam:{
        pageNo:1,
        pageSize:4,
      },
      historyPageParam:{
        pageNo:1,
        pageSize:4,
      }
  },
  mutations: {
      set_reviewList: function(state, data) {
          state.reviewList = data
      },
      set_reviewListLoading: function(state, data) {
          state.reviewListLoading = data
      },
      set_chatlog: function(state, data) {
        state.chatlog = data
      },
      set_chatlogLoading: function(state, data) {
        state.chatlogLoading = data
      },
      set_currentReviewId: function(state, data) {
        state.currentReviewId = data
    },
  },
  actions: {
      loginReview: async({ state, commit },data) => {
          const res = await loginReviewAPI(data).catch(err => {
              console.log('登录复盘页面失败')
              console.log(err)
              message.error('登录复盘页面失败')
          })
          console.log('跳转到复盘页面 :')
          if(res){

          }
      },
      // 
      getrecommendReviewList:async({ state, commit,dispatch }) => {
          // const res = await getrecommendReviewListAPI().catch(err => {
          //     console.log('获取复盘推荐列表失败')
          //     console.log(err)
          //     message.error('获取复盘推荐列表失败')
          // })
          const res=[
            {rid:1,pid:1,uid:1,startTime:"2022-04-12 11:20:22",endTime:"2022-06-12 11:20:22",rate:5.33},
            {rid:2,pid:2,uid:2,startTime:"2022-04-12 11:20:22",endTime:"2022-06-12 11:20:22",rate:5.33},
            {rid:3,pid:3,uid:3,startTime:"2022-04-12 11:20:22",endTime:"2022-06-12 11:20:22",rate:5.33},
            {rid:4,pid:4,uid:4,startTime:"2022-04-12 11:20:22",endTime:"2022-06-12 11:20:22",rate:5.33},
            {rid:5,pid:5,uid:5,startTime:"2022-04-12 11:20:22",endTime:"2022-06-12 11:20:22",rate:5.33},
            {rid:6,pid:6,uid:6,startTime:"2022-04-12 11:20:22",endTime:"2022-06-12 11:20:22",rate:5.33},
          ]
          console.log('获取到复盘推荐列表 :')
          if(res){
            message.success('复盘推荐列表获取成功')
            console.log(res)
            commit('set_reviewList',res)
            commit('set_reviewListLoading',false)
          }else{
            message.success('复盘空推荐列表获取成功')
            console.log(res)
            commit('set_reviewList',[])
            commit('set_reviewListLoading',false)
          }
      },
      // 
      searchReview:async({ state, commit,dispatch },data) => {
          const res = await searchReviewAPI(data).catch(err => {
              console.log('查找复盘失败')
              console.log(err)
              message.error('查找复盘失败')
          })
          console.log('获取到复盘查找列表 :')
          if(res){
            message.success('复盘查找列表获取成功')
            console.log(res)
            commit('set_reviewList',res)
            commit('set_reviewListLoading',false)
          }else{
            message.success('复盘空查找列表获取成功')
            console.log(res)
            commit('set_reviewList',[])
            commit('set_reviewListLoading',false)
          }
      },
      getReviewDetail:async({ state, commit,dispatch },data) => {
        const res = await getReviewDetailAPI(data).catch(err => {
            console.log('获取复盘详细信息失败')
            console.log(err)
            message.error('获取复盘详细信息失败')
        })
        console.log('获取到复盘详细信息 :')
        if(res){
          message.success('复盘详细信息获取成功')
          console.log(res)
          commit('set_chatlog',res)
          commit('set_chatlogLoading',false)
        }
    },
      

}
}
export default review