import Vue from 'vue';
import VueRouter, {RouteConfig} from 'vue-router';

Vue.use(VueRouter);

const routes : Array<RouteConfig> = [{
    path:'/department',
    name:'部门',
    redirect:'/department/list'
    },{
        path:'/department/list',
        name:'部门列表',
        component: () => import('@/department/views/List.vue'),
        meta: {keepAlive: true }
    }
];

const route = new VueRouter({
    mode:'history',
    base:process.env.BASE_URL,
    routes
})

export default route