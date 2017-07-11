<template>
  <div class="page-cell">
    <div class="page-title">帖子详情</div>
    <mt-cell>
      <div class="cell-content">
        <h4 class="cell-content-title">{{topic.title}}:</h4>
        <div class="cell-content-summary" v-html="topic.content">
        </div>
        <div class="cell-content-footer">发布于: {{topic.create_at_forread}} 点击: {{topic.visit_count}} 回复: {{topic.reply_count}}</div>
      </div>
      <img slot="icon" :src="topic.author && topic.author.avatar_url" width="50" height="50">
    </mt-cell>
    <mt-cell title="" value="回复列表"></mt-cell>
    <mt-cell v-for="reply in topic.replies" :key="reply">
      <div class="cell-content">
        <h4 class="cell-content-title">{{reply.author.loginname}}:</h4>
        <div class="cell-content-summary" v-html="reply.content">
        </div>
        <div class="cell-content-footer">发布于: {{reply.create_at_forread}} </div>
      </div>
      <img slot="icon" :src="reply.author && reply.author.avatar_url" width="50" height="50">
    </mt-cell>
  </div>
</template>
<script>
export default {
  data() {
    return {
      topic: {}
    }
  },
  created() {
    this.topic.id = this.$route.params.id;
  },
  mounted() {
    this.get('/qa/detail/' + this.topic.id, result => {
      this.topic = result.data.topic.data;
    })
  }
}
</script>
<style>
.mint-cell-title {
  width: 50px;
}

.mint-cell-value {
  min-width: 85%;
}

.cell-content {
  margin-left: 10px;
  min-width: 100%;
}

.cell-content-title {
  color: #000;
  font-weight: 800;
  margin: 5px
}

.cell-content-footer {
  font-size: 8px;
  color: #AAA;
  text-align: right
}

.cell-content-summary {
  width: 100%;
  font-size: 12px;
  text-indent: 24px;
  margin: 2px
}
</style>


