<template>
  <a-card hoverable class="knowledgeCard" >
    <a-card-meta >
      <span slot="title" style="font-size:20px;font-weight: 700;letter-spacing:3px;">{{knowledge.title}}</span>
      <template slot="description">
        {{(knowledge.content).substring(0,75)}}
      </template>
    </a-card-meta>
  </a-card>

</template>
<script>
export default {
  name: "",
  props: {
    knowledge: {}
  },
  data() {
    return {};
  },
  computed:{
  },
  methods:{
    
  }
};
</script>

<style lang="less">
.knowledgeCard {
  margin:  1.1%;
  width: 31%;
  max-height: 200px;
  // display: flex;
  border-radius: 15px;
  padding:15px;
  .ant-card-meta-title{
  width: 100%;
  border-bottom-style:solid;
  border-bottom-color:rgb(188, 186, 186);
  border-bottom-width: 0.5px;
  height: 30%;
  }
  .ant-card-meta-description{
    width: 100%;
    height: 70%;
  }
}


.ant-card-hoverable:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.4);
}
</style>

