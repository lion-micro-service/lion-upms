import Vue from 'vue';
import VueRouter, {RouteConfig} from 'vue-router';

Vue.use(VueRouter);

const routes : Array<RouteConfig> = [{
    path:'/role',
    name:'角色',
    redirect:'/role/list'
    },{
        path:'/role/list',
        name:'角色列表',
        component: () => import('@/role/views/List.vue'),
        meta: {keepAlive: true }
    }
];

const route = new VueRouter({
    mode:'history',
    base:process.env.BASE_URL,
    routes
})

export default route