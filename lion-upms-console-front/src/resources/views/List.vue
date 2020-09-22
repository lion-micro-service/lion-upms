<template>
    <div>
        <a-card class="card" :bordered="false">
            <a-form-model layout="inline" ref="from" :model="searchModel" >
                <a-row>
                    <a-col :span="6">
                        <a-form-model-item label="作用域" prop="scope" ref="scope" >
                            <a-select  v-model="searchModel.scope">
                                <a-select-option :key="value.key" v-for="(value) in scope" :value="value.name">{{value.desc}}</a-select-option>
                            </a-select>
                        </a-form-model-item>
                    </a-col>
                </a-row>
                <a-row >
                    <a-col :span="24" style="text-align:right;">
                        <a-form-item>
                            <a-button type="primary" icon="search" @click="search()" >查询</a-button>
                            <a-button type="primary" icon="file-add" @click="() => (addOrUpdateModal = true)" >添加</a-button>
                        </a-form-item>
                    </a-col>
                </a-row>
            </a-form-model>
        </a-card>

        <a-card :bordered="false">
            <a-table rowKey="id" :columns="columns" :dataSource="data" :loading="loading" :pagination="false">
                <span slot="action" slot-scope="text, record">
                    <a-button icon="edit" size="small" @click="edit(record.id)">修改</a-button>
                    <a-button type="danger" icon="delete" size="small" @click='del(record.id)'>删除</a-button>
                </span>
            </a-table>
        </a-card>

        <a-modal v-model="addOrUpdateModal" width="800px" title="添加/修改资源" centered @ok="() => (addOrUpdateModal = false)">
            <a-form-model layout="inline" ref="addOrUpdateForm" :model="addOrUpdateModel" :rules="rules" >
                <a-row>
                    <a-col :span="12">
                        <a-form-model-item label="作用域" prop="scope" ref="scope" >
                            <a-select  v-model="addOrUpdateModel.scope">
                                <a-select-option :key="value.key" v-for="(value) in scope" :value="value.name">{{value.desc}}</a-select-option>
                            </a-select>
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-model-item label="类型" prop="type" ref="type" >
                            <a-select  v-model="addOrUpdateModel.type">
                                <a-select-option :key="value.key" v-for="(value) in type" :value="value.name">{{value.desc}}</a-select-option>
                            </a-select>
                        </a-form-model-item>
                    </a-col>
                </a-row>
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
                    <a-col :span="12">
                        <a-form-model-item label="URL" prop="url" ref="url">
                            <a-input placeholder="请输入URL" v-model="addOrUpdateModel.url" />
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-model-item label="排序" prop="sort" ref="sort">
                            <a-input-number   v-model="addOrUpdateModel.sort" />
                        </a-form-model-item>
                    </a-col>
                </a-row>
            </a-form-model>
        </a-modal>
    </div>
</template>

<script lang="ts">
    import {Component, Emit, Inject, Model, Prop, Provide, Vue, Watch} from 'vue-property-decorator';
    import axios from "@lion/lion-front-core/src/network/axios";

    @Component({})
    export default class List extends Vue{

        private loading:boolean=false;

        private addOrUpdateModal:boolean=false;

        private scope:Array<any> = [];

        private type:Array<any> = [];

        private addOrUpdateModel:any ={
            scope:"CONSOLE",
            type:"CATALOG",
            sort:0,
        };

        private searchModel : any ={
            scope:"CONSOLE",
            pageNumber:1,
            pageSize:999
        };

        private rules:any={};

        private data:Array<any>=[];

        private columns :Array<any> = [
            { title: '名称', dataIndex: 'name', key: 'name',width: '200px' },
            { title: '编码', dataIndex: 'code', key: 'code',width: '200px'},
            { title: '作用域', dataIndex: 'scope.desc', key: 'scope' },
            { title: '类型', dataIndex: 'type.desc', key: 'type' },
            { title: 'url', dataIndex: 'url', key: 'url',width: '200px' },
            { title: '排序', dataIndex: 'sort', key: 'sort' },
            { title: '操作', key: 'action', scopedSlots: { customRender: 'action' },width: 180,}
        ];

        private async mounted() {
            await axios.get("/common/enum/console/to/select", {params: {"enumClass": "com.lion.upms.entity.resources.enums.Scope"}})
                .then((data) => {
                    this.scope = data.data.enum;
                })
                .catch(fail => {
                })
                .finally(() => {
                });
            await axios.get("/common/enum/console/to/select", {params: {"enumClass": "com.lion.upms.entity.resources.enums.Type"}})
                .then((data) => {
                    this.type = data.data.enum;
                })
                .catch(fail => {
                })
                .finally(() => {
                });
            this.search();
        }

        private search():void{
            this.loading=true;
            axios.get("/upms/resources/console/list/tree",{params:this.searchModel})
                .then((data)=>{
                    this.data=data.data.list;
                })
                .catch(fail => {
                })
                .finally(()=>{
                    this.loading=false;
                });
        }

        private addOrUpdate(id:number):void{

        }


        // @Watch("$route", { immediate: true,deep: true })
        // private onRouteChange(route: any):void {
        //     if (route.path === "/resources/list"){
        //         this.search();
        //     }
        // }


    }
</script>

<style lang="css" scoped>
    .ant-form-item >>> .ant-form-item-label{
        width: 50px;
    }
    .ant-form-item{
        width: 100%;
    }
    .ant-row >>> .ant-form-item-control-wrapper{
        width: calc(100% - 50px);
    }
</style>