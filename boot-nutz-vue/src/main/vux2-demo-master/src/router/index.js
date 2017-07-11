import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import Detail from '@/components/Detail'
import GroupCell from '@/components/GroupCell'

Vue.use(Router)

export default new Router({
    routes: [{
            path: '/',
            name: 'Hello',
            component: Hello
        },
        {
            path: '/detail/:id',
            name: 'Detail',
            component: Detail
        },
        {
            path: '/GroupCell',
            name: 'GroupCell',
            component: GroupCell
        }
    ]
})