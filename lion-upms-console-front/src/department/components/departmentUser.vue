<template>
    <a-modal v-model="modal" width="1000px"  title="部门用户" centered @ok="save" :maskClosable="maskClosable" cancelText="关闭" okText="保存">
        <search-from ref="searchFrom">
            <a-row >
                <a-col :span="24" style="text-align:right;">
                    <a-form-item>
                        <a-button type="primary" icon="search" @click="()=>{this.searchModel.pageNumber =1;search()}">查询</a-button>
                    </a-form-item>
                </a-col>
            </a-row>
        </search-from>

        <list @search="search" @set-page-info="setPageInfo" ref="list"></list>
    </a-modal>
</template>

<script lang="ts">
    import {Component, Emit, Inject, Model, Prop, Provide, Vue, Watch} from 'vue-property-decorator';
    import axios from "@lion/lion-front-core/src/network/axios";
    import { message } from 'ant-design-vue';
    import SearchFrom from "@/components/user/searchFrom.vue";
    import List from "@/components/user/list.vue";
    import qs from "qs";
    @Component({
        components: {List, SearchFrom}
    })
    export default class departmentUser extends Vue{
        private modal:boolean=false;
        private maskClosable:boolean=false;
        private departmentId?:string;
        private oldUserId:Array<string>=[];

        private userId:Array<string>=[];
        private save():void{
            const list = (this.$refs.list as any);
            axios.post("/upms/department/console/save/user", {departmentId:this.departmentId,oldUserId:this.oldUserId,newUserId:list.selectedRowKeys})
                .then((data) =>{
                    if (Object(data).status === 200){
                        message.success(Object(data).message);
                        this.modal=false;
                    }
                }).catch((fail)=>{
            }).finally(()=>{
            })

        }

        private searchModel : any ={
            pageNumber:1,
            pageSize:10
        }

        private setPageInfo(pageNumber:number,pageSize:number):void{
            this.searchModel.pageNumber=pageNumber;
            this.searchModel.pageSize=pageSize;
        }

        private async search(){
            const list = (this.$refs.list as any);
            list.selectedRowKeys=[];
            const searchFrom = (this.$refs.searchFrom as any);
            list.loading=true;
            const _this = this;
            this.searchModel={pageNumber:this.searchModel.pageNumber,pageSize:this.searchModel.pageSize};
            if (searchFrom.searchModel){
                Object.keys(searchFrom.searchModel).forEach(function(key){
                    _this.searchModel[key]=searchFrom.searchModel[key];
                });
            }
            let _data:any={};
            let total:number=0;
            let current:number=0;
            let pageSize:number=0;
            await axios.get("/upms/user/console/list",{params:this.searchModel})
            .then((data)=> {
                _data = data.data.list;
                total = Number((Object(data)).totalElements);
                current = (Object(data)).pageNumber;
                pageSize = (Object(data)).pageSize;
                this.userId=[];
                for(let j:number = 0,len=data.data.list.length; j < len; j++) {
                    this.userId[j]=(data.data.list[j].id);
                }
            })
            .catch(fail => {
            })
            .finally(()=>{
                list.loading=false;
            });

            await axios.get("/upms/department/console/user",{params:{id: this.departmentId,userId: this.userId},
            paramsSerializer: params => {
                return qs.stringify(params, { indices: false })
            }})
            .then((data)=>{
                this.oldUserId= data.data.oldUserId
                list.notCheckUserId=data.data.notCheckUserId;

                list.data = _data;
                list.selectedRowKeys=this.oldUserId;
                list.paginationProps.total = total;
                list.paginationProps.current = current;
                list.paginationProps.pageSize = pageSize;
            })
            .catch(fail => {
            })
            .finally(()=>{
            });
        }


    }
</script>

<style lang="css" scoped>
    .ant-table-wrapper {
        max-height: 350px;
        overflow:auto;
        overflow-x:auto;
        white-space:nowrap;
        white-space:nowrap;
    }
</style>