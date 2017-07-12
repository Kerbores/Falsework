<template>
  <div>
    <group :title="topic.title">
      <card>
        <div slot="content" class="card-padding" v-html="topic.content"></div>
      </card>
    </group>
    <group title="回复列表">
      <card v-for="reply in replies" :key="reply">
        <div slot="content" class="weui-media-box weui-media-box_text card-padding">
          <div class="weui-media-box_appmsg">
            <div class="weui-media-box__hd">
              <img :src="reply.src" alt="" class="weui-media-box__thumb">
            </div>
            <div class="weui-media-box__bd">
              <h4 class="weui-media-box__title">{{reply.title}}</h4>
              <p class="weui-media-box__desc1" v-html="reply.desc"></p>
            </div>
          </div>
          <ul class="weui-media-box__info">
            <li class="weui-media-box__info__meta">{{reply.meta.source}}</li>
            <li class="weui-media-box__info__meta"></li>
            <li class="weui-media-box__info__meta weui-media-box__info__meta_extra"></li>
          </ul>
        </div>
      </card>
    </group>
  </div>
</template>

<script>
import { XHeader, Panel, Card, Group, Cell } from 'vux'

export default {
  components: {
    Panel,
    Card,
    XHeader,
    Group,
    Cell
  },
  data() {
    return {
      id: '',
      type: '5',
      topic: {},
      replies: []
    }
  },
  methods: {
    loadTopic() {
      this.get('/qa/detail/' + this.id, result => {
        this.topic = result.data.topic.data;
        result.data.topic.data.replies.forEach(item => {
          this.replies.push({
            src: item.author.avatar_url,
            title: item.author.loginname,
            desc: item.content,
            url: {
              replace: false
            },
            meta: {
              source: '发表于: ' + item.create_at_forread
            }
          });
        })
      })
    },
  },
  created() {
    this.id = this.$route.params.id;
  },
  mounted() {
    document.title = '帖子详情';
    this.loadTopic();
  }
}
</script>

<style>
.weui-media-box__desc1 {
  color: #999999;
  line-height: 1.2;
  overflow: scroll;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 0;
}

img {
  width: auto;
  max-width: 100%
}

.card-padding {
  padding: 15px;
  font-size: 12px;
}

.vux-demo {
  text-align: center;
}

.logo {
  width: 100px;
  height: 100px
}
</style>
