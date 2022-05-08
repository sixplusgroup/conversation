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
// done
export function getAllStaffAPI(){
    return axios({
        url:`${api.reviewPre}/getAllStaff`,
        method:'GET',
    })
}

// done
export function getAllCustomerAPI(staffId){
    return axios({
        url:`${api.reviewPre}/getAllCustomer`,
        method:'GET',
        params:{staffId},
    })
}

// done
export function getRecommendReviewListAPI(data) {
    return axios({
        url: `${api.reviewPre}/recommend`,
        method: 'POST',
        data,
    })
}

// done
export function searchReviewAPI(data) {
    return axios({
        url: `${api.reviewPre}/history`,
        method: 'POST',
        params:{staffId:data.staffId,customerId:data.customerId},
        data:{pageNum:data.pageNum,pageSize:3}
    })
}
// todo
export function getReviewDetailAPI(id) {
    return axios({
        url: `${api.reviewPre}/detail`,
        method: 'GET',
        params:{id},
    })
}