<template>
    <a-modal v-model="addOrUpdateModal" width="800px" title="添加/修改角色" centered @ok="addOrUpdate" :maskClosable="maskClosable" cancelText="关闭" okText="保存">
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
                    <a-form-model-item label="名称" prop="name" ref="name">
                        <a-input placeholder="请输入名称" v-model="addOrUpdateModel.name" />
                    </a-form-model-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="12">
                    <a-form-model-item label="编码" prop="code" ref="code">
                        <a-input placeholder="请输入编码" v-model="addOrUpdateModel.code" />
                    </a-form-model-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col id="remark" :span="24">
                    <a-form-model-item label="备注" prop="remark" ref="remark">
                        <a-textarea  placeholder="请输入备注" :rows="4" v-model="addOrUpdateModel.remark"/>
                    </a-form-model-item>
                </a-col>
            </a-row>
        </a-form-model>
    </a-modal>
</template>

<script lang="ts">
    import {Component, Emit, Inject, Model, Prop, Provide, Vue, Watch} from 'vue-property-decorator';
    import axios from "@lion/lion-front-core/src/network/axios";
    import { message } from 'ant-design-vue';
    @Component({})
    export default class addOrUpdate extends Vue{
        private maskClosable:boolean=false;
        private addOrUpdateModal:boolean=false;
        private addOrUpdateModel:any={}
        private scope:Array<any>=[];
        private rules:any={
            code:[{required:true,validator:this.checkCodeIsExist,trigger:'blur'}],
            name:[{required:true,validator:this.checkNameIsExist,trigger:'blur'}],
        };

        private checkCodeIsExist(rule :any, value:string, callback:any):void{
            if (!value || value.trim() === ''){
                callback(new Error('请输入编码'));
                return;
            }else if (value && value.trim() !== ''){
                axios.get("/upms/role/console/check/code/exist",{params:{"code":this.addOrUpdateModel.code,"id":this.addOrUpdateModel.id}})
                    .then((data)=> {
                        if (Object(data).status !== 200){
                            callback(new Error('异常错误！请检查'));
                            return;
                        }
                        if (data.data.isExist) {
                            callback(new Error('编码已存在'));
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
                axios.get("/upms/role/console/check/name/exist",{params:{"name":this.addOrUpdateModel.name,"id":this.addOrUpdateModel.id}})
                    .then((data)=> {
                        if (Object(data).status !== 200){
                            callback(new Error('异常错误！请检查'));
                            return;
                        }
                        if (data.data.isExist) {
                            callback(new Error('名称已存在'));
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

        private addOrUpdate():void{
            (this.$refs.addOrUpdateForm as any).validate((validate: boolean) => {
                if (validate) {
                    if (this.addOrUpdateModel.id){
                        axios.put("/upms/role/console/update",this.addOrUpdateModel)
                        .then((data) =>{
                            if (Object(data).status === 200){
                                message.success(Object(data).message);
                                this.success();
                            }
                        }).catch((fail)=>{
                        }).finally(()=>{
                        })
                    }else {
                        axios.post("/upms/role/console/add",this.addOrUpdateModel)
                        .then((data) =>{
                            if (Object(data).status === 200){
                                message.success(Object(data).message);
                                this.success();
                            }
                        }).catch((fail)=>{
                        }).finally(()=>{
                        })
                    }
                }
            });
        }

        private getDetails(id:string):void{
            axios.get("/upms/role/console/details",{params:{"id":id}})
            .then((data)=>{
                if (Object(data).status === 200 && data.data.role){
                    let role = data.data.role;
                    const _scope=role.scope.name;
                    delete role.state; //必须删除(该值为object),否则会有诡异问题
                    delete role.scope; //必须删除(该值为object),否则会有诡异问题
                    this.addOrUpdateModel=role;
                    this.addOrUpdateModel.scope=_scope;
                    this.addOrUpdateModal=true;
                }
            })
            .catch(fail => {
            })
            .finally(()=>{
            });
        }

        private success():void{
            this.addOrUpdateModal = false;
            const _scope=this.addOrUpdateModel.scope;
            this.addOrUpdateModel={};
            this.addOrUpdateModel.scope=_scope;
            (this.$parent as any).search();
        }

    }
</script>

<style scoped>
 #remark >>> .ant-form-item-control-wrapper{
     width: calc(100% - 80px);
 }
 #remark >>> .ant-form-item{
     width: 100%;
 }
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