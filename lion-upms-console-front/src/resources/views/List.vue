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
                            <a-button type="primary" icon="file-add" >添加</a-button>
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
    </div>
</template>

<script lang="ts">
    import {Component, Emit, Inject, Model, Prop, Provide, Vue, Watch} from 'vue-property-decorator';
    import axios from "@lion/lion-front-core/src/network/axios";

    @Component({})
    export default class List extends Vue{

        private loading:boolean=false;

        private scope:Array<any> = [];

        private searchModel : any ={
            scope:"CONSOLE",
            pageNumber:1,
            pageSize:999
        };

        private data:Array<any>=[];

        private columns :Array<any> = [
            { title: '名称', dataIndex: 'name', key: 'name',width: '400px' },
            { title: '编码', dataIndex: 'code', key: 'code'},
            { title: '作用域', dataIndex: 'scope.desc', key: 'scope' },
            { title: '类型', dataIndex: 'type.desc', key: 'type' },
            { title: '操作', key: 'action', scopedSlots: { customRender: 'action' },width: 180,}
        ];

        private mounted():void{
            axios.get("/common/enum/console/to/select",{params:{"enumClass":"com.lion.upms.entity.resources.enums.Scope"}})
                .then((data)=>{
                    this.scope=data.data.enum;
                })
                .catch(fail => {
                })
                .finally(()=>{
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