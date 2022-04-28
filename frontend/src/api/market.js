import { axios } from '@/utils/request'
const api = {
    marketKnowledgePre: '/knowledge-base/knowledge',
    marketTagPre:'knowledge-base/tag',
    marketCommentPre:'knowledge-base/comment',
}
//  创建知识 check done
export function createKnowledgeAPI(data){
    return axios({
        url: `${api.marketKnowledgePre}/create`,
        method: 'POST',
        data
    })
}
// 删除知识条目 done
export function deleteKnowledgeAPI(id) {
    return axios({
        url: `${api.marketKnowledgePre}/delete/${id}`,
        method: 'GET',
    })
}
// 修改某条知识(除了modifyTime都传， 其中知识ID, title, content必传)
// done
export function modifyKnowledgeAPI(data){
    return axios({
        url: `${api.marketKnowledgePre}/modify`,
        method: 'POST',
        data
    })
}
// 知识采编者对某条知识进行纠错 done
// 评论状态会变为已解决
export function correctKnowledgeAPI(data){
    console.log("API")
    console.log(data)
    return axios({
        url: `${api.marketCommentPre}/correct/${data.commentId}`,
        method: 'POST',
        data:{id:data.id,content:data.content,title:data.title,modifyTime:data.modifyTime}
    })
}

// 采编人员废弃评论 done
export function abandonCommentAPI(id){
    return axios({
        url:`${api.marketCommentPre}/abandon/${id}`,
        method:'POST',
    })
}
// 采编人员根据评论状态分页获得评论列表 done
export function getCommentListByStatusAPI(status){
    return axios({
        url:`${api.marketCommentPre}/getCommentListByStatus`,
        method:'POST',
        params:{status}
    })
}
// 新建标签 check done
export function createTagsAPI(data) {
    return axios({
        url: `${api.marketTagPre}/create`,
        method: 'POST',
        data
    })
}
// 删除标签 check done
export function deleteTagsAPI(id) {
    return axios({
        url: `${api.marketTagPre}/delete/${id}`,
        method: 'POST',
    })
}
// 修改标签 check done
export function modifyTagsAPI(data) {
    return axios({
        url: `${api.marketTagPre}/modifyTagName`,
        method: 'POST',
        data
    })
}
// 新建或者修改知识的标签 check
// 注意查重 done
export function modifyKnowledgeTagsAPI(data) {
    return axios({
        url: `${api.marketTagPre}/modifyKnowledgeTags`,
        method: 'POST',
        data
    })
}

