<template>
  <div class="manageKnowledge-wrapper">
    <KnowledgeShowUser
      ref="knowledgeShow"
    ></KnowledgeShowUser>
    <KnowledgeShowMarket
      ref="marketKnowledgeShow"
    ></KnowledgeShowMarket>
    <KnowledgeShowManager
      ref="managerKnowledgeShow"
    ></KnowledgeShowManager>
    <a-tabs>
      <a-tab-pane tab="尚未处理的信息" key="1">
        <a-table
        :columns="columns1"
        :data-source="this.commentUnresolvedListMarket"
        :loading="loading"
        :pagination="pagination" 
        :rowKey="(record, index) => index"
        >
          <span slot="view" slot-scope="text,record">
            <a-button type="primary" size="small" style="margin-right:10px" @click="viewKnowledge(record)">查看</a-button>
            <a-button type="info" size="small" style="margin-right:10px" @click="abandon(record)">废弃</a-button>
            <a-button type="primary" size="small" style="margin-right:10px" @click="modify(record)">修改</a-button>
          </span>
          <a-tag :color="typeColor(text)" slot="type" slot-scope="text">{{text==="审核人员反馈"?'管理员':'用户'}}</a-tag>
        </a-table>
      </a-tab-pane>
      <a-tab-pane tab="处理好的信息" key="2">
        <a-table
        :columns="columns2"
        :data-source="this.commentResolvedListMarket"
        :loading="loading"
        :pagination="pagination" 
        :rowKey="(record, index) => index"
        >
          <span slot="view" slot-scope="text,record">
            <a-button type="primary" size="small" style="" @click="viewKnowledge(record)">查看</a-button>
          </span>
          <a-tag :color="typeColor(text)" slot="type" slot-scope="text">{{text==="审核人员反馈"?'管理员':'用户'}}</a-tag>
        </a-table>
      </a-tab-pane>
      <a-tab-pane tab="忽视的信息" key="3">
        <a-table
        :columns="columns1"
        :data-source="this.commentAbandonListMarket"
        :loading="loading"
        :pagination="pagination" 
        :rowKey="(record, index) => index"
        >
          <span slot="view" slot-scope="text,record">
            <a-button type="primary" size="small" style="" @click="viewKnowledge(record)">查看</a-button>
          </span>
          <a-tag :color="typeColor(text)" slot="type" slot-scope="text">{{text==="审核人员反馈"?'管理员':'用户'}}</a-tag>
        </a-table>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>
<script>
import { mapGetters, mapMutations, mapActions } from "vuex";
import { message } from "ant-design-vue";
import KnowledgeShowUser from "@/views/user/knowledgeShow";
import KnowledgeShowMarket from "@/views/market/knowledgeShow";
import KnowledgeShowManager from "@/views/manager/component/knowledgeShow";


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
    title: '评论者',
    dataIndex: 'type',
    sorter: (a, b) => a.type-b.type,//manager:0,client:1
    scopedSlots: { customRender: "type" },
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
    title: '评论者',
    dataIndex: 'type',
    sorter: (a, b) => a.type-b.type,//manager:0,client:1
    scopedSlots: { customRender: "type" },
  },
  {
    title: "查看对应知识",
    key: "view",
    scopedSlots: { customRender: "view" }
  },
];
export default {
  name: "marketMessage",
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
    KnowledgeShowMarket,
    KnowledgeShowManager,
  },
  computed: {
    ...mapGetters(["commentUnresolvedListMarket", "commentResolvedListMarket", "commentAbandonListMarket","currentKnowledgeInfo","userInfo"]),

  },
  async mounted() {
    await this.getCommentListByStatus("待解决");
    await this.getCommentListByStatus("已解决");
    await this.getCommentListByStatus("废弃");
  },
  methods: {
    ...mapMutations(["set_currentKnowledgeId"]),
    ...mapActions(["getKnowledgeById","getCommentListByStatus","abandonComment","correctKnowledge"]),
    typeColor(status) {
      const trans = {
        "使用人员反馈": "blue",
        "审核人员反馈": "orange"
      };
      return trans[status];
    },
    viewKnowledge(record){
      this.$refs.managerKnowledgeShow.setModalVisible({knowledgeId:record.knowledgeId})
    },
    modify(record){
      this.$refs.marketKnowledgeShow.showDrawer({knowledgeId:record.knowledgeId,commentId:record.id,fromMes:true})
    },
    async abandon(record){
      const _record=record;
      const that = this;
      this.$confirm({
        title: '你要废弃这个评论吗',
        content: h => <div style="color:red;">删除后将不复存在</div>,
        onOk:async() => {
          console.log(_record)
          await that.abandonComment(_record.id)
          console.log('OK');
          console.log(_record.id)
        },
        onCancel() {
          console.log('Cancel');
        },
        class: 'test',
      });
    }
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