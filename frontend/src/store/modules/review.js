import {
  loginReviewAPI,
  getAllStaffAPI,
  getAllCustomerAPI,
  getRecommendReviewListAPI,
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
      //pid,cid,sentigraph,totalnum
      reviewList:[], 
      reviewListLoading: false,
      chatlog:[],
      chatlogLoading:false, //userType,time,content
      sentiGraph:[],
      sentiGraphLoading:false, //userType,time,emoRate
      reviewDetail:{},
      currentReviewId:1,
      currentPid:0,
      currentCid:0,
      totalNumReviewRecommend:0,
      pageNumReviewRecommend:1,
      totalNumReviewSearch:0,
      pageNumReviewSearch:1,
      recommendPageParam:{
        pageNo:1,
        pageSize:3,
      },
      historyPageParam:{
        pageNo:1,
        pageSize:3,
      },
      staff:[],
      customer:[],
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
      set_sentiGraph: function(state, data) {
        state.sentiGraph = data
      },
      set_sentiGraphLoading: function(state, data) {
        state.sentiGraphLoading = data
      },
      set_reviewDetail: function(state, data) {
        state.reviewDetail = {
          ...data
        }
      },
      set_currentReviewId: function(state, data) {
        state.currentReviewId = data
      },
      set_currentPid: function(state, data) {
        state.currentPid = data
      },
      set_currentCid: function(state, data) {
        state.currentCid = data
      },
      set_totalNumReviewRecommend: function(state, data) {
        state.totalNumReviewRecommend = data
      },
      set_pageNumReviewRecommend:function(state, data) {
        state.pageNumReviewRecommend = data
      },
      set_totalNumReviewSearch: function(state, data) {
        state.totalNumReviewSearch = data
      },
      set_pageNumReviewSearch: function(state, data) {
        state.pageNumReviewSearch = data
      },
      set_recommendPageParam:function(state,data){
        state.pageNo = data,
        state.pageSize = 3
      },
      set_historyPageParam:function(state,data){
        state.pageNo = data,
        state.pageSize = 3
      },
      set_staff: function(state, data) {
        state.staff = data
      },
      set_customer: function(state, data) {
        state.customer = data
      },
  },
  actions: {
      loginReview: async({ state, commit },data) => {
          const res = await loginReviewAPI(data).catch(err => {
              console.log('????????????????????????')
              console.log(err)
              message.error('????????????????????????')
          })
          console.log('????????????????????? :')
          router.push('/review')
      },
      getAllStaff: async({state,commit,dispatch})=>{
        const res = await getAllStaffAPI().catch(err=>{
          console.log('????????????????????????')
          console.log(err)
          console.log('????????????????????????')
        })
        if(res){
          console.log('????????????????????????')
          console.log(res)
          commit('set_staff',res)
        }
      },
      getAllCustomer: async({state,commit,dispatch},data)=>{
        const res = await getAllCustomerAPI(data).catch(err=>{
          console.log('????????????????????????')
          console.log(err)
          console.log('????????????????????????')
        })
        if(res){
          console.log('????????????????????????')
          console.log(res)
          commit('set_customer',res)
        }
      },
      // 
      getRecommendReviewList:async({ state, commit,dispatch },recommendPageParam) => {
          const res = await getRecommendReviewListAPI(recommendPageParam).catch(err => {
              console.log('??????????????????????????????')
              console.log(err)
              message.error('??????????????????????????????')
          })
          console.log('??????????????????????????? :')
          if(res){
            message.success('??????????????????????????????')
            console.log(res)
            commit('set_reviewList',res.list)
            commit('set_totalNumReviewRecommend',res.totalNum+1)
            // commit('set_pageNumReviewRecommend',Math.ceil((res.totalNum+1)/3))
            commit('set_reviewListLoading',false)
            console.log("loading??????false")
          }else{
            message.success('?????????????????????????????????')
            console.log(res)
            commit('set_reviewList',[])
            commit('set_totalNumReviewRecommend',0)
            // commit('set_pageNumReviewRecommend',0)
            commit('set_reviewListLoading',false)
            console.log("loading??????false")

          }
      },
      // 
      searchReview:async({ state, commit,dispatch },data) => {
          const res = await searchReviewAPI(data).catch(err => {
              console.log('??????????????????')
              console.log(err)
              message.error('??????????????????')
          })
          console.log('??????????????????????????? :')
          if(res){
            message.success('??????????????????????????????')
            console.log(res)
            commit('set_reviewList',res.list)
            commit('set_totalNumReviewSearch',res.totalNum+1)
            // commit('set_pageNumReviewSearch',Math.ceil((res.totalNum+1)/3))
            commit('set_reviewListLoading',false)
          }else{
            message.success('?????????????????????????????????')
            console.log(res)
            commit('set_reviewList',[])
            commit('set_totalNumReviewSearch',0)
            // commit('set_pageNumReviewSearch',0)
            commit('set_reviewListLoading',false)
          }
      },
      getReviewDetail:async({ state, commit,dispatch },data) => {
        const res = await getReviewDetailAPI(data).catch(err => {
            console.log('??????????????????????????????')
            console.log(err)
            message.error('??????????????????????????????')
        })
        console.log('??????????????????????????? :')
        if(res){
          message.success('??????????????????????????????')
          console.log(res)
          commit('set_reviewDetail',res)
        }
    },
      

}
}
export default review