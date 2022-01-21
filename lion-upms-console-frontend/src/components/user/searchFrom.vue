<template>
    <a-form ref="from" :model="searchModel" >
        <a-row>
            <a-col :span="6">
                <a-form-item label="登陆账号" name="username" ref="username" >
                    <a-input placeholder="请输入登陆账号" v-model:value="searchModel.username"/>
                </a-form-item>
            </a-col>
            <a-col :span="6">
                <a-form-item label="姓名" name="name" ref="name" >
                    <a-input placeholder="请输入姓名" v-model:value="searchModel.name"/>
                </a-form-item>
            </a-col>
            <a-col :span="6">
                <a-form-item label="年龄" name="age" ref="age" >
                    <a-input-number placeholder="请输入年龄" v-model:value="searchModel.age"/>
                </a-form-item>
            </a-col>
            <a-col :span="6">
                <a-form-item label="邮箱" name="email" ref="email" >
                    <a-input placeholder="请输入邮箱" v-model:value="searchModel.email"/>
                </a-form-item>
            </a-col>
        </a-row>
        <a-row>
            <a-col :span="6">
                <a-form-item label="生日" name="birthday" ref="birthday" >
                    <a-date-picker placeholder="请输入生日" v-model:value="searchModel.birthday" />
                </a-form-item>
            </a-col>
            <a-col :span="6">
                <a-form-item label="部门" name="departmentId" ref="departmentId" >
                    <a-tree-select v-model:value="searchModel.departmentId" :tree-data="department" tree-checkable :show-checked-strategy="SHOW_PARENT" search-placeholder="请选择部门"/>
                </a-form-item>
            </a-col>
            <a-col :span="6">
                <a-form-item label="角色" name="roleId" ref="roleId" >
                    <a-select allowClear v-model:value="searchModel.roleId" >
                        <a-select-option :key="value.id" v-for="(value) in role" :value="value.id">{{value.name}}</a-select-option>
                    </a-select>
                </a-form-item>
            </a-col>
        </a-row>
        <slot></slot>
    </a-form>
</template>

<script lang="ts">
    import {Options,  Vue} from 'vue-property-decorator';
    import moment from 'moment';
    import 'moment/locale/zh-cn';
    import axios from "@lion/lion-frontend-core/src/network/axios";
    import { TreeSelect } from 'ant-design-vue';
    @Options({components:{}})
    export default class searchFrom extends Vue{
        //antdv组件汉化
        private moment:any = moment;
        //部门tree下拉框数据
        private department:Array<any>=[];
        //角色下拉框数据
        private role:Array<any>=[];
        //查询的参数模型
        private searchModel:any={};
        private SHOW_PARENT:any = Object(TreeSelect).SHOW_PARENT;
        //组件挂载后调用（初始化数据）
        public mounted():void{
            //获取部门数据
            axios.get("/lion-upms-console-restful/department/console/list/tree",{params:{}})
            .then((data)=>{
                this.department=[];
                let list:Array<any> = data.data
                for(let j = 0; j < list.length; j++) {
                    this.department[j] = {
                        title:list[j].name,
                        value:list[j].id,
                        key:list[j].id,
                        children: this.convertChildren(list[j].children)
                    };
                }
            })
            .catch(fail => {
            })
            .finally(()=>{
            });
            axios.get("/lion-upms-console-restful/role/console/list",{params:{pageSize:9999}})
            .then((data)=>{
                this.role=data.data;
            })
            .catch(fail => {
            })
            .finally(()=>{
            });
        }

        /**
         * 部门tree下拉框数据转换
         * @param list
         */
        private convertChildren(list:Array<any>):Array<any>{
            if (list && list.length>0) {
                let treeData: Array<any> = [];
                for (let j = 0; j < list.length; j++) {
                    treeData[j] = {
                        title: list[j].name,
                        value: list[j].id,
                        key: list[j].id,
                        children: this.convertChildren(list[j].children)
                    };
                }
                return treeData;
            }
            return [];
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