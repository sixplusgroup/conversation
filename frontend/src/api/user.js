import { axios } from '@/utils/request'

const api = {
    userKnowledgePre: 'knowledge-base/knowledge',
    userTagPre:'knowledge-base/tag',
    userCommentPre:'knowledge-base/comment'
}
//  done
export function loginAPI(data){
    console.log('loginAPI')
    return axios({
        url:`oauth/token`,
        method: 'POST',
        data,
        transformRequest: [
            function (data) {
                let ret = "";
                for (let i in data) {
                    ret += encodeURIComponent(i) + "=" + encodeURIComponent(data[i]) + "&";
                }
                ret = ret.substring(0, ret.lastIndexOf("&"));
                return ret;
            }
        ],
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        }
    })
}
// 获取分页内容 check done
export function getKnowledgeAPI(data){
    return axios({
        url:`${api.userKnowledgePre}/getPublished`,
        method: 'POST',
        data
    })
}
// 根据关键词搜索 check done
export function searchKnowledgeAPI(data) {
    return axios({
        url: `${api.userKnowledgePre}/searchByTagsKeys`,
        method: 'POST',
        data
    })
}
// 对某条知识进行评论纠错 done
export function commentKnowledgeAPI(data) {
    return axios({
        url: `${api.userCommentPre}/create`,
        method: 'POST',
        data
    })
}
// 获取所有标签 done
export function getAllTagsAPI(data) {
    return axios({
        url: `${api.userTagPre}/getAllTags`,
        method: 'POST',
        data
    })
}
// 获取某条知识的所有标签 done
export function getTagsByKnowledgeAPI(id) {
    return axios({
        url: `${api.userTagPre}/getTagsByKnowledge/${id}`,
        method: 'POST',
    })
}
// 通过标签查找知识 done
export function getKnowledgeByTagsAPI(data) {
    return axios({
        url: `${api.userTagPre}/getByTags`,
        method: 'POST',
        data
    })
}
// 用户根据评论状态分页获得自己的评论列表 check done
export function getUserCommentListByStatusAPI(data) {
    return axios({
        url: `${api.userCommentPre}/getUserCommentListByStatus`,
        method: 'POST',
        params:{userId:data.userId,status:data.status}
    })
}

// done
export function getKnowledgeByIdAPI(id){
    return axios({
        url:`${api.userKnowledgePre}/getById/${id}`,
        method: 'POST',
    })
}
// done
export function getKeywordsAPI(){
    return axios({
        url:`${api.userKnowledgePre}/keywords/`,
        method: 'GET',
    })
}