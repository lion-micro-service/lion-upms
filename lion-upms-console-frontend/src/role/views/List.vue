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
                    <a-col :span="6">
                        <a-form-model-item label="名称" prop="name" ref="name" >
                            <a-input placeholder="请输入名称" v-model="searchModel.name"/>
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="6">
                        <a-form-model-item label="编码" prop="code" ref="code" >
                            <a-input placeholder="请输入编码" v-model="searchModel.code"/>
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="6">
                    </a-col>
                </a-row>
                <a-row >
                    <a-col :span="24" style="text-align:right;">
                        <a-form-item>
                            <a-button v-if="getAuthority('SYSTEM_SETTINGS_ROLE_LIST')" style="margin-left: 5px;" type="primary" icon="search" @click="search()">查询</a-button>
                            <a-button v-if="getAuthority('SYSTEM_SETTINGS_ROLE_ADD')" style="margin-left: 5px;" type="primary" icon="file-add" @click="add()">添加</a-button>
                            <a-button v-if="getAuthority('SYSTEM_SETTINGS_ROLE_ADD')" style="margin-left: 5px;" type="danger" icon="delete" @click="del()">删除</a-button>
                        </a-form-item>
                    </a-col>
                </a-row>
            </a-form-model>
        </a-card>

        <a-card v-if="getAuthority('SYSTEM_SETTINGS_ROLE_LIST')" :bordered="false">
            <a-table bordered :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }" rowKey="id" :columns="columns" :dataSource="data" :loading="loading" :pagination="paginationProps">
                <span slot="action" slot-scope="text, record">
                    <a-button style="margin-left: 5px;" icon="edit" v-if="getAuthority('SYSTEM_SETTINGS_ROLE_UPDATE')" size="small" @click="edit(record.id)">修改</a-button>
                    <a-button style="margin-left: 5px;" icon="security-scan" v-if="getAuthority('SYSTEM_SETTINGS_ROLE_RESOURCES')" size="small" @click="roleResources(record.id)">权限</a-button>
                    <a-button style="margin-left: 5px;" icon="user" v-if="getAuthority('SYSTEM_SETTINGS_ROLE_USER')" size="small" @click="roleUser(record.id)">用户</a-button>
                    <a-button style="margin-left: 5px;" v-if="!record.isDefault && getAuthority('SYSTEM_SETTINGS_ROLE_UPDATE')" type="danger" icon="delete" size="small" @click='del(record.id)'>删除</a-button>
                </span>
            </a-table>
        </a-card>

        <add-or-update ref="addOrUpdate"></add-or-update>

        <role-resources ref="roleResources"></role-resources>

        <role-department ref="roleDepartment"></role-department>

        <role-user ref="roleUser"></role-user>

        <role-position ref="rolePosition"></role-position>
    </div>
</template>

