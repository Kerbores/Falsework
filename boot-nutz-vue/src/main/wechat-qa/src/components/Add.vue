<template>
  <div>
    <group title="发表新帖">
      <x-input placeholder="标题" v-model="title"></x-input>
      <x-textarea :max="200" v-model="content" name="description" placeholder="请描述你的问题"></x-textarea>
      <x-button type="primary" action-type="button" @click.native="submit">提交</x-button>
    </group>
  </div>
</template>
<script>
import { XTextarea, Group, XButton, XInput } from 'vux'
export default {
  data() {
    return {
      title: '',
      content: ''
    }
  },
  components: {
    XButton,
    XTextarea,
    Group,
    XInput
  },
  methods: {
    submit() {
      this.postForm('/qa/add', {
        title: this.title,
        content: this.content
      }, result => {
        this.$router.push({ name: 'List' });
      })
    }
  },
  mounted(){
    document.title = '发表新帖';
  }
}
</script>
