<template>
    <a-modal destroyOnClose style="max-height: 500px;" v-model="modal" width="400px"  title="角色权限（资源）" centered @ok="save" :maskClosable="maskClosable" cancelText="关闭" okText="保存">
        <a-tree style="max-height: 450px;overflow:auto;overflow-x:auto;white-space:nowrap;white-space:nowrap;" v-model="checkedKeys" checkable :tree-data="treeData" @check="onCheck"/>
    </a-modal>
</template>

<script lang="ts">
    import {Component,  Vue} from 'vue-property-decorator';
    import axios from "@lion/lion-front-core/src/network/axios";
    import { message } from 'ant-design-vue';
    @Component({})
    export default class roleResources extends Vue{
        //是否显示窗口
        private modal:boolean=false;
        //点击阴影层是否关闭窗口
        private maskClosable:boolean=false;
        //选中的资源
        private checkedKeys:Array<string>=[];
        //作用域
        private scope:string="CONSOLE";
        //角色id
        private roleId?:string;
        //数据源
        private treeData:Array<any>=[];

        /**
         * 复选框选中事件
         * @param checkedKeys
         * @param info
         */
        private onCheck(checkedKeys:Array<string>, info:any):void {
            this.checkedKeys = checkedKeys;
        }

        /**
         * 弹出窗口
         */
        private async roleResources(){
            await axios.get("/upms/role/console/resources/tree", {params: {"scope": this.scope}})
            .then((data) => {
                this.treeData = data.data;
            })
            .catch(fail => {
            })
            .finally(() => {
            });
            await axios.get("/upms/role/console/resources", {params: {"roleId": this.roleId}})
            .then((data) => {
                this.checkedKeys = data.data;
            })
            .catch(fail => {
            })
            .finally(() => {
            });
        }

        /**
         * 保存
         */
        private save():void{
            axios.post("/upms/role/console/save/resources", {roleId:this.roleId,resourcesId:this.checkedKeys})
            .then((data) => {
                if (Object(data).status === 200){
                    message.success(Object(data).message);
                    this.checkedKeys=[];
                    this.modal = false;
                }
            })
            .catch(fail => {
            })
            .finally(() => {
            });
        }
    }
</script>

<style scoped>

</style>