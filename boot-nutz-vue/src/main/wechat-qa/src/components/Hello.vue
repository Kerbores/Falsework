<template>
  <div>
    <x-header :left-options="{showBack: false}">帖子列表</x-header>
    <group>
      <button-tab v-model="tabIndex">
        <button-tab-item v-for="tab in tabs" :key="tab" @on-item-click="changeTab()"> {{tab.name}} </button-tab-item>
      </button-tab>
      <scroller lock-x scrollbar-y use-pullup :height="height" @on-pullup-loading="loadMore" ref="scroller">
        <div class="box2">
          <panel :list="list" :type="type"></panel>
        </div>
      </scroller>

    </group>
  </div>
</template>

<script>
import { XHeader, Panel, Scroller, ButtonTab, ButtonTabItem, Group, Cell } from 'vux'

export default {
  components: {
    ButtonTab,
    ButtonTabItem,
    Scroller,
    Panel,
    XHeader,
    Group,
    Cell
  },
  data() {
    return {
      height: '600px',
      page: 1,
      tab: '',
      type: '5',
      list: [],
      tabIndex: 0,
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
      footer: {
        title: '查看更多',
        url: 'http://vux.li'
      }
    }
  },
  methods: {
    changeTab() {
      this.tab = this.tabs[this.tabIndex].id;
      this.page = 1;
      this.loadTopics(true);
    },
    loadMore() {
      this.page++;
      this.loadTopics();
    },
    loadTopics(clean) {
      if (clean) {
        this.list = [];
      }
      this.get('/qa/topic?page=' + this.page + '&tab=' + this.tab, result => {
        result.data.topics.data.forEach(item => {
          this.list.push({
            src: item.author.avatar_url,
            title: item.author.loginname,
            desc: item.title,
            url: {
              path: '/detail/' + item.id,
              replace: false
            },
            meta: {
              source: '发表于: ' + item.create_at_forread,
              date: '点击: ' + item.visit_count + ' 回复: ' + item.reply_count,
              other: item.good ? '精华' : '' + item.top ? '置顶' : '',
            }
          });
          this.$refs.scroller.donePullup();
          if (result.data.topics.data.length == 0) {
            this.$refs.scroller.disablePullup();
          }
        })
      })
    },
  },
  mounted() {
    this.height = (document.documentElement.clientHeight - 100) + 'px';
    this.loadTopics();
  }
}
</script>

<style>
.vux-demo {
  text-align: center;
}

.logo {
  width: 100px;
  height: 100px
}
</style>
