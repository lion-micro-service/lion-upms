<template>
    <div>
        <a-form-model layout="inline" ref="form" :model="addModel" :rules="rules" >
            <a-card class="card" :bordered="false">
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
                </a-row>

                <a-row >
                    <a-col :span="24" style="text-align:center;">
                        <a-form-item >
                            <a-button v-bind:disabled="saveButtonDisabled" type="primary" icon="save" @click="save()">保存</a-button>
                            <a-button v-bind:disabled="resetDisabled" type="dashed" icon="undo" @click="reset()">重置</a-button>
                            <a-button icon="rollback" @click="back()">返回</a-button>
                        </a-form-item>
                    </a-col>
                </a-row>
            </a-card>
        </a-form-model>
    </div>
</template>

<script lang="ts">
    import {Component, Emit, Inject, Model, Prop, Provide, Vue, Watch} from 'vue-property-decorator';
    import axios from "@lion/lion-front-core/src/network/axios";
    import { message } from 'ant-design-vue'
    let md5 = require('md5');
    @Component({})
    export default class AddOrUpdate extends Vue{

        private saveButtonDisabled:boolean = false;
        private usernameDisabled:boolean = false;
        private passDisabled:boolean = false;
        private confirmPassDisabled:boolean = false;
        private resetDisabled:boolean = false;

        private addModel : any={};

        private rules:any={
            username:[{required:true,validator:this.checkUsernameIsExist,trigger:'blur'}],
            pass:[{required:true,validator:this.validatorPass, trigger:'blur'}],
            confirmPass:[{required:true,validator:this.validatorConfimPass, trigger:'blur'}],
            email:[{validator:this.validatorEmail, trigger:'blur'}]
        };

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
                axios.get("/upms/user/console/exist",{params:{"username":this.addModel.username}})
                    .then((data)=> {
                        if (data.data.isExist) {
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

        private validatorEmail(rule :any, value:string, callback:any):void{
            const id = this.$route.query.id;
            if (value && value.trim() !== ''){
                axios.get("/upms/user/console/email/exist",{params:{email:value,id:id}})
                .then((data)=>{
                    if (data.data.isExist){
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

        private getUserInfo(id:string):void{
            axios.get("/upms/user/console/info",{params:{id:id}})
                .then((data)=>{
                    this.addModel = data.data.user;
                }).catch(error=>{

            }).finally(()=>{

            });
        }

        private save():void{
            (this.$refs.form as any).validate((validate: boolean) => {
                if (validate) {
                    this.saveButtonDisabled=true;
                    const id = this.$route.query.id;
                    if(id){
                        this.addModel.id=id;
                        axios.put("/upms/user/console/update",this.addModel)
                            .then((data) =>{
                                if((Object(data)).message){
                                    message.success((Object(data)).message);
                                }
                            }).catch((fail)=>{

                        }).finally(()=>{
                            this.saveButtonDisabled=false;
                        })
                    }else{
                        this.addModel.password = md5(this.addModel.pass);
                        axios.post("/upms/user/console/add",this.addModel)
                            .then((data) =>{
                                if((Object(data)).message){
                                    message.success((Object(data)).message);
                                }
                            }).catch((fail)=>{

                        }).finally(()=>{
                            this.saveButtonDisabled=false;
                        })
                    }
                }
            });
        };

        private back():void{
            this.$router.go(-1);
        };

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