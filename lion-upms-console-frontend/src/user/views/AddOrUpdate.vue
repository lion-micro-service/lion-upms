<template>
    <a-card class="card" :bordered="false">
        <a-form ref="addOrUpdateForm" :model="addModel" :rules="rules" >
            <a-row >
                <a-col :span="8">
                    <a-form-item label="登陆账号" name="username" ref="username" >
                        <a-input v-bind:disabled="usernameDisabled" placeholder="请输入登陆账号" v-model:value="addModel.username"  autocomplete="off"/>
                    </a-form-item>
                </a-col>
                <a-col :span="8">
                    <a-form-item label="密码" name="pass" ref="pass" >
                        <a-input-password v-bind:disabled="passDisabled" placeholder="请输入密码" v-model:value="addModel.pass" autocomplete="off"/>
                    </a-form-item>
                </a-col>
                <a-col :span="8">
                    <a-form-item label="确认密码" name="confirmPass" ref="confirmPass" >
                        <a-input-password v-bind:disabled="confirmPassDisabled" placeholder="请输入确认密码" v-model:value="addModel.confirmPass" autocomplete="off"/>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="8">
                    <a-form-item label="姓名" name="name" ref="name" >
                        <a-input placeholder="请输入姓名" v-model:value="addModel.name" />
                    </a-form-item>
                </a-col>
                <a-col :span="8">
                    <a-form-item label="邮箱" name="email" ref="email">
                        <a-input placeholder="请输入邮箱" v-model:value="addModel.email" />
                    </a-form-item>
                </a-col>
                <a-col :span="8">
                    <a-form-item label="年龄" name="age" ref="age">
                        <a-input-number  placeholder="请输入年龄" v-model:value="addModel.age" />
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="8">
                    <a-form-item label="生日" name="birthday" ref="birthday" >
                        <a-date-picker placeholder="请输入生日" valueFormat="YYYY-MM-DD" v-model:value="addModel.birthday" />
                    </a-form-item>
                </a-col>
                <a-col :span="8">
                    <a-form-item label="头像" name="headPortrait" ref="headPortrait" >
                        <a-upload :action="uploadAction" accept="image/png, image/jpeg" list-type="picture-card" :file-list="headPortraitList" @change="(e)=>headPortraitChange(e)" :remove="function(file){headPortraitRemove(file)} " @preview="handlePreview">
                            <div v-if="headPortraitList.length < 1">
                                <a-icon type="plus" />
                                <div class="ant-upload-text">
                                    上传
                                </div>
                            </div>

                        </a-upload>
                    </a-form-item>
                </a-col>
            </a-row>

            <a-row >
                <a-col :span="24" style="text-align:center;">
                    <a-form-item >
                      <a-space :size="size">
                        <a-button type="primary" v-bind:disabled="saveButtonDisabled" @click="save()">
                          <template #icon><SaveOutlined /></template>
                          保存
                        </a-button>
                        <a-button type="dashed" v-bind:disabled="resetDisabled" @click="reset()">
                          <template #icon><UndoOutlined /></template>
                          重置
                        </a-button>
                        <a-button type="danger"  @click="back">
                          <template #icon><RollbackOutlined /></template>
                          返回
                        </a-button>
                      </a-space>
                    </a-form-item>
                </a-col>
            </a-row>
        </a-form>

        <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%; " :src="previewImage" />
        </a-modal>
    </a-card>
</template>

