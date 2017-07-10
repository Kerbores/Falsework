<template>
  <div>
    <router-link class="page-back"  :to="'/'">
      <i class="mintui mintui-back"></i>
    </router-link>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'app'
}
</script>

<style>
html,
body {
  background-color: #fafafa;
  -webkit-overflow-scrolling: touch;
  user-select: none;
}

a {
    background-color: transparent;
    -webkit-text-decoration-skip: objects;
    text-decoration: none;
}

.page-back {
    display: inline-block;
    top: 12px;
    left: 10px;
    position: absolute;
    width: 40px;
    height: 40px;
    text-align: center;
}

.page-back i {
    font-size: 24px;
    line-height: 40px;
}

#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

 .page-title {
    font-size: 20px;
    margin: 20px auto;
    text-align: center;
    display: block;
    line-height: 1;
  }
</style>
