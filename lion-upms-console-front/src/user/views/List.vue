<template>
    <div>
        <a-card class="card" :bordered="false">
            <search-from ref="searchFrom">
                <a-row >
                    <a-col :span="24" style="text-align:right;">
                        <a-form-item>
                            <a-button type="primary" icon="search"  @click="()=>{this.searchModel.pageNumber =1;search()}">查询</a-button>
                            <a-button type="primary" icon="file-add" @click="add()">新增</a-button>
                            <a-button type="danger" icon="delete"  @click="del(null)">删除</a-button>
                        </a-form-item>
                    </a-col>
                </a-row>
            </search-from>
        </a-card>

        <a-card :bordered="false">
            <list @edit="edit" @del="del" @search="search" @set-page-info="setPageInfo" ref="list">

            </list>
        </a-card>
    </div>
</template>

<script lang="ts">
    import {Component, Emit, Inject, Model, Prop, Provide, Vue, Watch} from 'vue-property-decorator';
    import axios from "@lion/lion-front-core/src/network/axios";
    import { message } from 'ant-design-vue';
    import qs from 'qs';
    import searchFrom from "@/components/user/searchFrom.vue";
    import list from "@/components/user/list.vue";
    @Component({components:{searchFrom,list}})
    export default class List extends Vue{
        private isMounted:boolean=false;
        private mounted():void {
            const list = (this.$refs.list as any);
            list.columns[list.columns.length]={ title: '操作', key: 'action', scopedSlots: { customRender: 'action' },width: 180,};
            this.search();
            this.isMounted=true;
        }

        private searchModel : any ={
            pageNumber:1,
            pageSize:10
        }

        private setPageInfo(pageNumber:number,pageSize:number):void{
            this.searchModel.pageNumber=pageNumber;
            this.searchModel.pageSize=pageSize;
        }

        private search():void{
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
            axios.get("/upms/user/console/list",{params:this.searchModel})
            .then((data)=>{
                list.data=data.data.list;
                list.paginationProps.total=Number((Object(data)).totalElements);
                list.paginationProps.current=(Object(data)).pageNumber;
                list.paginationProps.pageSize=(Object(data)).pageSize;
            })
            .catch(fail => {
            })
            .finally(()=>{
                list.loading=false;
            });
        }

        private add():void{
            this.$router.push("/user/add");
        }

        private edit(id:string):void{
            this.$router.push({ path: '/user/add', query: { id: id }});
        }

        @Watch("$route", { immediate: true,deep: true })
        private onRouteChange(route: any):void {
            if (this.isMounted && route.path === "/user/list"){
                this.search();
            }
        }

        private del(id:any):void{
            const _this =this;
            const list = (this.$refs.list as any);
            if (!id){
                if (list.selectedRowKeys.length <=0 ){
                    message.error("请选择要删除的数据");
                    return;
                }else{
                    id = list.selectedRowKeys;
                }
            }
            this.$confirm({
                title: '是否要删除该数据?',
                // content: '',
                okText: 'Yes',
                okType: 'danger',
                cancelText: 'No',
                onOk() {
                    _this.delete(id);
                },
                onCancel() {
                },
            });

        }

        private delete(id:any):void{
            axios.delete("/upms/user/console/delete",{params:{id:id},
            paramsSerializer: params => {
                return qs.stringify(params, { indices: false })
            }})
            .then((data)=>{
                if((Object(data)).status === 200 && (Object(data)).message){
                    message.success((Object(data)).message);
                    this.search();
                }
            }).catch((fail)=>{
            }).finally(()=>{
            });
        }
    }
</script>

<style lang="css" scoped>
    .ant-form-item >>> .ant-form-item-label{
        width: 80px;
    }
    .ant-form-item{
        width: 100%;
    }
    .ant-row >>> .ant-form-item-control-wrapper{
        width: calc(100% - 80px);
    }
</style>
