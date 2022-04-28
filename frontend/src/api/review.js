import { axios } from '@/utils/request'
const api = {
    reviewPre: '/api/review',
}
// 审核者审核通过，发布知识 todo
export function loginReviewAPI(data) {
    return axios({
        url: `${api.reviewPre}/login`,
        method: 'POST',
        data
    })
}

// 获取需要审核的知识的分页内容 todo
export function getrecommendReviewListAPI(data) {
    return axios({
        url: `${api.reviewPre}/recommend`,
        method: 'POST',
        data,
    })
}

// todo
export function searchReviewAPI(data) {
    return axios({
        url: `${api.reviewPre}/find`,
        method: 'POST',
        data,
    })
}
// todo
export function getReviewDetailAPI(data) {
  return axios({
      url: `${api.reviewPre}/detail`,
      method: 'POST',
      data,
  })
}