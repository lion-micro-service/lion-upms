<template>
    <a-modal destroyOnClose v-model="modal" width="1000px"  title="角色用户" centered @ok="save" :maskClosable="maskClosable" cancelText="关闭" okText="保存">
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
    import {Component, Vue} from 'vue-property-decorator';
    import axios from "@lion/lion-front-core/src/network/axios";
    import { message } from 'ant-design-vue';
    import SearchFrom from "@/components/user/searchFrom.vue";
    import List from "@/components/user/list.vue";
    import qs from "qs";
    @Component({
        components: {List, SearchFrom}
    })
    export default class roleUser extends Vue{
        //是否显示窗口
        private modal:boolean=false;
        //点击阴影层是否关闭窗口
        private maskClosable:boolean=false;
        //角色id
        private roleId?:string;
        //之前选中的用户
        private oldUserId:Array<string>=[];
        //本页列表所有的用户id
        private userId:Array<string>=[];
        //查询数据模型
        private searchModel : any ={
            pageNumber:1,
            pageSize:10
        }
        /**
         * 保存
         */
        private save():void{
            const list = (this.$refs.list as any);
            axios.post("/upms/role/console/save/user", {roleId:this.roleId,oldUserId:this.oldUserId,newUserId:list.selectedRowKeys})
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
            await axios.get("/upms/user/console/list",{params:this.searchModel})
            .then((data)=> {
                list.data = data.data.list;
                list.paginationProps.total = Number((Object(data)).totalElements);
                list.paginationProps.current = (Object(data)).pageNumber;
                list.paginationProps.pageSize = (Object(data)).pageSize;
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

            await axios.get("/upms/role/console/user",{params:{roleId: this.roleId,userId: this.userId},
                paramsSerializer: params => {
                    return qs.stringify(params, { indices: false })
                }})
            .then((data)=>{
                this.oldUserId= data.data.oldUserId
                list.selectedRowKeys=this.oldUserId;
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