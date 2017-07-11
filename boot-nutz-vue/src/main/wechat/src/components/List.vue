<template>
  <div class="page-navbar">
    <div class="page-title">帖子列表</div>
    <mt-navbar class="page-part" v-model="tab">
      <mt-tab-item v-for="tab in tabs" :key="tab" :id="tab.id">{{tab.name}}</mt-tab-item>
    </mt-navbar>
    <mt-tab-container v-model="tab">
      <mt-tab-container-item id="">
        <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
          <mt-loadmore :bottom-method="loadBottom" @bottom-status-change="handleBottomChange" :bottom-all-loaded="allLoaded" ref="loadmore_">
            <mt-cell v-for="topic in topics" :key="topic" is-link :to="{ name: 'Detail',params:{id:topic.id} }">
              <div class="cell-content">
                <h4 class="cell-content-title">{{topic.author.loginname}}:</h4>
                <div class="cell-content-summary">
                  {{topic.title}}
                </div>
                <div class="cell-content-footer">发布于: {{topic.create_at_forread}} 点击: {{topic.visit_count}} 回复: {{topic.reply_count}}</div>
              </div>
              <img slot="icon" :src="topic.author.avatar_url" width="50" height="50">
            </mt-cell>
          </mt-loadmore>
        </div>
      </mt-tab-container-item>
      <mt-tab-container-item id="ask">
        <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
          <mt-loadmore :bottom-method="loadBottom" @bottom-status-change="handleBottomChange" :bottom-all-loaded="allLoaded" ref="loadmore_ask">
            <mt-cell v-for="topic in topics" :key="topic" is-link :to="{ name: 'Detail',params:{id:topic.id} }">
              <div class="cell-content">
                <h4 class="cell-content-title">{{topic.author.loginname}}:</h4>
                <div class="cell-content-summary">
                  {{topic.title}}
                </div>
                <div class="cell-content-footer">发布于: {{topic.create_at_forread}} 点击: {{topic.visit_count}} 回复: {{topic.reply_count}}</div>
              </div>
              <img slot="icon" :src="topic.author.avatar_url" width="50" height="50">
            </mt-cell>
          </mt-loadmore>
        </div>
      </mt-tab-container-item>
      <mt-tab-container-item id="news">
        <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
          <mt-loadmore :bottom-method="loadBottom" @bottom-status-change="handleBottomChange" :bottom-all-loaded="allLoaded" ref="loadmore_news">
            <mt-cell v-for="topic in topics" :key="topic" is-link :to="{ name: 'Detail',params:{id:topic.id} }">
              <div class="cell-content">
                <h4 class="cell-content-title">{{topic.author.loginname}}:</h4>
                <div class="cell-content-summary">
                  {{topic.title}}
                </div>
                <div class="cell-content-footer">发布于: {{topic.create_at_forread}} 点击: {{topic.visit_count}} 回复: {{topic.reply_count}}</div>
              </div>
              <img slot="icon" :src="topic.author.avatar_url" width="50" height="50">
            </mt-cell>
          </mt-loadmore>
        </div>
      </mt-tab-container-item>
      <mt-tab-container-item id="share">
        <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
          <mt-loadmore :bottom-method="loadBottom" @bottom-status-change="handleBottomChange" :bottom-all-loaded="allLoaded" ref="loadmore_share">
            <mt-cell v-for="topic in topics" :key="topic" is-link :to="{ name: 'Detail',params:{id:topic.id} }">
              <div class="cell-content">
                <h4 class="cell-content-title">{{topic.author.loginname}}:</h4>
                <div class="cell-content-summary">
                  {{topic.title}}
                </div>
                <div class="cell-content-footer">发布于: {{topic.create_at_forread}} 点击: {{topic.visit_count}} 回复: {{topic.reply_count}}</div>
              </div>
              <img slot="icon" :src="topic.author.avatar_url" width="50" height="50">
            </mt-cell>
          </mt-loadmore>
        </div>
      </mt-tab-container-item>
      <mt-tab-container-item id="job">
        <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
          <mt-loadmore :bottom-method="loadBottom" @bottom-status-change="handleBottomChange" :bottom-all-loaded="allLoaded" ref="loadmore_job">
            <mt-cell v-for="topic in topics" :key="topic" is-link :to="{ name: 'Detail',params:{id:topic.id} }">
              <div class="cell-content">
                <h4 class="cell-content-title">{{topic.author.loginname}}:</h4>
                <div class="cell-content-summary">
                  {{topic.title}}
                </div>
                <div class="cell-content-footer">发布于: {{topic.create_at_forread}} 点击: {{topic.visit_count}} 回复: {{topic.reply_count}}</div>
              </div>
              <img slot="icon" :src="topic.author.avatar_url" width="50" height="50">
            </mt-cell>
          </mt-loadmore>
        </div>
      </mt-tab-container-item>
    </mt-tab-container>
  </div>
</template>
<style>
.page-loadmore-wrapper {
  margin-top: -1px;
  overflow: scroll;
}

.mint-cell-title {
  width: 50px;
}

.mint-cell-value.is-link {
  min-width: 80%;
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

<script>
export default {
  name: 'page-navbar',
  data() {
    return {
      page: 0,
      limit: 15,
      key: '',
      allLoaded: false,
      bottomStatus: '',
      wrapperHeight: 0,
      topics: [],
      tab: '',
      tabs: [
        {
          id: '',
          name: '全部'
        },
        {
          id: 'ask',
          name: '问答'
        },
        {
          id: 'news',
          name: '新闻'
        }, {
          id: 'share',
          name: '分享'
        }, {
          id: 'job',
          name: '招聘'
        }
      ],
    };
  },
  watch: {
    tab() {
      this.page = 1;
      this.loadTopics();
    }
  },
  methods: {
    handleBottomChange(status) {
      this.bottomStatus = status;
    },
    loadTopics() {
      this.get('/qa/topic?page=' + this.page + '&tab=' + this.tab, result => {
        this.topics = result.data.topics.data;
      })
    },
    loadBottom() {
      this.page++;
      this.get('/qa/topic?page=' + this.page + '&tab=' + this.tab, result => {
        result.data.topics.data.forEach(item=>{
          this.topics.push(item);
        })
        this.$refs['loadmore_' + this.tab].onBottomLoaded();
      })
    }
  },
  mounted() {
    this.wrapperHeight = document.documentElement.clientHeight - this.$refs.wrapper.getBoundingClientRect().top;
    this.loadTopics();
  }
};
</script>
