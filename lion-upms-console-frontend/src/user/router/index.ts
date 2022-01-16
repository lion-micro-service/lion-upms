import { createRouter,createWebHashHistory}  from 'vue-router'
const routes : Array<any> =[{
    path:'/',
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
const route = createRouter({
    history: createWebHashHistory(),
    routes
})
export default route