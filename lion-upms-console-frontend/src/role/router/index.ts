import { createRouter,createWebHashHistory}  from 'vue-router'
const routes : Array<any> =[{
    path:'/',
    name:'角色',
    redirect:'/role/list'
},{
    path:'/role/list',
    name:'角色列表',
    component: () => import('@/role/views/List.vue'),
    meta: {keepAlive: true }
}
];
const route = createRouter({
    history: createWebHashHistory(),
    routes
})
export default route