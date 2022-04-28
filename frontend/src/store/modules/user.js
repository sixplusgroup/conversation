import {
    loginAPI,
    getKnowledgeAPI,
    searchKnowledgeAPI,
    commentKnowledgeAPI,
    getUserCommentListByStatusAPI,
    getAllTagsAPI,
    getTagsByKnowledgeAPI,
    getKnowledgeByTagsAPI,
    getKnowledgeByIdAPI,
    getKeywordsAPI,
} from '@/api/user'
import{
    createTagsAPI,
    modifyTagsAPI
}from '@/api/market'
import router, {
    resetRouter
} from '@/router'
import {
    removeToken,
    setToken,
    getToken
} from '@/utils/auth'
import {
    message
} from 'ant-design-vue'



const user = {
    state: {
        //用户信息待完善
        userInfo: {
            id: 0,
            password: '',
            username: '',
            type: 'Manager',
            url: 'https://software3.oss-cn-beijing.aliyuncs.com/2020-06-28/1593351539438-a3b864b0dfe84abaa0.jpeg',
        },
        userIsManager:false,
        userIsMarket:false,
        token:'',
        knowledgeTotalNum:0,
        knowledgeList: [],
        knowledgeListParams: {
            pageSize: 12,
            pageNo: 1,
        },
        knowledgeListLoading: false,
        currentKnowledgeId: '',
        currentKnowledgeInfo: {
            tags:[],
        },
        tagList:[],
        // Integer id;String tag_name;
        currentTagId:'',
        commentUnresolvedList:[],
        commentResolevdList:[],
        commentAbandonList:[],
        key:''
    },

    mutations: {
        set_AllInfo: function (state, info) {
            state.userInfo.userId = info.id
            state.userInfo.password = info.password
            state.userInfo.userName = info.userName
            state.userInfo.userType = info.type
            //userTypes = ['Client', 'Manager', 'Market']
            state.userType = info.userType
        },
        set_userIsManager:function(state){
            if(state.userInfo.userType == "Manager"){
                state.userIsManager=true;
            }else{
                state.userIsManager=false;
            }
        },
        set_userIsMarket:function(state){
            if(state.userInfo.userType == "Market"){
                state.userIsMarket=true;
            }else{
                state.userIsMarket=false;
            }
        },
        set_token:function (state, data) {
            state.token=data
        },
        set_knowledgeTotalNum:function(state, data){
            state.knowledgeTotalNum=data
        },
        set_knowledgeList: function(state, data) {
            state.knowledgeList = data
        },
        set_knowledgePageListParams: function(state, data) {
            state.knowledgePageListParams = {
                ...state.knowledgePageListParams,
                ...data,
            }
        },
        set_knowledgeListLoading: function(state, data) {
            state.knowledgeListLoading = data
        },
        set_currentKnowledgeId: function(state, data) {
            state.currentKnowledgeId = data
        },
        set_currentKnowledgeInfo: function(state, data) {
            state.currentKnowledgeInfo = {
                ...state.currentKnowledgeInfo,
                ...data,
            }
        },
        set_currentKnowledgeTags:function(state,data){
            state.currentKnowledgeInfo.tags=data
        },
        set_tagList: function(state, data) {
            state.tagList = data
        },
        set_currentTagId: function(state, data) {
            state.currentTagId = data
        },
        set_commentResolvedList: function(state, data) {
            state.commentResolvedList = data
        },
        set_commentUnresolvedList: function(state, data) {
            state.commentUnresolvedList = data;
        },
        set_commentAbandonList: function(state, data) {
            state.commentAbandonList = data
        },
        set_key: function(state, data) {
            state.key = data
        },
    },

    actions: {
        login: async ({
            dispatch,
            state,
            commit
        }, userData) => {
            console.log("登录中")
            const res = await loginAPI(userData).catch(err => {
                console.log('登录失败')
                console.log(err)
                message.error('登录失败')
            })
            // const res = {
            //     "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJlZGl0b3IxIiwic2NvcGUiOlsicmVzb3VyY2U6cmVhZCJdLCJleHAiOjE2NTA4OTU4NTgsImF1dGhvcml0aWVzIjpbImNvbW1lbnRfZ2V0QWxsIiwiY29tbWVudF9tb2RpZnkiXSwianRpIjoiVXMtN1hyVVFHc3pMVHl3cTZwM1R5SXN0X3FBIiwiY2xpZW50X2lkIjoiY2xpZW50MSIsImluZm8iOnsiaWQiOjIsInVzZXJuYW1lIjoiZWRpdG9yMSIsInR5cGUiOiJNYXJrZXQifX0.XBKtWMOY50LNm5XDUrGDdQh8cYRljwxpeLMrOQTgfw4",
            //     "token_type": "bearer",
            //     "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJlZGl0b3IxIiwic2NvcGUiOlsicmVzb3VyY2U6cmVhZCJdLCJhdGkiOiJVcy03WHJVUUdzekxUeXdxNnAzVHlJc3RfcUEiLCJleHAiOjE2NTM0NDQ2NTgsImF1dGhvcml0aWVzIjpbImNvbW1lbnRfZ2V0QWxsIiwiY29tbWVudF9tb2RpZnkiXSwianRpIjoiZV9GaE16S3B1QlJOdzNZTGY5Z1Q2UU9weVVRIiwiY2xpZW50X2lkIjoiY2xpZW50MSIsImluZm8iOnsiaWQiOjIsInVzZXJuYW1lIjoiZWRpdG9yMSIsInR5cGUiOiJNYXJrZXQifX0.BkBS1UT6B7eYs2YZFOi7NmjVTs1a6YJetCZvflZz5TA",
            //     "expires_in": 43199,
            //     "scope": "resource:read",
            //     "info": {
            //         "id": 2,
            //         "username": "editor1",
            //         "type": "Market"
            //     },
            //     "jti": "Us-7XrUQGszLTywq6p3TyIst_qA"
            // }
            console.log('获取到登录token :')
            console.log(res)
            if (res) {
                commit('set_token',res.access_token)
                commit('set_AllInfo', res.info)
                commit('set_userIsManager')
                console.log(state.userIsManager)
                commit('set_userIsMarket')
                console.log(state.userIsMarket)
                console.log(state.userInfo)
                //判断用户类别，推送到不同主页
                router.push('/home')
            }
        },
        
        // 获取分页知识
        getKnowledgeList: async({commit, state}) => {
            // state.knowledgeList=[]
            // for (let i = 0; i < 60; i++) {
            //     state.knowledgeList.push({ id:i+10,title: "liu" ,content:"very handsome:getKnowledgeAPI(knowledgePageListParams).catch(err => {"});
            // }
            // commit('set_knowledgeTotalNum',60)

            const res = await getKnowledgeAPI().catch(err => {
                console.log('获取分页失败')
                console.log(err)
                message.error('获取分页失败')
            })
            // console.log('获取到分页 :')
            if(res){
                commit('set_knowledgeList', res)
                commit('set_knowledgeListLoading', false)
            }
        },
        // 获取分页知识ydl 
        // getPage(pageNum) {
        //     this.loading = true;
        //     const pageSize = this.pagination.defaultPageSize;
        //     judgement
        //         .getPageAPI({ pageSize, pageNum })
        //         .then((res) => {
        //         this.pagination.total = res.data.data.docs.totalNum;
        //         this.data = res.data.data.docs.documents.map((o) => ({
        //             ...o,
        //             status: o.status === '0' ? '未发布' : '已发布',
        //         }));
        //         this.loading = false;
        //         })
        //         .catch((e) => {
        //         this.$message.error(e);
        //         });
        //     },
        

        // 
        searchKnowledge:async({ state, commit },searchPageParam) => {//GET
            const res = await searchKnowledgeAPI(searchPageParam).catch(err=>{
                console.log('搜索知识失败')
                console.log(err)
                message.error('搜索知识失败')
            })
            commit('set_knowledgeListLoading', false)
            if(res){
                message.success('搜索知识成功')
                console.log(res)
                commit('set_knowledgeList', res)
                commit('set_knowledgeListLoading', false)
            }else{
                commit('set_knowledgeList', [])
                commit('set_knowledgeListLoading', false)
            }
        },

        // {Integer knowledgeId;String content;responserId;type;}
        // type是评论的人是审核者还是普通用户。responerId是审核者或普通用户的id
        commentKnowledge: async ({
            state,
            commit,
            dispatch
        }, data) => {
            const res = await commentKnowledgeAPI({
                ...data,
            }).catch(err => {
                console.log('知识评价失败')
                console.log(err)
                message.error('知识评价失败')
            })
            if (res) {
                message.success('知识评价成功')
                console.log(res)
            }
        },

        // userId,status
        getUserCommentListByStatus: ({
            state,
            commit
        },data) => {
            getUserCommentListByStatusAPI(data).then((res) => {
                if (res) {
                    message.success('获取自身的知识评论成功')
                    if(data.status=='待解决'){commit('set_commentUnresolvedList', res)}
                    if(data.status=='已解决'){commit('set_commentResolvedList', res)}
                    if(data.status=='废弃'){commit('set_commentAbandonList', res)}
                }else{
                    message.success('没有相应评论')
                    console.log(res)
                    if(data.status=='待解决'){commit('set_commentUnresolvedList', [])}
                    if(data.status=='已解决'){commit('set_commentResolvedList', [])}
                    if(data.status=='废弃'){commit('set_commentAbandonList', [])}
                }
            }).catch(err => {
                console.log('获取自身的知识评论失败')
                console.log(err)
                message.error('获取自身的知识评论失败')
            })
        },

        getAllTags:async({ state, commit }) => {
            const res = await getAllTagsAPI().catch(err=>{
                console.log('获取所有标签失败')
                console.log(err)
                message.error('获取所有标签失败')
            })
            // console.log("获取tags")
            if(res){
                let data=[]
                res.forEach(s=>{
                    if(s.tag_name){
                        data.push({...s,tagEditable:false})
                    }
                })
                message.success('获取所有标签成功')
                commit("set_tagList",data)
            }
        },

        // knowledgeId
        getTagsByKnowledge:async({ state, commit },data) => {
            const res = await getTagsByKnowledgeAPI(data).catch(err=>{
                console.log('获取知识对应标签失败')
                console.log(err)
                message.error('获取知识对应标签失败')
            })
            if(res){
                let data=[]
                res.forEach(s=>{
                    if(s.tag_name){
                        data.push(s)
                    }
                })
                message.success('获取知识对应标签成功')
                commit("set_currentKnowledgeTags",data)
            }
        },

        // {Integer pageSize;Integer pageNum;List<Integer> tag_ids;}
        getKnowledgeByTags:async({ state, commit },data) => {
            const res = await getKnowledgeByTagsAPI(data).catch(err=>{
                console.log('获取标签对应知识失败')
                console.log(err)
                message.error('获取标签对应知识失败')
            })
            if(res){
                message.success('获取标签对应知识成功')
                console.log(res)
                commit('set_knowledgeList', res)
            }else{
                commit('set_knowledgeList', [])
            }
        },

        getKnowledgeById:async({ state, dispatch,commit },id) => {
            const res = await getKnowledgeByIdAPI(id).catch(err=>{
                console.log('获取id对应知识失败')
                console.log(err)
                message.error('获取id对应知识失败')
            })
            if(res){
                message.success('获取id对应知识成功')
                console.log(res)
                commit('set_currentKnowledgeInfo', res)
                await dispatch('getTagsByKnowledge',id)
            }else{
                commit('set_currentKnowledgeInfo', {})
            }
        },
        // tagName
        createTags:async({ state, commit,dispatch }, data) => {//POST
            var duplication = false;
            // for (let item of this.$store.state.tagList) {
            //     if(item.tag_name == data.tag_name){
            //         duplication = true;
            //         break
            //     }
            // }
            console.log(state)
            state.tagList.some(s=>{
                if(s.tag_name == data.tag_name){
                    duplication = true;
                    return true;
                }
            })
            if(duplication){message.error('添加标签重复')}
            else{
                const res = await createTagsAPI(data).catch(err=>{
                console.log('添加标签失败')
                console.log(err)
                message.error('添加标签失败')
                })

                message.success('添加标签成功')
                console.log(res)
                await dispatch('getAllTags')
            }
        },
         // tag:{Integer id;String tag_name;}
        modifyTags:async({ state, commit,dispatch }, data) => {//POST
            var duplication = false;
            state.tagList.some(s=>{
                if(s.tag_name === data.tag_name && s.id !== data.id){
                    duplication = true;
                    return true;
                }
            })
            if(duplication){message.error('添加标签重复')}
            else{
                const res = await modifyTagsAPI({id:data.id,tag_name:data.tag_name}).catch(err=>{
                    console.log('修改标签失败')
                    console.log(err)
                    message.error('修改标签失败')
                })
                message.success('修改标签成功')
                await dispatch('getAllTags')
            }
        },
        getKeywords: async({commit, state}) => {
            const res = await getKeywordsAPI().catch(err => {
                console.log('获取关键词推荐失败')
                console.log(err)
                message.error('获取关键词推荐失败')
            })
            if(res){
                commit('set_key', res)
            }
        },
    }
}

export default user