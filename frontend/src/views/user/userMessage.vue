<template>
  <div class="manageKnowledge-wrapper">
    <KnowledgeShowUser
      ref="knowledgeShow"
    ></KnowledgeShowUser>
    <a-tabs>
      <a-tab-pane tab="尚未被处理的信息" key="1">
        <a-table
        :columns="columns1"
        :data-source="this.commentUnresolvedList"
        :loading="loading"
        :pagination="pagination" 
        :rowKey="(record, index) => index"
        >
          <span slot="view" slot-scope="text,record">
            <a-button type="primary" size="small" style="" @click="viewKnowledge(record)">查看</a-button>
          </span>
        </a-table>
      </a-tab-pane>
      <a-tab-pane tab="被处理的信息" key="2">
        <a-table
        :columns="columns2"
        :data-source="this.commentResolevdList"
        :loading="loading"
        :pagination="pagination" 
        :rowKey="(record, index) => index"
        >
          <span slot="view" slot-scope="text,record">
            <a-button type="primary" size="small" style="" @click="viewKnowledge(record)">查看</a-button>
          </span>
        </a-table>
      </a-tab-pane>
      <a-tab-pane tab="被忽视的信息" key="3">
        <a-table
        :columns="columns2"
        :data-source="this.commentAbandonList"
        :loading="loading"
        :pagination="pagination" 
        :rowKey="(record, index) => index"
        >
          <span slot="view" slot-scope="text,record">
            <a-button type="primary" size="small" style="" @click="viewKnowledge(record)">查看</a-button>
          </span>
        </a-table>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>
<script>
import { mapGetters, mapMutations, mapActions } from "vuex";
import { message } from "ant-design-vue";
import KnowledgeShowUser from "@/views/user/knowledgeShow";

import moment from "moment";

const columns1 = [
  {
    title: '评论id',
    dataIndex: 'id',
    sorter: (a, b) => a.id-b.id,
  },
  {
    title: '知识ID',
    dataIndex: 'knowledgeId',
    sorter: (a, b) => a.knowledgeId-b.knowledgeId,
  },
  {
    title: '评论',
    dataIndex: 'content',
    sorter: (a, b) => a.content.localeCompare(b.content),
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    sorter: (a, b) => { return a.createTime> b.createTime? 1 : -1 },
  },
  {
    title: "查看对应知识",
    key: "view",
    scopedSlots: { customRender: "view" }
  },
];
const columns2 = [
  {
    title: '评论id',
    dataIndex: 'id',
    sorter: (a, b) => a.id-b.id,
  },
  {
    title: '知识ID',
    dataIndex: 'knowledgeId',
    sorter: (a, b) => a.knowledgeId-b.knowledgeId,
  },
  {
    title: '评论',
    dataIndex: 'content',
    sorter: (a, b) => a.content.localeCompare(b.content),
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    sorter: (a, b) => { return a.createTime> b.createTime? 1 : -1 },
  },
  {
    title: '解决时间',
    dataIndex: 'solveTime',
    sorter: (a, b) => { return a.solveTime> b.solveTime? 1 : -1 },
  },
  {
    title: "查看对应知识",
    key: "view",
    scopedSlots: { customRender: "view" }
  },
];
export default {
  name: "userMessage",
  data() {
    return {
      columns2,
      columns1,
      loading: false,
      pagination: {
        defaultPageSize: 5,
        defaultCurrent: 1,
        total: 0,
      },
    };
  },
  components: {
    KnowledgeShowUser,
  },
  computed: {
    ...mapGetters(["commentResolevdList", "commentUnresolvedList", "commentAbandonList","currentKnowledgeInfo","userInfo"]),

    canEdit() {
      if (this.hotelEdit.length == 0) {
        return true;
      }
      return this.hotelEdit[this.hotelEdit.length - 1].status != "WAIT";
    }
  },
  async mounted() {
    await this.getUserCommentListByStatus({userId:this.userInfo.userId,status:"待解决"});
    await this.getUserCommentListByStatus({userId:this.userInfo.userId,status:"已解决"});
    await this.getUserCommentListByStatus({userId:this.userInfo.userId,status:"废弃"});
  },
  methods: {
    ...mapMutations(["set_currentKnowledgeId"]),
    ...mapActions(["getKnowledgeById","getUserCommentListByStatus"]),
    viewKnowledge(record){
      this.$refs.knowledgeShow.setModalVisible({knowledgeId:record.knowledgeId,fromMes:true})
    },
  }
};
</script>
<style lang="less">
.manageKnowledge-wrapper {
  .ant-row {
    margin-bottom: 30px;
  }
  .ant-tabs-bar {
    padding-left: 30px;
  }
}
.manageKnowledge-wrapper {
  padding: 50px;
  .chart {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-top: 20px;
  }
}
</style>