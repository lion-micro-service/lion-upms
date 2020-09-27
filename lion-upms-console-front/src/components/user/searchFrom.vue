<template>
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
        <slot></slot>
    </a-form-model>
</template>

<script lang="ts">
    import {Component, Emit, Inject, Model, Prop, Provide, Vue, Watch} from 'vue-property-decorator';
    @Component({})
    export default class searchFrom extends Vue{
        private searchModel:any={};
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