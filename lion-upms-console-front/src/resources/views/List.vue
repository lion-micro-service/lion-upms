<template>
    <div>
        <a-card class="card" :bordered="false">
            <a-form-model layout="inline" ref="from" :model="searchModel" >
                <a-row>
                    <a-col :span="6">
                        <a-form-model-item label="作用域" prop="scope" ref="scope" >
                            <a-select  v-model="searchModel.scope" @change="searchScopelChange">
                                <a-select-option :key="value.key" v-for="(value) in scope" :value="value.name">{{value.desc}}</a-select-option>
                            </a-select>
                        </a-form-model-item>
                    </a-col>
                </a-row>
                <a-row >
                    <a-col :span="24" style="text-align:right;">
                        <a-form-item>
                            <a-button type="primary" icon="search" @click="search()" >查询</a-button>
                            <a-button type="primary" icon="file-add" @click="add(0,0)" >添加</a-button>
                        </a-form-item>
                    </a-col>
                </a-row>
            </a-form-model>
        </a-card>

        <a-card :bordered="false">
            <a-table rowKey="id" :columns="columns" :dataSource="data" :loading="loading" :pagination="false">
                <span slot="action" slot-scope="text, record">
                    <a-button icon="edit" size="small" @click="getDetails(record.id)">修改</a-button>
                    <a-button v-if="record.type.key===0||record.type.key===1" icon="file-add" size="small" @click="add(record.id,record.type.key===0?1:2)">{{ record.type.key===0?'添加菜单':'添加功能' }}</a-button>
                    <a-button v-if="!record.isDefault" type="danger" icon="delete" size="small" @click='del(record.id)'>删除</a-button>
                </span>
            </a-table>
        </a-card>

        <a-modal v-model="addOrUpdateModal" width="800px" title="添加/修改资源" centered @ok="addOrUpdate" cancelText="关闭" okText="保存">
            <a-form-model layout="inline" ref="addOrUpdateForm" :model="addOrUpdateModel" :rules="rules" >
                <a-row>
                    <a-col :span="12">
                        <a-form-model-item label="作用域" prop="scope" ref="scope" >
                            <a-select disabled="disabled"  v-model="addOrUpdateModel.scope">
                                <a-select-option :key="value.key" v-for="(value) in scope" :value="value.name">{{value.desc}}</a-select-option>
                            </a-select>
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-model-item label="类型" prop="type" ref="type" >
                            <a-select disabled="disabled" v-model="addOrUpdateModel.type" @change="typeChange">
                                <a-select-option :key="value.key" v-for="(value) in type" :value="value.name">{{value.desc}}</a-select-option>
                            </a-select>
                        </a-form-model-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col :span="12">
                        <a-form-model-item label="名称" prop="name" ref="name">
                            <a-input placeholder="请输入名称" v-model="addOrUpdateModel.name" />
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-model-item label="编码" prop="code" ref="code">
                            <a-input placeholder="请输入编码" v-model="addOrUpdateModel.code" />
                        </a-form-model-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col :span="12">
                        <a-form-model-item label="URL" prop="url" ref="url">
                            <a-input v-bind:disabled="urlDisabled" placeholder="请输入URL" v-model="addOrUpdateModel.url" />
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-model-item label="排序" prop="sort" ref="sort">
                            <a-input-number   v-model="addOrUpdateModel.sort" />
                        </a-form-model-item>
                    </a-col>
                </a-row>
            </a-form-model>
        </a-modal>
    </div>
</template>

