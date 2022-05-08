const getters = {
  //user
  userId: state => state.user.userInfo.userId,
  userType: state => state.user.userInfo.userType,
  userInfo: state => state.user.userInfo,
  userIsManager: state => state.user.userIsManager,
  userIsMarket: state => state.user.userIsMarket,
  token: state => state.user.token,
  knowledgeTotalNum: state => state.user.knowledgeTotalNum,
  knowledgeList: state => state.user.knowledgeList,
  knowledgeListParams: state => state.user.knowledgeListParams,
  knowledgeListLoading: state => state.user.knowledgeListLoading,
  currentKnowledgeId: state => state.user.currentKnowledgeId,
  currentKnowledgeInfo: state => state.user.currentKnowledgeInfo,
  tagList:state => state.user.tagList,
  currentTagId: state => state.user.currentTagId,
  knowledgeShown:(state) => (id) => {
    return state.user.knowledgeList.find(knowledge => knowledge.id === id)
  },
  commentUnresolvedList:state => state.user.commentUnresolvedList,
  commentResolvedList:state => state.user.commentResolvedList,
  commentAbandonList:state => state.user.commentAbandonList,
  key:state => state.user.key,

  //market
  commentUnresolvedListMarket: state => state.market.commentUnresolvedListMarket,
  commentResolvedListMarket: state => state.market.commentResolvedListMarket,
  commentAbandonListMarket: state => state.market.commentAbandonListMarket,
  marketCommentListParams: state => state.market.marketCommentListParams,

  //manager
  auditListParams: state => state.manager.auditListParams,
  auditList: state => state.manager.auditList,
  auditTotalNum: state => state.manager.auditTotalNum,
  auditListLoading: state => state.manager.auditListLoading,
  
  // review
  reviewList: state => state.review.reviewList,
  reviewListLoading: state => state.review.reviewListLoading,
  chatlog: state => state.review.chatlog,
  chatlogLoading: state => state.review.chatlogLoading,
  sentiGraph: state => state.review.chatlog,
  sentiGraphLoading: state => state.review.chatlogLoading,
  reviewDetail: state => state.review.reviewDetail,
  currentReviewId: state => state.review.currentReviewId,
  currentPid: state => state.review.currentPid,
  currentCid: state => state.review.currentCid,
  totalNumReviewRecommend: state => state.review.totalNumReviewRecommend,
  pageNumReviewRecommend: state => state.review.pageNumReviewRecommend,
  totalNumReviewSearch: state => state.review.totalNumReviewSearch,
  pageNumReviewSearch: state => state.review.pageNumReviewSearch,
  recommendPageParam: state => state.review.recommendPageParam,
  historyPageParam: state => state.review.historyPageParam,
  staff: state => state.review.staff,
  customer: state => state.review.customer,
}

export default getters