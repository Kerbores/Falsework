<template>
  <div>
    <divider v-show="me.length <= 0">未绑定用户</divider>
    <x-button type="primary" action-type="button" v-show="me.length <= 0" @click.native="scan">扫码绑定</x-button>

    <panel header="个人信息" :list="me" :type="type" v-show="me.length > 0"></panel>

    <panel header="最近话题" :list="topics" type="5" v-show="topics.length > 0"></panel>

    <panel header="最近回帖" :list="replies" type="5" v-show="replies.length > 0"></panel>
  </div>
</template>

<script>
import { Panel, XButton, Divider, Group, Radio } from 'vux'
export default {
  components: {
    Divider,
    XButton,
    Panel,
    Group,
    Radio
  },
  data() {
    return {
      type: '5',
      me: [],
      topics: [],
      replies: []
    }
  },
  methods: {
    scan() {
      var self = this;
      this.$wechat.scanQRCode({
        needResult: 1,
        scanType: ["qrCode", "barCode"],
        success: function (res) {
          self.get('/qa/bind?token='+res.resultStr,result=>{
            console.log(result);
            self.loadInfo();
          });
        }
      });
    },
    loadInfo() {
      this.get('/qa/me', result => {
        console.log(result);
        this.me.push({
          src: result.data.me.data.avatar_url,
          title: result.data.me.data.loginname,
          desc: "积分: " + result.data.me.data.score,
          meta: {
            source: '发帖数: ' + result.data.me.data.recent_topics.length,
            date: '回帖数:' + result.data.me.data.recent_replies.length,
            other: '注册时间 ' + result.data.me.data.create_at_forread
          }
        });
        result.data.me.data.recent_topics.forEach(item => {
          this.topics.push({
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
          })
        });
        result.data.me.data.recent_replies.forEach(item => {
          this.replies.push({
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
          })
        });
      })
    }
  },
  created() {
    document.title = '个人信息';
    this.loadInfo();
  }
}
</script>
