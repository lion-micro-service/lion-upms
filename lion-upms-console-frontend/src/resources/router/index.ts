import Vue from 'vue';
import VueRouter, {RouteConfig} from 'vue-router';

Vue.use(VueRouter);

const routes : Array<RouteConfig> = [{
    path:'/resources',
    name:'资源',
    redirect:'/resources/list'
    },{
        path:'/resources/list',
        name:'资源列表',
        component: () => import('@/resources/views/List.vue'),
        meta: {keepAlive: true }
    }
];

const route = new VueRouter({
    mode:'history',
    base:process.env.BASE_URL,
    routes
})

export default route