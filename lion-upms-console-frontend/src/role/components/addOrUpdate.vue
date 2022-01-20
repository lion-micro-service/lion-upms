<template>
    <a-modal v-model:visible="addOrUpdateModal" width="800px" title="添加/修改角色" @cancel="cancel" centered @ok="addOrUpdate" :maskClosable="maskClosable" cancelText="关闭" okText="保存">
        <a-form ref="addOrUpdateForm" name="custom-validation" :model="addOrUpdateModel" :rules="rules" >
            <a-row>
                <a-col :span="12">
                    <a-form-item label="作用域" name="scope" ref="scope" >
                        <a-select disabled="disabled"  v-model:value="addOrUpdateModel.scope">
                            <a-select-option :key="value.key" v-for="(value) in scope" :value="value.name">{{value.desc}}</a-select-option>
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="名称" name="name" ref="name">
                        <a-input placeholder="请输入名称" v-model:value="addOrUpdateModel.name" />
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="12">
                    <a-form-item label="编码" name="code" ref="code">
                        <a-input placeholder="请输入编码" v-model:value="addOrUpdateModel.code" />
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col id="remark" :span="24">
                    <a-form-item label="备注" name="remark" ref="remark">
                        <a-textarea  placeholder="请输入备注" :rows="4" v-model:value="addOrUpdateModel.remark"/>
                    </a-form-item>
                </a-col>
            </a-row>
        </a-form>
    </a-modal>
</template>

<script lang="ts">
    import {Options,  Vue} from 'vue-property-decorator';
    import axios from "@lion/lion-frontend-core/src/network/axios";
    import { message } from 'ant-design-vue';
    @Options({components:{}})
    export default class addOrUpdate extends Vue{
        //是否点击阴影层关闭窗口
        private maskClosable:boolean=false;
        //是否显示窗口
        private addOrUpdateModal:boolean=false;
        //新增/修改数据模型
        public addOrUpdateModel:any={}
        //作用域
        private scope:Array<any>=[];
        //校验规则
        private rules:any={
            code:[{required:true,validator:(rule :any, value:string) => {return this.checkCodeIsExist(rule,value,this)},trigger:'blur'}],
            name:[{required:true,validator:(rule :any, value:string) => {return this.checkNameIsExist(rule,value,this)},trigger:'blur'}],
        };

      /**
       * 关闭弹窗时清空数据，以免数据污染
       * @private
       */
        private cancel():void {
          (this.$refs.addOrUpdateForm as any).clearValidate();
          (this.$refs.addOrUpdateForm as any).resetFields();
        }

        /**
         * 检查编码是否存在
         * @param rule
         * @param value
         * @param callback
         */
        private checkCodeIsExist(rule :any, value:string,_this:any):any{
          if (!value || value.trim() === ''){
            return Promise.reject('请输入编码');
          }else if (value && value.trim() !== ''){
            axios.get("/lion-upms-console-restful/role/console/check/code/exist",{params:{"code":_this.addOrUpdateModel.code,"id":_this.addOrUpdateModel.id}})
            .then((data)=> {
              if (Object(data).status !== 200){
                return Promise.reject('异常错误！请检查');
              }
              if (data.data) {
                return Promise.reject('编码已存在');
              }else {
                return Promise.resolve();
              }
            })
            .catch(fail => {
            })
            .finally(()=>{
            });
            return Promise.resolve();
          }
          return Promise.resolve();
        }

        /**
         * 检查名称是否存在
         * @param rule
         * @param value
         * @param callback
         */
        private checkNameIsExist(rule :any, value:string,_this:any):any{
          if (!value || value.trim() === ''){
            return Promise.reject('请输入名称');
          }else if (value && value.trim() !== ''){
            axios.get("/lion-upms-console-restful/role/console/check/name/exist",{params:{"name": _this.addOrUpdateModel.name,"id":_this.addOrUpdateModel.id}})
            .then((data)=> {
              if (Object(data).status !== 200){
                return Promise.reject('异常错误！请检查');
              }
              if (data.data) {
                return Promise.reject('名称已存在');
              }else {
                return Promise.resolve();
              }
            })
            .catch(fail => {
            })
            .finally(()=>{
            });
            return Promise.resolve();
          }
          return Promise.resolve();
        }

        /**
         * 提交数据
         */
        private addOrUpdate():void{
            // (this.$refs.addOrUpdateForm as any).validate((validate: boolean) => {
            //     if (validate) {
            //         if (this.addOrUpdateModel.id){
            //             axios.put("/lion-upms-console-restful/role/console/update",this.addOrUpdateModel)
            //             .then((data) =>{
            //                 if (Object(data).status === 200){
            //                     message.success(Object(data).message);
            //                     this.success();
            //                 }
            //             }).catch((fail)=>{
            //             }).finally(()=>{
            //             })
            //         }else {
            //             axios.post("/lion-upms-console-restful/role/console/add",this.addOrUpdateModel)
            //             .then((data) =>{
            //                 if (Object(data).status === 200){
            //                     message.success(Object(data).message);
            //                     this.success();
            //                 }
            //             }).catch((fail)=>{
            //             }).finally(()=>{
            //             })
            //         }
            //     }
            // });
          let validate = (this.$refs.addOrUpdateForm as any).validate();
        }

        /**
         * 获取详情
         * @param id
         */
        private getDetails(id:string):void{
            axios.get("/lion-upms-console-restful/role/console/details",{params:{"id":id}})
            .then((data)=>{
                if (Object(data).status === 200){
                    let role = data.data;
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

        /**
         * 保存/修改成功后事件
         */
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
 #remark >>> .ant-form-item-control{
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
 .ant-row >>> .ant-form-item-control{
     width: calc(100% - 80px);
 }
</style>