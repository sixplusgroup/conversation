<template>
  <div class="reviewDetail">
    <reviewDiagram style="position:fixed"/>
    <reviewChatlog/>
  </div>
</template>

<script>
import { mapGetters, mapActions, mapMutations } from "vuex";
import reviewChatlog from '@/views/review/component/reviewChatlog';
import reviewDiagram from '@/views/review/component/reviewDiagram';
export default {
  name:"reviewDetail",
  components: {
    reviewDiagram,
    reviewChatlog,
  },
  data() {
    return {
      
    };
  },
  async mounted() {
    await this.getReviewDetail(this.currentReviewId);
  },
  
  computed: {
    ...mapGetters(["currentReviewId"]),
  },
  methods: {
    ...mapMutations([ "set_reviewListLoading"]),
    ...mapActions([
      "getReviewDetail",
    ]),
  }
}
</script>

<style>

</style>