<script lang="ts">
    import {Options, Vue} from 'vue-property-decorator';
    import { useRoute, useRouter } from 'vue-router'
    import { RollbackOutlined,SaveOutlined,UndoOutlined} from '@ant-design/icons-vue';
    import axios from "@lion/lion-frontend-core/src/network/axios";
    import { message } from 'ant-design-vue'
    import moment from 'moment';
    import 'moment/locale/zh-cn';
    import {ref} from "vue";
    let md5 = require('md5');
    @Options({components:{RollbackOutlined,SaveOutlined,UndoOutlined}})
    export default class AddOrUpdate extends Vue{
      private size:any = ref(5);
      private addOrUpdateForm:any = null;
      private route:any = useRoute();
        //组件汉化
        private moment:any = moment;
        //保存按钮是否禁用（防多次点击）
        private saveButtonDisabled:boolean = false;
        //用户名输入框是否禁用
        private usernameDisabled:boolean = false;
        //密码输入框是否禁用
        private passDisabled:boolean = false;
        //确认密码输入框是否禁用
        private confirmPassDisabled:boolean = false;
        //重置按钮是否禁用
        private resetDisabled:boolean = false;
        //添加数据模型
        private addModel : any={};
        //上传头像接口地址
        private uploadAction:string=process.env.VUE_APP_BASEURL+process.env.VUE_APP_BASEAPI+"/lion-common-console-restful/file/console/upload"
        //头像地址
        private headPortraitList:Array<any>=[];
        //头像预览窗口是否显示
        private previewVisible:boolean=false;
        //预览图片
        private previewImage:any="";
        //校验规则
        private rules:any={
            username:[{required:true,validator:(rule :any, value:string) => {return this.checkUsernameIsExist(rule,value,this)},trigger:'blur'}],
            pass:[{required:true,validator:(rule :any, value:string,) => {return this.validatorPass(rule,value,this)}, trigger:'blur'}],
            confirmPass:[{required:true,validator:(rule :any, value:string) => {return this.validatorConfimPass(rule,value,this)}, trigger:'blur'}],
            email:[{validator:(rule :any, value:string) => {return this.checkEmailIsExist(rule,value,this)}, trigger:'blur'}]
        };

        /**
         * 检查登陆用户张号是否存在
         * @param rule
         * @param value
         * @param callback
         */
        private async checkUsernameIsExist(rule :any, value:string, _this:any){
          let promise:any=null;
          const id = _this.route.query.id;
          if(id){
            return  Promise.resolve();
          }
          if (!value || value.trim() === ''){
            promise = Promise.reject('请输入登陆账号');
          }else if (value && value.trim() !== ''){
            await axios.get("/lion-upms-console-restful/user/console/check/username/exist",{params:{"username":_this.addModel.username}})
              .then((data)=> {
                  if (Object(data).status !== 200){
                    promise = Promise.reject('异常错误！请检查');
                  }
                  if (data.data) {
                    promise = Promise.reject('该账号已存在');
                  }else {
                    promise = Promise.resolve();
                  }
              })
              .catch(fail => {
              })
              .finally(()=>{
              });
          }
          return promise;
        }

        /**
         * 检查邮箱是否存在
         * @param rule
         * @param value
         * @param callback
         */
        private async checkEmailIsExist(rule :any, value:string, _this:any){
          let promise:any=null;
          const id = _this.route.query.id;
          if (value && value.trim() !== ''){
            await axios.get("/lion-upms-console-restful/user/console/check/email/exist",{params:{email:value,id:id}})
              .then((data)=>{
                  if (Object(data).status !== 200){
                    promise = Promise.reject('异常错误！请检查');
                  }
                  if (data.data){
                    promise = Promise.reject('该邮箱已存在');
                  }else {
                    promise = Promise.resolve();
                  }
              }).catch(error=>{

              }).finally(()=>{

              })
          }else{
            promise = Promise.resolve();
          }
          return promise;
        }

        /**
         * 校验密码
         * @param rule
         * @param value
         * @param callback
         */
        private validatorPass(rule :any, value:string, _this:any):any{
          const id = _this.route.query.id;

          if(id){
            return Promise.resolve();
          }
          if (!value || value.trim() === ''){
            return Promise.reject("请输入密码");
          }else{
            if (_this.addModel.confirmPass && _this.addModel.confirmPass.trim() !== ''){
              _this.addOrUpdateForm.validateFields("confirmPass");
            }
            return Promise.resolve();
          }
        }

        /**
         * 校验确认密码
         * @param rule
         * @param value
         * @param callback
         */
        private validatorConfimPass(rule :any, value:string, _this:any):any{
          const id = _this.route.query.id;
          if(id){
            return Promise.resolve();
          }
          if (!value || value.trim() === '' ){
            return Promise.reject("请输入确认密码！")
          }else if (this.addModel.pass !== _this.addModel.confirmPass){
            return Promise.reject("两次输入的密码不一致！")
          }
          return Promise.resolve();
        }

        /**
         * 组件挂载后触发的事件
         */
        public mounted():void{
            const id = this.route.query.id;
            this.addOrUpdateForm = this.$refs.addOrUpdateForm;
            if (id){
                this.usernameDisabled=true;
                this.passDisabled = true;
                this.confirmPassDisabled = true;
                this.resetDisabled = true;
                this.getUserInfo(String(this.route.query.id));
            }
        }

        /**
         * 获取要编辑的详情
         * @param id
         */
        private getUserInfo(id:string):void{
            axios.get("/lion-upms-console-restful/user/console/details",{params:{id:id}})
                .then((data)=>{
                    this.addModel = data.data;
                    if (this.addModel.headPortraitVo){
                        this.headPortraitList=[{
                            uid:this.addModel.headPortraitVo.id,
                            name:this.addModel.headPortraitVo.originalFileName,
                            status:"done",
                            url:process.env.VUE_APP_BASEURL+process.env.VUE_APP_BASEAPI+this.addModel.headPortraitUrl
                        }];
                    }

                    delete this.addModel.headPortraitVo;
                }).catch(error=>{

            }).finally(()=>{

            });
        }

        /**
         * 保存
         */
        private save():void{
          (this.$refs.addOrUpdateForm as any).validate().then(()=>{
            this.saveButtonDisabled=true;
            const id = this.route.query.id;
            if(id){
              this.addModel.id=id;
              axios.put("/lion-upms-console-restful/user/console/update",this.addModel)
                  .then((data) =>{
                    if (Object(data).status === 200){
                      message.success(Object(data).message);
                    }
                  }).catch((fail)=>{
              }).finally(()=>{
                this.saveButtonDisabled=false;
              })
            }else{
              this.addModel.password = md5(this.addModel.pass);
              axios.post("/lion-upms-console-restful/user/console/add",this.addModel)
                  .then((data) =>{
                    if (Object(data).status === 200){
                      message.success(Object(data).message);
                    }
                  }).catch((fail)=>{
              }).finally(()=>{
                this.saveButtonDisabled=false;
              })
            }
          }).catch(fail=>{}).finally(()=>{})
        }

        /**
         * 上传头像回调事件
         */
        private headPortraitChange( obj:any):void{
            this.headPortraitList = obj.fileList;
            if (obj.file.status === 'done'){
                const response:any = eval('('+obj.file.xhr.response+')');
                this.addModel.headPortrait=response.data[0].id;
            }
        }

        /**
         * 头像删除事件
         */
        private headPortraitRemove(file:any):boolean{
            delete this.addModel.headPortrait;
            return true;
        }

        /**
         * 显示预览窗口
         */
        async handlePreview(file:any){
            if (!file.url && !file.preview) {
                file.preview = await this.getBase64(file.originFileObj);
            }
            this.previewImage = file.url || file.preview;
            this.previewVisible = true;
        }
        /**
         * 获取图片base64编码
         */
        private getBase64(file:any) {
            return new Promise((resolve, reject) => {
                const reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = () => resolve(reader.result);
                reader.onerror = error => reject(error);
            });
        }

        /**
         * 取消预览
         */
        private handleCancel():void {
            this.previewVisible = false;
        }

        /**
         * 返回
         */
        private back():void{
            this.$router.go(-1);
        }

        /**
         * 重置所有输入框
         */
        private reset():void{
            (this.$refs.addOrUpdateForm as any).resetFields();
            this.addModel = {};
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
    .ant-row >>> .ant-form-item-control{
        width: calc(100% - 80px);
    }

</style>