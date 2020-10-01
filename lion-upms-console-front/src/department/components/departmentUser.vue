<template>
    <a-modal destroyOnClose v-model="modal" width="1000px"  title="部门用户" centered @ok="save" :maskClosable="maskClosable" cancelText="关闭" okText="保存">
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
    import {Component,  Vue} from 'vue-property-decorator';
    import axios from "@lion/lion-front-core/src/network/axios";
    import { message } from 'ant-design-vue';
    import SearchFrom from "@/components/user/searchFrom.vue";
    import List from "@/components/user/list.vue";
    import qs from "qs";
    @Component({
        components: {List, SearchFrom}
    })
    export default class departmentUser extends Vue{
        //是否显示该modal（组件）
        private modal:boolean=false;
        //是否点击阴影层关闭该modal（组件）
        private maskClosable:boolean=false;
        //部门id用户接口调用传值
        private departmentId?:string;
        //已经选中的用户
        private oldUserId:Array<string>=[];
        //该列表页所有的用户（用户接口调用传值）
        private userId:Array<string>=[];

        /**
         * 保存部门所关联的用户
         */
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

        /**
         * 查询数据模型
         */
        private searchModel : any ={
            pageNumber:1,
            pageSize:10
        }

        /**
         * 设置分页信息
         * @param pageNumber
         * @param pageSize
         */
        private setPageInfo(pageNumber:number,pageSize:number):void{
            this.searchModel.pageNumber=pageNumber;
            this.searchModel.pageSize=pageSize;
        }

        /**
         * 查询
         */
        private async search(){
            const list = (this.$refs.list as any);
            //重置子组件选择了的用户（初始化）
            list.selectedRowKeys=[];
            const searchFrom = (this.$refs.searchFrom as any);
            list.loading=true;
            const _this = this;
            //重置查询模型（避免历史数据造成干扰）
            this.searchModel={pageNumber:this.searchModel.pageNumber,pageSize:this.searchModel.pageSize};
            //获取子组件填写的查询数据
            if (searchFrom.searchModel){
                Object.keys(searchFrom.searchModel).forEach(function(key){
                    _this.searchModel[key]=searchFrom.searchModel[key];
                });
            }
            let _data:any={};
            let total:number=0;
            let current:number=0;
            let pageSize:number=0;
            //获取用户列表数据
            await axios.get("/upms/user/console/list",{params:this.searchModel})
            .then((data)=> {
                _data = data.data.list;
                total = Number((Object(data)).totalElements);
                current = (Object(data)).pageNumber;
                pageSize = (Object(data)).pageSize;
                this.userId=[];
                for(let j:number = 0,len=data.data.list.length; j < len; j++) {
                    this.userId[j]=(data.data.list[j].user.id);
                }
            })
            .catch(fail => {
            })
            .finally(()=>{
                list.loading=false;
            });
            //获取已经选中的用户以及不能选择的用户
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