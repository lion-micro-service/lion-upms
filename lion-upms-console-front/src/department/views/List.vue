<template>
    <div>
        <a-card class="card" :bordered="false">
            <a-form-model layout="inline" ref="from" >
                <a-row >
                    <a-col :span="24" style="text-align:right;">
                        <a-form-item>
                            <a-button type="primary" icon="file-add" @click="add(0)" >新增</a-button>
                        </a-form-item>
                    </a-col>
                </a-row>
            </a-form-model>
        </a-card>

        <a-card class="card" :bordered="false">
            <a-table rowKey="id" :columns="columns" :dataSource="data" :loading="loading" :pagination="false">
                <span slot="action" slot-scope="text, record">
                    <a-button icon="edit" size="small" @click="getDetails(record.id)">修改</a-button>
                    <a-button icon="file-add" size="small" @click="add(record.id)">新增子部门</a-button>
                    <a-button icon="user" size="small" @click="departmentUser(record.id)">用户</a-button>
                    <a-button type="danger" icon="delete" size="small" @click='del(record.id)'>删除</a-button>
                </span>
            </a-table>
        </a-card>

        <a-modal v-model="modal" width="800px" title="添加/修改部门" :maskClosable="maskClosable"  centered @ok="addOrUpdate" cancelText="关闭" okText="保存">
            <a-form-model layout="inline" ref="addOrUpdateForm" :model="addOrUpdateModel" :rules="rules" >
                <a-row>
                    <a-col :span="24">
                        <a-form-model-item label="名称" prop="name" ref="name" >
                            <a-input  v-model="addOrUpdateModel.name" />
                        </a-form-model-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col :span="24">
                        <a-form-model-item label="备注" prop="remark" ref="remark">
                            <a-textarea  placeholder="请输入备注" :rows="4" v-model="addOrUpdateModel.remark"/>
                        </a-form-model-item>
                    </a-col>
                </a-row>
            </a-form-model>
        </a-modal>

        <department-user ref="departmentUser" ></department-user>
    </div>
</template>

<script lang="ts">
    import {Component, Emit, Inject, Model, Prop, Provide, Vue, Watch} from 'vue-property-decorator';
    import axios from "@lion/lion-front-core/src/network/axios";
    import { message } from 'ant-design-vue';
    import qs from "qs";
    import DepartmentUser from "@/department/components/departmentUser.vue";
    @Component({
        components: {DepartmentUser}
    })
    export default class List extends Vue{
        private data:Array<any>=[];
        private loading:boolean=false;
        private modal:boolean=false;
        private maskClosable:boolean=false;
        private addOrUpdateModel:any={
            parentId:0,
        };

        private rules:any={
            name:[{required:true,validator:this.checkNameIsExist,trigger:'blur'}],
        };

        private checkNameIsExist(rule :any, value:string, callback:any):void{
            if (!value || value.trim() === ''){
                callback(new Error('请输入名称'));
                return;
            }else if (value && value.trim() !== ''){
                axios.get("/upms/department/console/check/name/exist",{params:{"name":this.addOrUpdateModel.name,"id":this.addOrUpdateModel.id,"parentId":this.addOrUpdateModel.parentId}})
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

        private columns :Array<any> = [
            { title: '名称', dataIndex: 'name', key: 'name' },
            { title: '备注', dataIndex: 'remark', key: 'remark',width: '300px'},
            { title: '操作', key: 'action', scopedSlots: { customRender: 'action' },width: 380,}
        ];

        private mounted():void {
            this.search();
        }

        private search():void{
            this.loading=true;
            axios.get("/upms/department/console/list/tree",{params:{}})
            .then((data)=>{
                this.data=data.data.list;
            })
            .catch(fail => {
            })
            .finally(()=>{
                this.loading=false;
            });
        }

        private add(parentId:string):void{
            this.addOrUpdateModel={};
            this.addOrUpdateModel.parentId=parentId;
            this.modal=true;
        }

        private addOrUpdate():void{
            if (this.addOrUpdateModel.id){
                axios.put("/upms/department/console/update",this.addOrUpdateModel)
                .then((data)=>{
                    if (Object(data).status === 200){
                        message.success(Object(data).message);
                        this.success();
                    }
                })
                .catch(fail => {
                })
                .finally(()=>{
                    this.loading=false;
                });
            }else{
                axios.post("/upms/department/console/add",this.addOrUpdateModel)
                .then((data)=>{
                    if (Object(data).status === 200){
                        message.success(Object(data).message);
                        this.success();
                    }
                })
                .catch(fail => {
                })
                .finally(()=>{
                    this.loading=false;
                });
            }

        }

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

        private delete(id:any):void{
            axios.delete("/upms/department/console/delete",{params:{id:id},
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
            });
        }

        private success():void{
            this.addOrUpdateModel ={parentId:0};
            (this.$refs.addOrUpdateForm as any).resetFields();
            this.modal = false;
            this.search();
        }

        private getDetails(id:string):void{
            axios.get("/upms/department/console/details",{params:{id:id}})
            .then((data)=>{
                this.addOrUpdateModel=data.data.department;
                this.modal = true;
            })
            .catch(fail => {
            })
            .finally(()=>{
            });
        }

        private departmentUser(id:string):void{
            const child = (this.$refs.departmentUser as any);
            child.modal=true;
            child.departmentId=id;
            child.searchModel.pageNumber=1;
            setTimeout(function () {
                child.search();
            },500);
        }
    }
</script>

<style scoped>
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