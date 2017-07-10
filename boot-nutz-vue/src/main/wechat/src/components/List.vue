<template>
  <div class="page-navbar">
    <div class="page-title">帖子列表</div>
    <mt-navbar class="page-part" v-model="selected">
      <mt-tab-item v-for="tab in tabs" :key="tab" :id="tab.id">{{tab.name}}</mt-tab-item>
    </mt-navbar>
    <mt-tab-container v-model="selected" v-infinite-scroll="loadMore" infinite-scroll-disabled="loading" infinite-scroll-distance="10">
      <mt-tab-container-item id="0">
        <mt-cell v-for="n in 10" :key="n" is-link to="https://mint-ui.github.io">
          <div class="cell-content">
            <h4 class="cell-content-title">wendal:</h4>
            <div class="cell-content-summary">
              内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容
            </div>
            <div class="cell-content-footer">时间: 10分钟前 点击: 10 回复: 8</div>
          </div>
          <img slot="icon" src="https://nutz.cn/yvr/u/wendal/avatar" width="50" height="50">
        </mt-cell>
      </mt-tab-container-item>
      <mt-tab-container-item id="1">
        <mt-cell v-for="n in 4" :key="n" title="C2" label="跳转到 https://mint-ui.github.io" is-link to="https://mint-ui.github.io"></mt-cell>
      </mt-tab-container-item>
      <mt-tab-container-item id="2">
        <mt-cell v-for="n in 6" :key="n" title="C3" label="跳转到 https://mint-ui.github.io" is-link to="https://mint-ui.github.io"></mt-cell>
      </mt-tab-container-item>
      <mt-tab-container-item id="3">
        <mt-cell v-for="n in 6" :key="n" title="C3" label="跳转到 https://mint-ui.github.io" is-link to="https://mint-ui.github.io"></mt-cell>
      </mt-tab-container-item>
      <mt-tab-container-item id="4">
        <mt-cell v-for="n in 6" :key="n" title="C3" label="跳转到 https://mint-ui.github.io" is-link to="https://mint-ui.github.io"></mt-cell>
      </mt-tab-container-item>
    </mt-tab-container>
  </div>
</template>
<style>
.cell-content {
  margin-left: 10px;
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
  font-size: 12px;
  text-indent: 24px;
  margin: 2px
}
</style>

<script>
export default {
  name: 'page-navbar',
  data() {
    return {
      page: 1,
      limit: 15,
      key: '',
      topics: [],
      tabs: [
        {
          id: '0',
          name: '全部'
        },
        {
          id: '1',
          name: '问答'
        },
        {
          id: '2',
          name: '新闻'
        }, {
          id: '3',
          name: '分享'
        }, {
          id: '4',
          name: '招聘'
        }
      ],
      selected: '0'
    };
  },
  methods: {
    loadMore() {
      this.loading = true;
      console.log('loading')
      setTimeout(() => {
        this.loading = false;
      }, 2500);
    }
  },
  mounted() {

  }
};
</script>
