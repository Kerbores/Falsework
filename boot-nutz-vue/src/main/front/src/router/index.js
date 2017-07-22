import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    routes: [{
            path: '/',
            redirect: '/login'
        },
        {
            path: '/readme',
            component: resolve => require(['../components/common/Home.vue'], resolve),
            children: [{
                    path: '/',
                    component: resolve => require(['../components/page/Readme.vue'], resolve)
                },
                {
                    path: '/user',
                    component: resolve => require(['../components/page/User.vue'], resolve)
                },
                {
                    path: '/role',
                    component: resolve => require(['../components/page/Role.vue'], resolve)
                },
                {
                    path: '/permission',
                    component: resolve => require(['../components/page/Permission.vue'], resolve)
                },
                {
                    path: '/group',
                    component: resolve => require(['../components/page/Group.vue'], resolve)
                },
                {
                    path: '/codebook',
                    component: resolve => require(['../components/page/Codebook.vue'], resolve)
                },
                {
                    path: '/settings',
                    component: resolve => require(['../components/page/Settings.vue'], resolve)
                },
                {
                    path: '/datasource',
                    component: resolve => require(['../components/page/DataSource.vue'], resolve)
                },
                {
                    path: '/runtime',
                    component: resolve => require(['../components/page/Runtime.vue'], resolve)
                },
                {
                    path: '/trace',
                    component: resolve => require(['../components/page/Trace.vue'], resolve)
                },
                {
                    path: '/log',
                    component: resolve => require(['../components/page/Log.vue'], resolve)
                },
                {
                    name: 'organization',
                    path: '/organization',
                    component: resolve => require(['../components/page/Branch.vue'], resolve)
                },
                {
                    name: 'oaoe',
                    path: '/organization/aoe/:id',
                    component: resolve => require(['../components/page/BranchAddOrEdit.vue'], resolve)
                }
            ]
        },
        {
            path: '/login',
            component: resolve => require(['../components/page/Login.vue'], resolve)
        },
    ]
})