<template>
    <a-modal v-model="addOrUpdateModal" width="800px" title="添加/修改角色" centered @ok="addOrUpdate" cancelText="关闭" okText="保存">
        <a-form-model layout="inline" ref="addOrUpdateForm" :model="addOrUpdateModel" :rules="rules" >
            <a-row>
                <a-col :span="12">
                    <a-form-model-item label="名称" prop="name" ref="name">
                        <a-input placeholder="请输入名称" v-model="addOrUpdateModel.name" />
                    </a-form-model-item>
                </a-col>
                <a-col :span="12">
                    <a-form-model-item label="编码" prop="code" ref="code">
                        <a-input placeholder="请输入编码" v-model="addOrUpdateModel.code" />
                    </a-form-model-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="24">
                    <a-form-model-item  id="textarea" label="备注" prop="remark" ref="remark">
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
        private addOrUpdateModal:boolean=false;
        private addOrUpdateModel:any={};
        private rules:any={};

        private addOrUpdate():void{

        }
    }
</script>

<style scoped>

</style>