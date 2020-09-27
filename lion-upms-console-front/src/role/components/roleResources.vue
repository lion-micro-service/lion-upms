<template>
    <a-modal style="max-height: 500px;" v-model="modal" width="400px"  title="角色权限（资源）" centered @ok="save" :maskClosable="maskClosable" cancelText="关闭" okText="保存">
        <a-tree style="max-height: 450px;overflow:auto;overflow-x:auto;white-space:nowrap;white-space:nowrap;" v-model="checkedKeys" checkable :tree-data="treeData" @check="onCheck"/>
    </a-modal>
</template>

<script lang="ts">
    import {Component, Emit, Inject, Model, Prop, Provide, Vue, Watch} from 'vue-property-decorator';
    import axios from "@lion/lion-front-core/src/network/axios";
    import { message } from 'ant-design-vue';
    @Component({})
    export default class roleResources extends Vue{
        private modal:boolean=false;
        private maskClosable:boolean=false;
        private checkedKeys:Array<string>=[];
        private scope:string="CONSOLE";
        private roleId?:string;
        private treeData:Array<any>=[];

        private onCheck(checkedKeys:Array<string>, info:any):void {
            this.checkedKeys = checkedKeys;
        }

        private async roleResources(){
            this.checkedKeys=[];
            this.treeData=[];
            await axios.get("/upms/role/console/resources/tree", {params: {"scope": this.scope}})
            .then((data) => {
                this.treeData = data.data.resources;
            })
            .catch(fail => {
            })
            .finally(() => {
            });
            await axios.get("/upms/role/console/resources", {params: {"roleId": this.roleId}})
            .then((data) => {
                this.checkedKeys = data.data.checkedKeys;
            })
            .catch(fail => {
            })
            .finally(() => {
            });
        }

        private save():void{
            axios.post("/upms/role/console/add/resources", {roleId:this.roleId,resourcesId:this.checkedKeys})
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