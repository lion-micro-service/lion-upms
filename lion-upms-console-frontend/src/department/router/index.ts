import { createRouter,createWebHashHistory}  from 'vue-router'
const routes : Array<any> =[{
    path:'/',
    name:'部门',
    redirect:'/department/list'
},{
    path:'/department/list',
    name:'部门列表',
    component: () => import('@/department/views/List.vue'),
    meta: {keepAlive: true }
}
];
const route = createRouter({
    history: createWebHashHistory(),
    routes
})
export default route