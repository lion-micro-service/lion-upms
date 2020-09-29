<template>
    <a-modal destroyOnClose style="max-height: 500px;" v-model="modal" width="800px"  title="角色职位" centered @ok="save" :maskClosable="maskClosable" cancelText="关闭" okText="保存">

    </a-modal>
</template>

<script lang="ts">
    import {Component, Emit, Inject, Model, Prop, Provide, Vue, Watch} from 'vue-property-decorator';
    import axios from "@lion/lion-front-core/src/network/axios";
    import { message } from 'ant-design-vue';
    @Component({})
    export default class rolePosition extends Vue{
        private modal:boolean=false;
        private maskClosable:boolean=false;
        private roleId?:string;

        private save():void{

        }
    }
</script>

<style scoped>

</style>