// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import FastClick from 'fastclick'
import VueRouter from 'vue-router'
import App from './App'
import router from './router'
import axios from 'axios';
import { WechatPlugin } from 'vux'
import { ToastPlugin } from 'vux'
Vue.use(ToastPlugin)
Vue.use(WechatPlugin)

Vue.config.productionTip = false

global.baseUrl = process.env.NODE_ENV == "development" ? 'api' : '';

Vue.prototype.requestFail = function(error, message) {
    if (error.response && error.response.status == 403) {
        router.push('/login');
        return;
    }
    if (error.response) {
        message({
            text: '请求错误,状态码: ' + error.response.status,
            type: 'warn'
        });
    } else {
        console.log('Error', error.message);
    }
    console.log(error.config);
}
Vue.prototype.bizFail = function(reason) {
    this.$vux.toast.show({
        text: '业务错误,错误原因:' + reason,
        type: 'warn'
    })
}
Vue.prototype.get = function(url, success, bizFail) { //全局get请求函数
    const message = this.$vux.toast;
    const error = this.requestFail;
    axios.get(baseUrl + url).then(resp => {
        console.log(resp)
        if (resp.status == 200 && resp.data.operationState == 'SUCCESS') {
            success(resp.data);
        } else {
            bizFail ? bizFail(resp) : this.bizFail(resp.data.data.reason);
        }
    }).catch(e => {
        error(e, message);
    });
}
Vue.prototype.postBody = function(url, body, success, bizFail) { //全局post请求函数
    const message = this.$vux.toast;
    const error = this.requestFail;
    axios.post(baseUrl + url, body)
        .then(resp => {
            if (resp.status == 200 && resp.data.operationState == 'SUCCESS') {
                success(resp.data);
            } else {
                bizFail ? bizFail(resp) : this.bizFail(resp.data.data.reason);
            }
        })
        .catch(e => {
            error(e, message);
        });
}
Vue.prototype.postForm = function(url, body, success, bizFail) { //全局post请求函数
    const message = this.$vux.toast;
    const error = this.requestFail;
    var params = new URLSearchParams();
    for (var key in body) {
        params.append(key, body[key]);
    }
    axios.post(baseUrl + url, params)
        .then(resp => {
            if (resp.status == 200 && resp.data.operationState == 'SUCCESS') {
                success(resp.data);
            } else {
                bizFail ? bizFail(resp) : this.bizFail(resp.data.data.reason);
            }
        })
        .catch(e => {
            error(e, message);
        });
}
Vue.prototype.upload = function(url, body, success, bizFail) { //全局文件上传请求函数
    const message = this.$vux.toast;
    const error = this.requestFail;
    var params = new FormData();
    for (var key in body) {
        params.append(key, body[key]);
    }
    axios.post(baseUrl + url, params, {
            headers: { 'Content-Type': 'multipart/form-data' }
        })
        .then(resp => {
            if (resp.status == 200 && resp.data.operationState == 'SUCCESS') {
                success(resp.data);
            } else {
                bizFail ? bizFail(resp) : this.bizFail(resp.data.data.reason);
            }
        })
        .catch(e => {
            error(e, message);
        });
}

Vue.use(VueRouter)

FastClick.attach(document.body)

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
    router,
    created() {
        this.postForm('/config', {
            url: location.href.split('#')[0]
        }, result => {
            Vue.wechat.config(result.data.config);
            Vue.wechat.error(function(res) {
                console.log(res);
            });
            Vue.wechat.ready(function() {
                console.log('wechat config success...');
            });
        })
    },
    render: h => h(App)
}).$mount('#app-box')