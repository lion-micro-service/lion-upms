<template>
    <div>
        <a-card class="card" :bordered="false">
            <a-form-model layout="inline" ref="from" :model="searchModel" >
                <a-row>
                    <a-col :span="6">
                        <a-form-model-item label="登陆账号" prop="username" ref="username" >
                            <a-input placeholder="请输入登陆账号" v-model="searchModel.username"/>
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="6">
                        <a-form-model-item label="姓名" prop="name" ref="name" >
                            <a-input placeholder="请输入姓名" v-model="searchModel.name"/>
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="6">
                        <a-form-model-item label="年龄" prop="age" ref="age" >
                            <a-input-number placeholder="请输入年龄" v-model="searchModel.age"/>
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="6">
                        <a-form-model-item label="邮箱" prop="email" ref="email" >
                            <a-input placeholder="请输入邮箱" v-model="searchModel.email"/>
                        </a-form-model-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col :span="6">
                        <a-form-model-item label="生日" prop="birthday" ref="birthday" >
                            <a-date-picker placeholder="请输入生日" v-model="searchModel.birthday" />
                        </a-form-model-item>
                    </a-col>
                </a-row>
                <a-row >
                    <a-col :span="24" style="text-align:right;">
                        <a-form-item>
                            <a-button type="primary" icon="search" @click="search()">查询</a-button>
                            <a-button type="primary" icon="file-add" @click="add()">添加</a-button>
                            <a-button type="danger" icon="delete" @click="del()">删除</a-button>
                        </a-form-item>
                    </a-col>
                </a-row>
            </a-form-model>
        </a-card>

        <a-card :bordered="false">
            <a-table :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }" rowKey="id" :columns="columns" :dataSource="data" :loading="loading" :pagination="paginationProps">
                <span slot="action" slot-scope="text, record">
                    <a-button icon="edit" size="small" @click="edit(record.id)">修改</a-button>
                    <a-button type="danger" icon="delete" size="small" @click='del(record.id)'>删除</a-button>
                </span>
            </a-table>
        </a-card>
    </div>
</template>

<script lang="ts">
    import {Component, Emit, Inject, Model, Prop, Provide, Vue, Watch} from 'vue-property-decorator';
    import axios from "@lion/lion-front-core/src/network/axios";
    import { message } from 'ant-design-vue';
    import qs from 'qs';
    @Component({})
    export default class List extends Vue{
        private create():void {
        }

        private searchModel : any ={
            pageNumber:1,
            pageSize:10
        }

        private selectedRowKeys:Array<number> = [];

        private data:Array<any> = [];
        private loading:boolean=false;
        //private x : number = 1500;
        //private y : number = 300;

        private columns :Array<any> = [
            { title: '姓名', dataIndex: 'name', key: 'name' },
            { title: '邮箱', dataIndex: 'email', key: 'email'},
            { title: '年龄', dataIndex: 'age', key: 'age' },
            { title: '操作', key: 'action', scopedSlots: { customRender: 'action' },width: 180,}
        ];

        private paginationProps:any={
            showSizeChanger: false,
            showQuickJumper: true,
            hideOnSinglePage:false,
            pageSizeOptions:['10', '20', '30', '40','50','60','70','80','90','100'],
            total:0,
            current:1,
            pageSize:10,
            showSizeChange: (pageNumber:number, pageSize: number)=>this.paginationSearch(pageNumber,pageSize),
            onChange: (pageNumber:number, pageSize: number)=>this.paginationSearch(pageNumber,pageSize),
        };

        // private mounted() :void{
        //     this.search();
        // }

        private paginationSearch(pageNumber:number, pageSize: number):void{
            this.searchModel.pageNumber=pageNumber;
            this.searchModel.pageSize=pageSize;
            this.search();
        }

        private search():void{
            this.loading=true;
            axios.get("/upms/user/console/list",{params:this.searchModel})
                .then((data)=>{
                        this.data=data.data.list;
                        this.paginationProps.total=Number((Object(data)).totalElements);
                        this.paginationProps.current=(Object(data)).pageNumber;
                        this.paginationProps.pageSize=(Object(data)).pageSize;
                })
                .catch(fail => {
                })
                .finally(()=>{
                        this.loading=false;
                });
        }

        private add():void{
            this.$router.push("/user/add");
        }

        private edit(id:string):void{
            this.$router.push({ path: '/user/add', query: { id: id }});
        }

        @Watch("$route", { immediate: true,deep: true })
        private onRouteChange(route: any):void {
            if (route.path === "/user/list"){
                this.search();
            }
        }

        private onSelectChange(selectedRowKeys:Array<number>):void{
            this.selectedRowKeys = selectedRowKeys;
        }

        private del(id:any):void{
            if (!id){
                if (this.selectedRowKeys.length <=0 ){
                    message.error("请选择要删除的数据");
                    return;
                }else{
                    id = this.selectedRowKeys;
                }
            }
            axios.delete("/upms/user/console/delete",{params:{id:id},
                paramsSerializer: params => {
                    return qs.stringify(params, { indices: false })
            }})
            .then((data)=>{
                if((Object(data)).message){
                    message.success((Object(data)).message);
                }
                this.search();
            }).catch((fail)=>{

            }).finally(()=>{

            })
        }
    }
</script>

<style lang="css" scoped>
    .ant-form-item >>> .ant-form-item-label{
        width: 80px;
    }
    .ant-form-item{
        width: 100%;
    }
    .ant-row >>> .ant-form-item-control-wrapper{
        width: calc(100% - 80px);
    }
</style>
