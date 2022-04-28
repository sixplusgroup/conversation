import {
    publishKnowledgeAPI,
    getAuditKnowledgeAPI,
    regiseterAPI,
} from '@/api/manager'
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

const manager = {
    state: {
        auditList:[],
        auditTotalNum:0,
        auditListLoading: false,

    },
    mutations: {
        set_auditList: function(state, data) {
            state.auditList = data
        },
        set_auditListParams: function(state, data) {
            state.auditListParams = {
                ...state.auditListParams,
                ...data,
            }
        },
        set_auditTotalNum:function(state, data){
            state.auditTotalNum=data
        },
        set_auditListLoading: function(state, data) {
            state.auditListLoading = data
        },
    },
    actions: {
        getAuditKnowledgePage: async({ state, commit }) => {
            const res = await getAuditKnowledgeAPI().catch(err => {
                console.log('获取审核分页失败')
                console.log(err)
                message.error('获取审核分页失败')
            })
            console.log('获取到审核分页 :')
            if(res){
                commit('set_auditList', res)
                commit('set_auditListLoading', false)
            }
        },
        // knowledgeId
        publishKnowledge:async({ state, commit,dispatch },knowledgeId) => {
            const res = await publishKnowledgeAPI(knowledgeId).catch(err => {
                console.log('发布知识失败')
                console.log(err)
                message.error('发布知识失败')
            })
            console.log('获取到发布知识 :')
            message.success('发布知识成功')
            await dispatch('getAuditKnowledgePage')
            await dispatch('getKnowledgeList')

        },
        // 注册新的普通用户
        register:async({ state, commit,dispatch },data) => {
            const res = await regiseterAPI(data).catch(err => {
                console.log('注册新的用户失败')
                console.log(err)
                message.error('注册新的用户失败')
            })
            console.log('新的用户注册成功')
            message.success('新用户注册成功')
        },
        

}
}
export default manager