<template>
  <div class="manageKnowledge-wrapper">
    <AuditKnowledge
      ref="audit"
    ></AuditKnowledge>
    <a-tabs>
      <a-tab-pane tab="尚未审核的信息" key="1">
        <a-table
        :columns="columns1"
        :data-source="this.auditList"
        :loading="loading"
        :pagination="pagination" 
        :rowKey="(record, index) => index"
        >
          <span slot="view" slot-scope="text,record">
            <a-button type="primary" size="small" style="margin-right:10px" @click="viewKnowledge(record)">查看</a-button>
          </span>
        </a-table>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>
<script>
import { mapGetters, mapMutations, mapActions } from "vuex";
import { message } from "ant-design-vue";
import AuditKnowledge from "@/views/manager/component/auditKnowledge";

import moment from "moment";

const columns1 = [
  {
    title: '知识ID',
    dataIndex: 'id',
    sorter: (a, b) => a.id-b.id,
  },
  {
    title: '标题',
    dataIndex: 'title',
    sorter: (a, b) => a.title.localeCompare(b.title),
  },
  {
    title: '内容',
    dataIndex: 'content',
    sorter: (a, b) => a.content.localeCompare(b.content),
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    sorter: (a, b) => { return a.createTime> b.createTime? 1 : -1 },
  },
  {
    title: '修改时间',
    dataIndex: 'modifyTime',
    sorter: (a, b) => { return a.modifyTime> b.modifyTime? 1 : -1 },
  },
  {
    title: "查看对应知识",
    key: "view",
    scopedSlots: { customRender: "view" }
  },
];

export default {
  name: "managerMessage",
  data() {
    return {
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
    AuditKnowledge,
  },
  computed: {
    ...mapGetters(["auditList", "auditTotalNum", "auditListLoading","currentKnowledgeInfo","userInfo"]),

  },
  async mounted() {
    await this.getAuditKnowledgePage();
  },
  methods: {
    ...mapMutations(["set_currentKnowledgeId"]),
    ...mapActions(["getKnowledgeById","getAuditKnowledgePage",]),
    viewKnowledge(record){
      this.$refs.audit.setModalVisible({knowledgeId:record.id})
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