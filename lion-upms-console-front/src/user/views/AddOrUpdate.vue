<template>
    <a-card class="card" :bordered="false">
        <a-form-model layout="inline" ref="form" :model="addModel" :rules="rules" >
            <a-row >
                <a-col :span="8">
                    <a-form-model-item label="登陆账号" prop="username" ref="username" >
                        <a-input v-bind:disabled="usernameDisabled" placeholder="请输入登陆账号" v-model="addModel.username"  autocomplete="off"/>
                    </a-form-model-item>
                </a-col>
                <a-col :span="8">
                    <a-form-model-item label="密码" prop="pass" ref="pass" >
                        <a-input-password v-bind:disabled="passDisabled" placeholder="请输入密码" v-model="addModel.pass" autocomplete="off"/>
                    </a-form-model-item>
                </a-col>
                <a-col :span="8">
                    <a-form-model-item label="确认密码" prop="confirmPass" ref="confirmPass" >
                        <a-input-password v-bind:disabled="confirmPassDisabled" placeholder="请输入确认密码" v-model="addModel.confirmPass" autocomplete="off"/>
                    </a-form-model-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="8">
                    <a-form-model-item label="姓名" prop="name" ref="name" >
                        <a-input placeholder="请输入姓名" v-model="addModel.name" />
                    </a-form-model-item>
                </a-col>
                <a-col :span="8">
                    <a-form-model-item label="邮箱" prop="email" ref="email">
                        <a-input placeholder="请输入邮箱" v-model="addModel.email" />
                    </a-form-model-item>
                </a-col>
                <a-col :span="8">
                    <a-form-model-item label="年龄" prop="age" ref="age">
                        <a-input-number  placeholder="请输入年龄" v-model="addModel.age" />
                    </a-form-model-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="8">
                    <a-form-model-item label="生日" prop="birthday" ref="birthday" >
                        <a-date-picker placeholder="请输入生日" valueFormat="YYYY-MM-DD" v-model="addModel.birthday" />
                    </a-form-model-item>
                </a-col>
                <a-col :span="8">
                    <a-form-model-item label="头像" prop="headPortrait" ref="headPortrait" >
                        <a-upload :action="uploadAction" accept="image/png, image/jpeg" list-type="picture-card" :file-list="headPortraitList" @change="(e)=>headPortraitChange(e)" :remove="function(file){headPortraitRemove(file)} " @preview="handlePreview">
                            <div v-if="headPortraitList.length < 1">
                                <a-icon type="plus" />
                                <div class="ant-upload-text">
                                    上传
                                </div>
                            </div>

                        </a-upload>
                    </a-form-model-item>
                </a-col>
            </a-row>

            <a-row >
                <a-col :span="24" style="text-align:center;">
                    <a-form-item >
                        <a-button style="margin-left: 5px;" v-bind:disabled="saveButtonDisabled" type="primary" icon="save" @click="save()">保存</a-button>
                        <a-button style="margin-left: 5px;" v-bind:disabled="resetDisabled" type="dashed" icon="undo" @click="reset()">重置</a-button>
                        <a-button style="margin-left: 5px;" icon="rollback" @click="back()">返回</a-button>
                    </a-form-item>
                </a-col>
            </a-row>
        </a-form-model>

        <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
        </a-modal>
    </a-card>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import axios from "@lion/lion-front-core/src/network/axios";
    import { message } from 'ant-design-vue'
    import moment from 'moment';
    import 'moment/locale/zh-cn';
    let md5 = require('md5');
    @Component({})
    export default class AddOrUpdate extends Vue{
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
        private uploadAction:string=process.env.VUE_APP_BASEURL+"/common/file/console/upload"
        //头像地址
        private headPortraitList:Array<any>=[];
        //头像预览窗口是否显示
        private previewVisible:boolean=false;
        //预览图片
        private previewImage:any="";
        //校验规则
        private rules:any={
            username:[{required:true,validator:this.checkUsernameIsExist,trigger:'blur'}],
            pass:[{required:true,validator:this.validatorPass, trigger:'blur'}],
            confirmPass:[{required:true,validator:this.validatorConfimPass, trigger:'blur'}],
            email:[{validator:this.checkEmailIsExist, trigger:'blur'}]
        };

        /**
         * 检查登陆用户张号是否存在
         * @param rule
         * @param value
         * @param callback
         */
        private checkUsernameIsExist(rule :any, value:string, callback:any):void{
            const id = this.$route.query.id;
            if(id){
                callback();
                return;
            }
            if (!value || value.trim() === ''){
                callback(new Error('请输入登陆账号'));
                return;
            }else if (value && value.trim() !== ''){
                axios.get("/upms/user/console/check/username/exist",{params:{"username":this.addModel.username}})
                    .then((data)=> {
                        if (Object(data).status !== 200){
                            callback(new Error('异常错误！请检查'));
                            return;
                        }
                        if (data.data) {
                            callback(new Error('该账号已存在'));
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
         * 检查邮箱是否存在
         * @param rule
         * @param value
         * @param callback
         */
        private checkEmailIsExist(rule :any, value:string, callback:any):void{
            const id = this.$route.query.id;
            if (value && value.trim() !== ''){
                axios.get("/upms/user/console/check/email/exist",{params:{email:value,id:id}})
                .then((data)=>{
                    if (Object(data).status !== 200){
                        callback(new Error('异常错误！请检查'));
                        return;
                    }
                    if (data.data){
                        callback(new Error('该邮箱已存在'));
                    }else {
                        callback();
                    }
                }).catch(error=>{

                }).finally(()=>{

                })
            }else{
                callback();
            }
        }

        /**
         * 校验密码
         * @param rule
         * @param value
         * @param callback
         */
        private validatorPass(rule :any, value:string, callback:any):void{
            const id = this.$route.query.id;
            if(id){
                callback();
                return;
            }
            if (!value || value.trim() === ''){
                callback(new Error('请输入密码'));
            }else{
                if (this.addModel.confirmPass && this.addModel.confirmPass.trim() !== ''){
                    (this.$refs.form as any).validateField("confirmPass");
                }
                callback();
            }
        }

        /**
         * 校验确认密码
         * @param rule
         * @param value
         * @param callback
         */
        private validatorConfimPass(rule :any, value:string, callback:any):void{
            const id = this.$route.query.id;
            if(id){
                callback();
                return;
            }
            if (!value || value.trim() === '' ){
                callback(new Error("请输入确认密码！"));
            }else if (this.addModel.pass !== this.addModel.confirmPass){
                callback(new Error("两次输入的密码不一致！"));
            }
            callback();
        }

        /**
         * 组件挂载后触发的事件
         */
        private mounted():void{
            const id = this.$route.query.id;
            if (id){
                this.usernameDisabled=true;
                this.passDisabled = true;
                this.confirmPassDisabled = true;
                this.resetDisabled = true;
                this.getUserInfo(String(this.$route.query.id));
            }
        }

        /**
         * 获取要编辑的详情
         * @param id
         */
        private getUserInfo(id:string):void{
            axios.get("/upms/user/console/details",{params:{id:id}})
                .then((data)=>{
                    this.addModel = data.data;
                    if (this.addModel.headPortraitVo){
                        this.headPortraitList=[{
                            uid:this.addModel.headPortraitVo.id,
                            name:this.addModel.headPortraitVo.originalFileName,
                            status:"done",
                            url:process.env.VUE_APP_BASEURL+this.addModel.headPortraitVo.url
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
            (this.$refs.form as any).validate((validate: boolean) => {
                if (validate) {
                    this.saveButtonDisabled=true;
                    const id = this.$route.query.id;
                    if(id){
                        this.addModel.id=id;
                        axios.put("/upms/user/console/update",this.addModel)
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
                        axios.post("/upms/user/console/add",this.addModel)
                        .then((data) =>{
                            if (Object(data).status === 200){
                                message.success(Object(data).message);
                            }
                        }).catch((fail)=>{
                        }).finally(()=>{
                            this.saveButtonDisabled=false;
                        })
                    }
                }
            });
        }

        /**
         * 上传头像回调事件
         */
        private headPortraitChange( obj:any):void{
            this.headPortraitList = obj.fileList;
            if (obj.file.status === 'done'){
                const response:any = eval('('+obj.file.xhr.response+')');
                this.addModel.headPortrait=response.data.files[0].id;
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
            (this.$refs.form as any).resetFields();
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
    .ant-row >>> .ant-form-item-control-wrapper{
        width: calc(100% - 80px);
    }

</style>