<script lang="ts">
    import {Component,  Vue} from 'vue-property-decorator';
    import axios from "@lion/lion-frontend-core/src/network/axios";
    import addOrUpdate from "@/role/components/addOrUpdate.vue";
    import roleResources from "@/role/components/roleResources.vue";
    import { message } from 'ant-design-vue';
    import qs from "qs";
    import RoleDepartment from "@/role/components/roleDepartment.vue";
    import RoleUser from "@/role/components/roleUser.vue";
    import RolePosition from "@/role/components/rolePosition.vue";
    import authority from "@lion/lion-frontend-core/src/security/authority";
    @Component({components:{RolePosition, RoleUser, RoleDepartment, addOrUpdate,roleResources}})
    export default class List extends Vue{
        //查询数据模型
        private searchModel : any ={
            pageNumber:1,
            pageSize:10,
            scope:"CONSOLE"
        }
        //列表复选框选中的值
        private selectedRowKeys:Array<number> = [];
        //列表数据源
        private data:Array<any> = [];
        //列表是否加载中（转圈圈）
        private loading:boolean=false;
        //作用域下拉框数据源
        private scope:Array<any>=[];
        //列表表头定义
        private columns :Array<any> = [
            { title: '名称', dataIndex: 'name', key: 'name'},
            { title: '编码', dataIndex: 'code', key: 'code' },
            { title: '状态', dataIndex: 'state.desc', key: 'state'},
            { title: '操作', key: 'action', scopedSlots: { customRender: 'action' },width: 350,}
        ];
        //列表分页参数定义
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

        /**
         * 列表复选框改变事件
         * @param selectedRowKeys
         */
        private onSelectChange(selectedRowKeys:Array<number>):void{
            this.selectedRowKeys = selectedRowKeys;
        }

        /**
         * 分页上/下页，跳转到第几页触发事件
         * @param pageNumber
         * @param pageSize
         */
        private paginationSearch(pageNumber:number, pageSize: number):void{
            this.searchModel.pageNumber=pageNumber;
            this.searchModel.pageSize=pageSize;
            this.search();
        }

        /**
         * 查询
         */
        private search():void{
            if (!this.getAuthority('SYSTEM_SETTINGS_ROLE_LIST')){
                return;
            }
            this.loading=true;
            axios.get("/lion-upms-console-restful/role/console/list",{params:this.searchModel})
            .then((data)=>{
                this.data=data.data;
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

        /**
         * 组件挂载后触发事件
         */
        private mounted() {
            axios.get("/lion-common-console-restful/enum/console/to/select", {params: {"enumClass": "com.lion.upms.entity.common.enums.Scope"}})
            .then((data) => {
                this.scope = data.data;
                (this.$refs.addOrUpdate as any).scope=data.data;
                (this.$refs.addOrUpdate as any).addOrUpdateModel.scope="CONSOLE";
                this.search();
            })
            .catch(fail => {
            })
            .finally(() => {
            });
        }

        /**
         * 作用域下拉框改变触发事件（用于改变新增/修改窗口scope值）
         * @param value
         */
        private searchScopelChange(value:string):void{
            (this.$refs.addOrUpdate as any).addOrUpdateModel.scope=value;
        }

        /**
         * 弹出新增窗口
         */
        private add():void{
            const child = (this.$refs.addOrUpdate as any);
            child.addOrUpdateModel={};
            child.addOrUpdateModel.scope=this.searchModel.scope;
            child.addOrUpdateModal=true;
        }

        /**
         * 弹出修改窗口
         * @param id
         */
        private edit(id:string):void{
            const child = (this.$refs.addOrUpdate as any);
            child.getDetails(id);
        }

        /**
         * 弹出删除警示
         * @param id
         */
        private del(id:any):void{
            const _this =this;
            if (!id){
                if (this.selectedRowKeys.length <=0 ){
                    message.error("请选择要删除的数据");
                    return;
                }else{
                    id = this.selectedRowKeys;
                }
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
            axios.delete("/lion-upms-console-restful/role/console/delete",{params:{id:id},
                paramsSerializer: params => {
                    return qs.stringify(params, { indices: false })
                }})
            .then((data)=>{
                if((Object(data)).status === 200 && (Object(data)).message){
                    message.success((Object(data)).message);
                    this.search();
                }
            }).catch((fail)=>{
            }).finally(()=>{
                this.selectedRowKeys=[];
            });
        }

        /**
         * 弹出角色权限配置窗口（资源）
         * @param id
         */
        private roleResources(id:string):void{
            if (!id){
                message.error("请选择角色进行配置权限（资源）");
                return
            }
            const child = (this.$refs.roleResources as any);
            child.scope=this.searchModel.scope;
            child.checkedKeys=[];
            child.treeData=[];
            child.roleId=id;
            child.modal=true;
            setTimeout(function () {
                child.roleResources();
            },500);
        }

        /**
         * 弹出角色用户关联设置窗口
         * @param id
         */
        private roleUser(id:string):void{
            if (!id){
                message.error("请选择角色进行用户关联");
                return
            }
            const child = this.$refs.roleUser as any;
            child.modal=true;
            child.roleId=id;
            child.searchModel= {
                pageNumber:1
            }
            setTimeout(function () {
                child.search();
            },500)
        }

        /**
         * 判断(获取)是否有权限
         */
        private getAuthority(authorityCode:string):any{
            return authority(authorityCode);
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