<script lang="ts">
    import {Component, Emit, Inject, Model, Prop, Provide, Vue, Watch} from 'vue-property-decorator';
    import axios from "@lion/lion-front-core/src/network/axios";
    import { message } from 'ant-design-vue';
    @Component({})
    export default class List extends Vue{

        private loading:boolean=false;

        private urlDisabled:boolean=false;

        private addOrUpdateModal:boolean=false;

        private scope:Array<any> = [];

        private type:Array<any> = [];

        private isSave:boolean=false;

        private searchModel : any ={
            scope:"CONSOLE",
            pageNumber:1,
            pageSize:999
        };

        private addOrUpdateModel:any ={
            scope:'CONSOLE',
            type:"CATALOG",
            sort:0
        };



        private rules:any={
            code:[{required:true,validator:this.checkCodeIsExist,trigger:'blur'}],
            name:[{required:true,validator:this.checkNameIsExist,trigger:'blur'}],
            url:[{validator:this.checkUrlIsExist,trigger:'blur'}],
        };

        private checkCodeIsExist(rule :any, value:string, callback:any):void{
            if (!value || value.trim() === ''){
                callback(new Error('请输入编码'));
                return;
            }else if (value && value.trim() !== ''){
                axios.get("/upms/resources/console/check/code/exist",{params:{"code":this.addOrUpdateModel.code,"id":this.addOrUpdateModel.id}})
                .then((data)=> {
                    if (Object(data).status !== 200){
                        callback(new Error('异常错误！请检查'));
                        return;
                    }
                    if (data.data.isExist) {
                        callback(new Error('该编码已存在'));
                    }else {
                        callback();
                    }
                })
                .catch(fail => {
                })
                .finally(()=>{
                });
                return;
            }
            callback();
        }

        private checkNameIsExist(rule :any, value:string, callback:any):void{
            if (!value || value.trim() === ''){
                callback(new Error('请输入名称'));
                return;
            }else if (value && value.trim() !== ''){
                axios.get("/upms/resources/console/check/name/exist",{params:{"name":this.addOrUpdateModel.name,"id":this.addOrUpdateModel.id,"parentId":this.addOrUpdateModel.parentId}})
                    .then((data)=> {
                        if (Object(data).status !== 200){
                            callback(new Error('异常错误！请检查'));
                            return;
                        }
                        if (data.data.isExist) {
                            callback(new Error('名称在同节点已存在'));
                        }else {
                            callback();
                        }
                    })
                    .catch(fail => {
                    })
                    .finally(()=>{
                    });
                return;
            }
            callback();
        }

        private checkUrlIsExist(rule :any, value:string, callback:any):void{
            if (this.addOrUpdateModel.type ==='MENU' && (!value || value.trim() === '')){
                callback(new Error('请输入url'));
                return;
            }else if (this.addOrUpdateModel.type ==='MENU' && value && value.trim() !== ''){
                axios.get("/upms/resources/console/check/url/exist",{params:{"url":this.addOrUpdateModel.url,"id":this.addOrUpdateModel.id}})
                    .then((data)=> {
                        if (Object(data).status !== 200){
                            callback(new Error('异常错误！请检查'));
                            return;
                        }
                        if (data.data.isExist) {
                            callback(new Error('该url已存在'));
                        }else {
                            callback();
                        }
                    })
                    .catch(fail => {
                    })
                    .finally(()=>{
                    });
                return;
            }
            callback();
        }

        private typeChange(value:string):void{
            if (value && value === 'MENU'){
                this.urlDisabled=false;
                (this.$refs.form as any).validateField("url");
            }else {
                this.urlDisabled=true;
                this.addOrUpdateModel.url = "";
            }
        }

        private data:Array<any>=[];

        private columns :Array<any> = [
            { title: '名称', dataIndex: 'name', key: 'name',width: '200px' },
            { title: '编码', dataIndex: 'code', key: 'code',width: '300px'},
            { title: '作用域', dataIndex: 'scope.desc', key: 'scope' },
            { title: '类型', dataIndex: 'type.desc', key: 'type' },
            { title: 'url', dataIndex: 'url', key: 'url',width: '150px' },
            { title: '排序', dataIndex: 'sort', key: 'sort' },
            { title: '操作', key: 'action', scopedSlots: { customRender: 'action' },width: 300,}
        ];

        private async mounted() {
            await axios.get("/common/enum/console/to/select", {params: {"enumClass": "com.lion.upms.entity.resources.enums.Scope"}})
                .then((data) => {
                    this.scope = data.data.enum;
                })
                .catch(fail => {
                })
                .finally(() => {
                });
            await axios.get("/common/enum/console/to/select", {params: {"enumClass": "com.lion.upms.entity.resources.enums.Type"}})
                .then((data) => {
                    this.type = data.data.enum;
                })
                .catch(fail => {
                })
                .finally(() => {
                });
            this.search();
        }

        private search():void{
            this.loading=true;
            axios.get("/upms/resources/console/list/tree",{params:this.searchModel})
            .then((data)=>{
                this.data=data.data.list;
            })
            .catch(fail => {
            })
            .finally(()=>{
                this.loading=false;
            });
        }

        private addOrUpdate(id:number):void{
            if (this.isSave){
                return;
            }
            (this.$refs.addOrUpdateForm as any).validate((validate: boolean) => {
                if (validate) {
                    this.isSave=true;
                    if (this.addOrUpdateModel.id){
                        axios.put("/upms/resources/console/update",this.addOrUpdateModel)
                        .then((data) =>{
                            if (Object(data).status === 200){
                                message.success(Object(data).message);
                                this.success();
                            }
                        }).catch((fail)=>{
                        }).finally(()=>{
                            this.isSave=false;
                        })
                    }else {
                        axios.post("/upms/resources/console/add",this.addOrUpdateModel)
                        .then((data) =>{
                            if (Object(data).status === 200){
                                message.success(Object(data).message);
                                this.success();
                            }
                        }).catch((fail)=>{
                        }).finally(()=>{
                            this.isSave=false;
                        })
                    }
                }
            });
        }

        private success():void{
            let _this = this;
            this.addOrUpdateModel ={
                scope:this.searchModel.scope,
            };
            (this.$refs.addOrUpdateForm as any).resetFields();
            setTimeout(function () {
                _this.addOrUpdateModal = false;
                _this.search();
            },3000);
        }

        private searchScopelChange(value:string):void{
            this.addOrUpdateModel.scope=value;
        }

        private add(parentId:number,type:number){
            if (type === 0){
                this.urlDisabled=true;
                this.addOrUpdateModel.type="CATALOG";
            }else if (type === 1){
                this.urlDisabled=false;
                this.addOrUpdateModel.type="MENU";
            }else if (type === 2){
                this.urlDisabled=true;
                this.addOrUpdateModel.type="FUNCTION";
            }
            this.addOrUpdateModel.parentId = parentId;
            this.addOrUpdateModal=true;
        }

        private getDetails(id:string):void{
            axios.get("/upms/resources/console/details",{params:{"id":id}})
            .then((data)=>{
                if (Object(data).status === 200 && data.data.resources){
                    if (data.data.resources.type.key !== 1){
                        this.urlDisabled=true;
                    }else {
                        this.urlDisabled=false;
                    }
                    this.addOrUpdateModel.id=data.data.resources.id;
                    this.addOrUpdateModel.parentId=data.data.resources.parentId;
                    this.addOrUpdateModel.version=data.data.resources.version;
                    this.addOrUpdateModel.name=data.data.resources.name;
                    this.addOrUpdateModel.url=data.data.resources.url;
                    this.addOrUpdateModel.sort=data.data.resources.sort;
                    this.addOrUpdateModel.code=data.data.resources.code;
                    this.addOrUpdateModel.scope=data.data.resources.scope.name;
                    this.addOrUpdateModel.type=data.data.resources.type.name;
                    this.addOrUpdateModal=true;
                }
            })
            .catch(fail => {
            })
            .finally(()=>{
            });
        }


        // @Watch("$route", { immediate: true,deep: true })
        // private onRouteChange(route: any):void {
        //     if (route.path === "/resources/list"){
        //         this.search();
        //     }
        // }


    }
</script>

<style lang="css" scoped>
    .ant-form-item >>> .ant-form-item-label{
        width: 50px;
    }
    .ant-form-item{
        width: 100%;
    }
    .ant-row >>> .ant-form-item-control-wrapper{
        width: calc(100% - 50px);
    }
</style>