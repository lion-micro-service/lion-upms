<template>
    <div>
        <a-card class="card" :bordered="false">
            <a-form-model layout="inline" ref="from" :model="searchModel" >
                <a-row>
                    <a-col :span="6">
                        <a-form-model-item label="名称" prop="name" ref="name" >
                            <a-input placeholder="请输入名称" v-model="searchModel.name"/>
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="6">
                    </a-col>
                    <a-col :span="6">
                        <a-form-model-item label="编码" prop="code" ref="code" >
                            <a-input placeholder="请输入编码" v-model="searchModel.code"/>
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="6">
                    </a-col>
                </a-row>
                <a-row >
                    <a-col :span="24" style="text-align:right;">
                        <a-form-item>
                            <a-button type="primary" icon="search" @click="search()">查询</a-button>
                            <a-button type="primary" icon="file-add" @click="add()">添加</a-button>
                            <a-button type="danger" icon="delete" @click="del()">删除</a-button>
                        </a-form-item>
                    </a-col>
                </a-row>
            </a-form-model>
        </a-card>

        <a-card :bordered="false">
            <a-table :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }" rowKey="id" :columns="columns" :dataSource="data" :loading="loading" :pagination="paginationProps">
                <span slot="action" slot-scope="text, record">
                    <a-button icon="edit" size="small" @click="edit(record.id)">修改</a-button>
                    <a-button type="danger" icon="delete" size="small" @click='del(record.id)'>删除</a-button>
                </span>
            </a-table>
        </a-card>

        <add-or-update ref="addOrUpdate"></add-or-update>
    </div>
</template>

<script lang="ts">
    import {Component, Emit, Inject, Model, Prop, Provide, Vue, Watch} from 'vue-property-decorator';
    import axios from "@lion/lion-front-core/src/network/axios";
    import addOrUpdate from "@/role/components/addOrUpdate.vue";
    import { message } from 'ant-design-vue';
    @Component({components:{addOrUpdate}})
    export default class List extends Vue{
        private searchModel : any ={
            pageNumber:1,
            pageSize:10
        }
        private selectedRowKeys:Array<number> = [];
        private data:Array<any> = [];
        private loading:boolean=false;
        private columns :Array<any> = [
            { title: '编码', dataIndex: 'code', key: 'code' },
            { title: '名称', dataIndex: 'name', key: 'name'},
            { title: '操作', key: 'action', scopedSlots: { customRender: 'action' },width: 180,}
        ];
        private onSelectChange(selectedRowKeys:Array<number>):void{
            this.selectedRowKeys = selectedRowKeys;
        }
        private paginationProps:any={
            showSizeChanger: false,
            showQuickJumper: true,
            hideOnSinglePage:false,
            pageSizeOptions:['10', '20', '30', '40','50','60','70','80','90','100'],
            total:0,
            current:1,
            pageSize:10,
            showSizeChange: (pageNumber:number, pageSize: number)=>this.paginationSearch(pageNumber,pageSize),
            onChange: (pageNumber:number, pageSize: number)=>this.paginationSearch(pageNumber,pageSize),
        };
        private paginationSearch(pageNumber:number, pageSize: number):void{
            this.searchModel.pageNumber=pageNumber;
            this.searchModel.pageSize=pageSize;
            this.search();
        }
        private search():void{
            this.loading=true;
            axios.get("/upms/role/console/list",{params:this.searchModel})
            .then((data)=>{
                this.data=data.data.list;
                this.paginationProps.total=Number((Object(data)).totalElements);
                this.paginationProps.current=(Object(data)).pageNumber;
                this.paginationProps.pageSize=(Object(data)).pageSize;
            })
            .catch(fail => {
            })
            .finally(()=>{
                this.loading=false;
            });
        }
        @Watch("$route", { immediate: true,deep: true })
        private onRouteChange(route: any):void {
            if (route.path === "/role/list"){
                this.search();
            }
        }

        private add():void{
            (this.$refs.addOrUpdate as any).addOrUpdateModal=true;
        }
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