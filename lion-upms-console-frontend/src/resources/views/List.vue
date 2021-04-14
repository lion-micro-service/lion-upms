<template>
    <div>
        <a-card class="card" style="border-bottom-width: 5px;">
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
                            <a-button style="margin-left: 5px;" type="primary" v-if="getAuthority('SYSTEM_SETTINGS_RESOURCES_LIST')" icon="search" @click="search()" >查询</a-button>
                            <a-button style="margin-left: 5px;" type="primary" v-if="getAuthority('SYSTEM_SETTINGS_RESOURCES_ADD')" icon="file-add" @click="add(0,0)" >添加</a-button>
                        </a-form-item>
                    </a-col>
                </a-row>
            </a-form-model>
        </a-card>

        <a-card v-if="getAuthority('SYSTEM_SETTINGS_RESOURCES_LIST')" :bordered="false">
            <a-table bordered rowKey="id" :columns="columns" :dataSource="data" :loading="loading" :pagination="false">
                <span slot="action" slot-scope="text, record">
                    <a-button style="margin-left: 5px;" v-if="getAuthority('SYSTEM_SETTINGS_RESOURCES_UPDATE')" icon="edit" size="small" @click="getDetails(record.id)">修改</a-button>
                    <a-button style="margin-left: 5px;" v-if="(record.type.key===1) && getAuthority('SYSTEM_SETTINGS_RESOURCES_ADD') " icon="file-add" size="small" @click="add(record.id,2)">添加功能</a-button>
                    <a-button style="margin-left: 5px;" v-if="(record.type.key===0) && getAuthority('SYSTEM_SETTINGS_RESOURCES_ADD') " icon="file-add" size="small" @click="add(record.id,0)">添加目录</a-button>
                    <a-button style="margin-left: 5px;" v-if="(record.type.key===0) && getAuthority('SYSTEM_SETTINGS_RESOURCES_ADD') " icon="file-add" size="small" @click="add(record.id,1)">添加菜单</a-button>
                    <a-button style="margin-left: 5px;" v-if="(!record.isDefault) && getAuthority('SYSTEM_SETTINGS_RESOURCES_DELETE') " type="danger" icon="delete" size="small" @click='del(record.id)'>删除</a-button>
                </span>
            </a-table>
        </a-card>

        <a-modal destroyOnClose v-model="addOrUpdateModal" width="800px" title="添加/修改资源" :maskClosable="maskClosable"  centered @ok="addOrUpdate" cancelText="关闭" okText="保存">
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
    import {Component, Vue} from 'vue-property-decorator';
    import axios from "@lion/lion-frontend-core/src/network/axios";
    import { message } from 'ant-design-vue';
    import authority from "@lion/lion-frontend-core/src/security/authority";
    @Component({})
    export default class List extends Vue{
        //列表数据源
        private data:Array<any>=[];
        //是否列表加载中
        private loading:boolean=false;
        //是否点击阴影层关闭modal（新增/修改窗口）
        private maskClosable:boolean=false;
        //url输入是否禁用
        private urlDisabled:boolean=false;
        //新增/修改数据模型
        private addOrUpdateModal:boolean=false;
        //作用域下拉框数据源
        private scope:Array<any> = [];
        //类型下拉框数据源
        private type:Array<any> = [];
        //是否提交数据中（防重提交）
        private isSave:boolean=false;
        //查询数据模型
        private searchModel : any ={
            scope:"CONSOLE",
            pageNumber:1,
            pageSize:999
        };
        //新增/修改数据模型
        private addOrUpdateModel:any ={
            scope:'CONSOLE',
            type:"CATALOG",
            sort:0
        };
        //表格列定义
        private columns :Array<any> = [
            { title: '名称', dataIndex: 'name', key: 'name',width: '200px' },
            { title: '编码', dataIndex: 'code', key: 'code',width: '500px'},
            { title: '作用域', dataIndex: 'scope.desc', key: 'scope' },
            { title: '类型', dataIndex: 'type.desc', key: 'type' },
            { title: 'url', dataIndex: 'url', key: 'url',width: '150px' },
            { title: '排序', dataIndex: 'sort', key: 'sort' },
            { title: '状态', dataIndex: 'state.desc', key: 'state' },
            { title: '操作', key: 'action', scopedSlots: { customRender: 'action' },width: 400,}
        ];
        //校验规则
        private rules:any={
            code:[{required:true,validator:this.checkCodeIsExist,trigger:'blur'}],
            name:[{required:true,validator:this.checkNameIsExist,trigger:'blur'}],
            url:[{validator:this.checkUrlIsExist,trigger:'blur'}],
        };

        /**
         * 检查编码是否存在
         * @param rule
         * @param value
         * @param callback
         */
        private checkCodeIsExist(rule :any, value:string, callback:any):void{
            if (!value || value.trim() === ''){
                callback(new Error('请输入编码'));
                return;
            }else if (value && value.trim() !== ''){
                axios.get("/lion-upms-console-restful/resources/console/check/code/exist",{params:{"code":this.addOrUpdateModel.code,"id":this.addOrUpdateModel.id}})
                .then((data)=> {
                    if (Object(data).status !== 200){
                        callback(new Error('异常错误！请检查'));
                        return;
                    }
                    if (data.data) {
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

        /**
         * 检查名称是否存在
         * @param rule
         * @param value
         * @param callback
         */
        private checkNameIsExist(rule :any, value:string, callback:any):void{
            if (!value || value.trim() === ''){
                callback(new Error('请输入名称'));
                return;
            }else if (value && value.trim() !== ''){
                axios.get("/lion-upms-console-restful/resources/console/check/name/exist",{params:{"name":this.addOrUpdateModel.name,"id":this.addOrUpdateModel.id,"parentId":this.addOrUpdateModel.parentId}})
                    .then((data)=> {
                        if (Object(data).status !== 200){
                            callback(new Error('异常错误！请检查'));
                            return;
                        }
                        if (data.data) {
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

        /**
         * 检查url是否存在
         * @param rule
         * @param value
         * @param callback
         */
        private checkUrlIsExist(rule :any, value:string, callback:any):void{
            if (this.addOrUpdateModel.type ==='MENU' && (!value || value.trim() === '')){
                callback(new Error('请输入url'));
                return;
            }else if (this.addOrUpdateModel.type ==='MENU' && value && value.trim() !== ''){
                axios.get("/lion-upms-console-restful/resources/console/check/url/exist",{params:{"url":this.addOrUpdateModel.url,"id":this.addOrUpdateModel.id}})
                    .then((data)=> {
                        if (Object(data).status !== 200){
                            callback(new Error('异常错误！请检查'));
                            return;
                        }
                        if (data.data) {
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

        /**
         * 类型下拉框改变事件
         * @param value
         */
        private typeChange(value:string):void{
            if (value && value === 'MENU'){
                this.urlDisabled=false;
                (this.$refs.form as any).validateField("url");
            }else {
                this.urlDisabled=true;
                this.addOrUpdateModel.url = "";
            }
        }

        /**
         * 组件挂载后触发事件
         */
        private async mounted() {
            await axios.get("/lion-common-console-restful/enum/console/to/select", {params: {"enumClass": "com.lion.upms.entity.common.enums.Scope"}})
            .then((data) => {
                this.scope = data.data;
            })
            .catch(fail => {
            })
            .finally(() => {
            });
            await axios.get("/lion-common-console-restful/enum/console/to/select", {params: {"enumClass": "com.lion.upms.entity.resources.enums.Type"}})
            .then((data) => {
                this.type = data.data;
            })
            .catch(fail => {
            })
            .finally(() => {
            });
            this.search();
        }

        /**
         * 查询
         */
        private search():void{
            if (!this.getAuthority('SYSTEM_SETTINGS_RESOURCES_LIST')){
                return;
            }
            this.loading=true;
            axios.get("/lion-upms-console-restful/resources/console/list/tree",{params:this.searchModel})
            .then((data)=>{
                this.data=data.data;
            })
            .catch(fail => {
            })
            .finally(()=>{
                this.loading=false;
            });
        }

        /**
         * 提交新增/修改数据
         * @param id
         */
        private addOrUpdate(id:number):void{
            if (this.isSave){
                return;
            }
            (this.$refs.addOrUpdateForm as any).validate((validate: boolean) => {
                if (validate) {
                    this.isSave=true;
                    if (this.addOrUpdateModel.id){
                        axios.put("/lion-upms-console-restful/resources/console/update",this.addOrUpdateModel)
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
                        axios.post("/lion-upms-console-restful/resources/console/add",this.addOrUpdateModel)
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

        /**
         * 新增/修改/删除成功后触发事件
         */
        private success():void{
            this.addOrUpdateModel ={
                scope:this.searchModel.scope,
            };
            (this.$refs.addOrUpdateForm as any).resetFields();
            this.addOrUpdateModal = false;
            this.search();
        }

        /**
         * 作用于下拉框改变事件（可以不写）
         * @param value
         */
        private searchScopelChange(value:string):void{
            this.addOrUpdateModel.scope=value;
        }

        /**
         * 显示新增窗口
         * @param parentId
         * @param type
         */
        private add(parentId:number,type:number){
            //重置新增数据模型（避免历史数据干扰）
            this.addOrUpdateModel ={
                scope:this.searchModel.scope,
            };
            //判断添加资源类型（对相应的组件/输入框做调整）
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

        /**
         * 获取详情
         * @param id
         */
        private getDetails(id:string):void{
            axios.get("/lion-upms-console-restful/resources/console/details",{params:{"id":id}})
            .then((data)=>{
                if (Object(data).status === 200){
                    if (data.data.type.key !== 1){
                        this.urlDisabled=true;
                    }else {
                        this.urlDisabled=false;
                    }
                    const resources = data.data;
                    const _type = resources.type.name;
                    const _scope = resources.scope.name;
                    delete resources.state; //必须删除(该值为object),否则会有诡异问题
                    delete resources.type; //必须删除(该值为object),否则会有诡异问题
                    delete resources.scope; //必须删除(该值为object),否则会有诡异问题
                    this.addOrUpdateModel=resources;
                    this.addOrUpdateModel.type=_type;
                    this.addOrUpdateModel.scope= _scope;
                    this.addOrUpdateModal=true;
                }
            })
            .catch(fail => {
            })
            .finally(()=>{
            });
        }

        /**
         * 弹出删除警示框
         * @param id
         */
        private del(id:any):void{
            const _this =this;
            if (!id){
                message.error("请选择要删除的数据");
                return;
            }
            this.$confirm({
                title: '是否要删除该数据?(错误的操作会带来灾难性的后果)',
                // content: '',
                okText: 'Yes',
                okType: 'danger',
                cancelText: 'No',
                onOk() {
                    _this.delete(id);
                },
                onCancel() {
                },
            });

        }

        /**
         * 删除
         * @param id
         */
        private delete(id:any):void{
            axios.delete("/lion-upms-console-restful/resources/console/delete",{params:{id:id}})
            .then((data)=>{
                if((Object(data)).status === 200 && (Object(data)).message){
                    message.success((Object(data)).message);
                    this.search();
                }
            }).catch((fail)=>{
            }).finally(()=>{
            });
        }

        /**
         * 判断(获取)是否有权限
         */
        private getAuthority(authorityCode:string):any{
            return authority(authorityCode);
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
        width: 80px;
    }
    .ant-form-item{
        width: 100%;
    }
    .ant-row >>> .ant-form-item-control-wrapper{
        width: calc(100% - 80px);
    }
</style>