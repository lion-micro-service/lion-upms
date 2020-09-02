import Vue from 'vue';
import VueRouter, {RouteConfig} from 'vue-router';

Vue.use(VueRouter);

const routes : Array<RouteConfig> = [{
    path:'/user',
    name:'用户',
    redirect:'/user/list'
    },{
        path:'/user/list',
        name:'用户列表',
        component: () => import('@/user/views/List.vue'),
        meta: {keepAlive: true }
    },{
        path:'/user/add',
        name:'用户新增',
        component: () => import('@/user/views/AddOrUpdate.vue'),
        meta: {keepAlive: false }
    }
];

const route = new VueRouter({
    mode:'history',
    base:process.env.BASE_URL,
    routes
})

export default route