import { createRouter,createWebHashHistory}  from 'vue-router'
const routes : Array<any> =[{
    path:'/',
    name:'资源',
    redirect:'/resources/list'
},{
    path:'/resources/list',
    name:'资源列表',
    component: () => import('@/resources/views/List.vue'),
    meta: {keepAlive: true }
}
];
const route = createRouter({
    history: createWebHashHistory(),
    routes
})
export default route