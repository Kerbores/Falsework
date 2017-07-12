import Vue from 'vue'
import Router from 'vue-router'
import List from '@/components/List'
import Detail from '@/components/Detail'
import Me from '@/components/Me'
import Add from '@/components/Add'
import Reply from '@/components/Reply'

Vue.use(Router)

export default new Router({
    routes: [{
        path: '/',
        name: 'List',
        component: List
    }, {
        path: '/detail/:id',
        name: 'Detail',
        component: Detail
    }, {
        path: '/me',
        name: 'Me',
        component: Me
    }, {
        path: '/add',
        name: 'Add',
        component: Add
    }, {
        path: '/reply',
        name: 'Reply',
        component: Reply
    }]
})