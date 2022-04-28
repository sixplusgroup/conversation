<template>
  <a-modal
      :visible="editTags_Visible"
      title="编辑标签"
      cancelText="取消"
      okText="确定"
      @cancel="cancelTag"
      @ok="handleSubmitTags"
    >
    <!--添加  -->
      <div class="input-Container" style="display:flex">
          <div style="width: 320px; text-align: center;margin-left:15px;margin-right:10px;height:32px;margin-bottom:15px">
              <input class="addTagName" type="text" style="border-radius:8px;height:100%;width:320px" v-model="newtag_Name" placeholder="  请输入标签名称">
          </div>
          <div style="width: 150px; text-align: center; ">
              <a-button type="primary" @click="addTag">
                  <a-icon type="plus" />添加标签 
              </a-button>
          </div>
      </div>
    <!-- 删除和修改 -->
      <a-table 
      :columns="columns" 
      :dataSource="tagList"
      :pagination="pagination" 
      :rowKey="record=>record.id" 
      bordered>
        <span slot="name" slot-scope="text,record">
          <a-input v-model=record.tag_name :disabled="!record.tagEditable" :ghost=true />
        </span>
        <span slot="edit" slot-scope="record">
          <a-button type="primary" size="small" style="margin-right:10px" @click="doModifyTag(record)">修改</a-button>
          <a-button type="danger" size="small" style="margin-right:10px" @click="doDeleteTags(record)">删除</a-button>
          <a-button type="primary" size="small" @click="saveModify(record)" v-if="record.tagEditable">保存</a-button>
        </span>
      </a-table>
    </a-modal>
</template>

<script>
import { message } from 'ant-design-vue';
import { mapGetters, mapActions, mapMutations } from "vuex";

const columns= [
  {
    title: "标签名",
    dataIndex: "tag_name",
    scopedSlots: { customRender: "name" },

    customHeaderCell: column => {
      return {
        style: {
          'min-width': "250px",
        }
      };
    }
  },
  {
    title: "修改",
    key: "edit",
    scopedSlots: { customRender: "edit" }
  },
];
export default {
  name:"editTagsAll",
    data() {
    return {
      editTags_Visible:false,
      newtag_Name:"",
      columns,
      pagination: {
        pageSize: 4, //每页中显示10条数据
        showSizeChanger: true
      },
      currentTagId:0,
      tagModify:false,
      modifyTag:{
        id:-1,
        tag_name:"112233",
      },

    };
  },
  async mounted() {
    // await this.getAllTags();
  },
  computed: {
    ...mapGetters(["tagList"]),

  },
  methods: {
    ...mapActions([
      "getAllTags",
      "createTags",
      "deleteTags",
      "modifyTags"
    ]),
// 添加标签
    editTag(){
      this.editTags_Visible=true;
      console.log(this.tagList)
    },
    async addTag(newtag_Name){
      if(this.newtag_Name===""){
        message.error("请输入标签名")
      }else{
        await this.createTags({tag_name:this.newtag_Name,id:-1}) 
        this.newtag_Name=""
      }
    },
    cancelTag(){
      this.editTags_Visible=false;
      this.newtag_Name="";
    },
    handleSubmitTags(){
      this.editTags_Visible=false;
      this.newtag_Name="";
    },
    doModifyTag(record){
      this.$store.state.user.tagList.forEach(i => {
        if (i.id === record.id) {
          i.tagEditable = true;
        }
      });
      this.modifyTag.tag_name=record.tag_name 
      this.modifyTag.id=record.id
      this.updateMessage()
    },
    async saveModify(record){
      console.log("看一下record")
      console.log(record)
      this.modifyTag.tag_name=record.tag_name
      this.updateMessage()
      console.log("修改的要保存了")
      console.log(this.modifyTag)
      await this.modifyTags(this.modifyTag)
      this.tagModify=false
    },
    async doDeleteTags(text){
      const that=this;
      this.$confirm({
        title: '你要删除这个标签吗',
        content: h => <div style="color:red;">删除后将不复存在</div>,
        onOk:async ()=> {
          await this.deleteTags(text.id)
          console.log('OK');
          console.log(text.id)
        },
        onCancel() {
          console.log('Cancel');
        },
        class: 'test',
      });
      
    },
    updateMessage() {
      const that=this
      this.$nextTick(function () {
        console.log("数据同步中")
        console.log(that.modifyTag);
        that.editTags_Visible;
      })
    }
  }
}
</script>

<style>

</style>