<template>
  <div>
    <x-header :left-options="{showBack: false}" title="slot:overwrite-title">帖子列表
      <div slot="right" @click="searchClick">
        <x-icon type="ios-search-strong" size="20"></x-icon>
      </div>
    </x-header>
    <search v-model="key" position="absolute" auto-scroll-to-top top="46px" @on-submit="onSubmit" ref="search" v-show="search"></search>
    <group>
      <button-tab v-model="tabIndex">
        <button-tab-item v-for="tab in tabs" :key="tab" @on-item-click="changeTab()"> {{tab.name}} </button-tab-item>
      </button-tab>
      <scroller lock-x scrollbar-y use-pullup height="-40" @on-pullup-loading="loadMore" ref="scroller" :pullup-config="pullConfig">
        <div class="box2">
          <panel :list="list" :type="type"></panel>
        </div>
      </scroller>

    </group>
  </div>
</template>

<script>
import { XHeader, Search, Panel, Scroller, ButtonTab, ButtonTabItem, Group, Cell } from 'vux'
export default {
  components: {
    Search,
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
      pullConfig: {
        content: '上拉加载更多',
        downContent: '松开进行加载',
        upContent: '上拉加载更多',
        loadingContent: '加载中...'
      },
      search: false,
      key: '',
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
    searchClick() {
      this.search = !this.search;
      this.$refs.search.setFocus();
    },
    onSubmit() {
      this.search = false;
      this.loadTopics(true);
    },
    changeTab() {
      this.tab = this.tabs[this.tabIndex].id;
      this.page = 1;
      this.loadTopics(true);
    },
    loadMore() {
      this.page++;
      this.loadTopics();
    },
    query() {
      var search = location.search; //获取url中"?"符后的字串
      var query = {};
      if (search.indexOf("?") != -1) {
        var str = search.substr(1);
        strs = str.split("&");
        for (var i = 0; i < strs.length; i++) {
          query[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
        }
      }
      return query;
    },
    getQueryString(name) {
      var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
      var r = window.location.search.substr(1).match(reg);
      if (r != null) return unescape(r[2]); return null;
    },
    loadTopics(clean) {
      if (clean) {
        this.list = [];
        this.$refs.scroller.reset({ top: 0 });
      }
      this.get('/qa/topic?code=' + this.getQueryString('code') + '&page=' + this.page + '&tab=' + this.tab + '&search=' + this.key, result => {
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
