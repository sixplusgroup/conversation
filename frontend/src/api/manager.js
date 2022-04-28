import { axios } from '@/utils/request'
const api = {
    managerKnowledgePre: '/knowledge-base/knowledge',
    managerTagPre:'knowledge-base/tag'
}
// 审核者审核通过，发布知识 done
export function publishKnowledgeAPI(id) {
    return axios({
        url: `${api.managerKnowledgePre}/publish`,
        method: 'POST',
        params:{id}
    })
}

// 获取需要审核的知识的分页内容 done
export function getAuditKnowledgeAPI(data) {
    return axios({
        url: `${api.managerKnowledgePre}/getAudit`,
        method: 'POST',
        data,
    })
}

// done
export function regiseterAPI(data) {
    return axios({
        url: `knowledge-base/user/create`,
        method: 'POST',
        data,
    })
}
