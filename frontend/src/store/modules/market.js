import {
    createKnowledgeAPI,
    deleteKnowledgeAPI,
    modifyKnowledgeAPI,
    correctKnowledgeAPI,
    abandonCommentAPI,
    getCommentListByStatusAPI,
    deleteTagsAPI,
    modifyTagsAPI,
    modifyKnowledgeTagsAPI,
} from '@/api/market'
import { message } from 'ant-design-vue'
import router, {
    resetRouter
} from '@/router'
import {
    removeToken,
    setToken,
    getToken
} from '@/utils/auth'

const market = {
    state: {
        commentUnresolvedListMarket:[],
        commentResolvedListMarket:[],
        commentAbandonListMarket:[],
        marketCommentListParams: {
            pageSize: 12,
            pageNo: 1,
            
        },
    },
    mutations: {
        set_commentResolvedListMarket: function(state, data) {
            state.commentResolvedListMarket = data
        },
        set_commentUnresolvedListMarket: function(state, data) {
            console.warn(data);
            state.commentUnresolvedListMarket = data
            console.warn(state);
        },
        set_commentAbandonListMarket: function(state, data) {
            state.commentAbandonListMarket = data
        },
        set_marketCommentListParams: function(state, data) {
            state.marketCommentListParams = {
                ...state.marketCommentListParams,
                ...data,
            }
        },
    },
    actions: {
        //  {String title; String content;}
        createKnowledge:async({ state, commit ,dispatch}, data) => {
            const res = await createKnowledgeAPI(data).catch(err=>{
                console.log('添加知识失败')
                console.log(err)
                message.error('添加知识失败')
            })
            message.success('添加知识成功')
            console.log(res)
            
        },
        // knowledgeId
        deleteKnowledge:async({ state, commit ,dispatch}, knowledgeId) => {
            const res = await deleteKnowledgeAPI(knowledgeId).catch(err=>{
                console.log('删除知识失败')
                console.log(err)
                message.error('删除知识失败')
            })
            
            message.success('删除知识成功')
            console.log(res)
            await dispatch('getKnowledgeList')
        
        },
        // knowledgeVO:{knowledgeId,title,content}
        modifyKnowledge:async({ state, commit ,dispatch}, data) => {
            const res = await modifyKnowledgeAPI(data).catch(err=>{
                console.log('修改知识失败')
                console.log(err)
                message.error('修改知识失败')
            })
            message.success('修改知识成功')
            await dispatch('getKnowledgeList')
        },
        //commentId，knowledgeVO:{knowledgeId，title，content} 
        correctKnowledge:async({ state, commit ,dispatch}, data) => {
            console.log("correct")
            console.log(data)
            const res = await correctKnowledgeAPI(data).catch(err=>{
                console.log('根据建议修改知识失败')
                console.log(err)
                message.error('根据建议修改知识失败')
            })
            message.success('根据建议修改知识成功')
            console.log(res)
            await dispatch('getKnowledgeList')
            await dispatch('getCommentListByStatus','待解决')
            await dispatch('getCommentListByStatus','已解决')
            await dispatch('getAuditKnowledgePage')
        },
        // commentId
        abandonComment:async({ state, commit ,dispatch}, commentId) => {
            const res = await abandonCommentAPI(commentId).catch(err=>{
                console.log('废弃评论失败')
                console.log(err)
                message.error('废弃评论失败')
            })
            message.success('废弃评论成功')
            console.log(res)
            await dispatch('getCommentListByStatus','待解决')
            await dispatch('getCommentListByStatus','废弃')
        },
        // String status
        getCommentListByStatus: async ({
            state,
            commit
        },data) => { 
            console.log("获取评论列表")
            getCommentListByStatusAPI(data).then(res => {
                if (res) {
                    console.log('采编人员获取修改建议成功')

                    //！important 好好检查自己传进来的数据是什么，下面的处理函数要什么，不要复制粘贴写代码

                    if(data=='待解决'){commit('set_commentUnresolvedListMarket', res)}
                    if(data=='已解决'){commit('set_commentResolvedListMarket', res)}
                    if(data=='废弃'){commit('set_commentAbandonListMarket', res)}
                    
                }else{
                    console.log('没有相应评论')
                    console.log(res)
                    if(data=='待解决'){commit('set_commentUnresolvedListMarket', [])}
                    if(data=='已解决'){commit('set_commentResolvedListMarket', [])}
                    if(data=='废弃'){commit('set_commentAbandonListMarket', [])}
                }
            }).catch(err => {
                console.log('采编人员获取修改建议失败')
                console.log(err)
                message.error('采编人员获取修改建议失败')
            })
        },
        
        // tagId
        deleteTags:async({ state, commit,dispatch }, data) => {//POST
            const res = await deleteTagsAPI(data).catch(err=>{
                console.log('删除标签失败')
                console.log(err)
                message.error('删除标签失败')
            })
            message.success('删除标签成功')
            await dispatch('getAllTags')
        },
        
        // {Integer knowledge_id;List<Integer> add_tags;List<Integer> delete_tags;}
        modifyKnowledgeTags:async({ state, commit,dispatch }, data) => {//POST
            const res = await modifyKnowledgeTagsAPI(data).catch(err=>{
                console.log('修改知识拥有的标签失败')
                console.log(err)
                message.error('修改知识拥有的标签失败')
            })
            message.success('修改知识拥有的标签成功')
        },
        
    }
}
export default market