<template>
    <div>
        <a-card class="card" style="border-bottom-width: 5px;" >
            <search-from ref="searchFrom" >
                <a-row >
                    <a-col :span="24" style="text-align:right;">
                        <a-form-item>
                            <a-button style="margin-left: 5px;" type="primary" v-if="getAuthority('SYSTEM_SETTINGS_USER_LIST')" icon="search"  @click="()=>{this.searchModel.pageNumber =1;search()}">查询</a-button>
                            <a-button style="margin-left: 5px;" type="primary" v-if="getAuthority('SYSTEM_SETTINGS_USER_ADD')"  icon="file-add" @click="add()">新增</a-button>
                            <a-button style="margin-left: 5px;" type="danger" v-if="getAuthority('SYSTEM_SETTINGS_USER_DELETE')" icon="delete"  @click="del(null)">删除</a-button>
                        </a-form-item>
                    </a-col>
                </a-row>
            </search-from>
        </a-card>

        <a-card v-if="getAuthority('SYSTEM_SETTINGS_USER_LIST')" :bordered="false">
            <list @edit="edit" @del="del" @search="search" @set-page-info="setPageInfo" ref="list">

            </list>
        </a-card>
    </div>
</template>

<script lang="ts">
    import {Options, Vue, Watch} from 'vue-property-decorator';
    import axios from "@lion/lion-frontend-core/src/network/axios";
    import { message,Modal } from 'ant-design-vue';
    import qs from 'qs';
    import searchFrom from "@/components/user/searchFrom.vue";
    import list from "@/components/user/list.vue";
    import authority from "@lion/lion-frontend-core/src/security/authority";
    @Options({components:{searchFrom,list}})
    export default class List extends Vue{
        //组件是否已经挂载
        private isMounted:boolean=false;
        //查询数据模型
        private searchModel : any ={
            pageNumber:1,
            pageSize:10
        }

        /**
         * 组件挂载后触发事件
         */
        public mounted():void {
            const list = (this.$refs.list as any);
            list.columns[list.columns.length]={ title: '操作', key: 'action', scopedSlots: { customRender: 'action' },width: 180,};
            this.search();
            this.isMounted=true;
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
        private search():void{
            if (!this.getAuthority('SYSTEM_SETTINGS_USER_LIST')){
                return;
            }
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
            axios.get("/lion-upms-console-restful/user/console/list",{params:this.searchModel,
                paramsSerializer: params => {
                    return qs.stringify(params, { indices: false })
                }})
            .then((data)=>{
                list.listData=data.data;
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

        /**
         * 跳转到新增页面
         */
        private add():void{
            this.$router.push("/user/add");
        }

        /**
         * 跳转到编辑页面
         * @param id
         */
        private edit(id:string):void{
            this.$router.push({ path: '/user/add', query: { id: id }});
        }

        /**
         * 路由监控，返回到本路由的时候触发查询事件，（当从新增/修改页面返回时触发查询事件。获取最新数据）
         * @param route
         */
        @Watch("$route", { immediate: true,deep: true })
        private onRouteChange(route: any):void {
            if (this.isMounted && route.path === "/user/list"){
                this.search();
            }
        }

        /**
         * 弹出删除警示
         * @param id
         */
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
            Modal.confirm({
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

        /**
         * 删除
         * @param id
         */
        private delete(id:any):void{
            axios.delete("/lion-upms-console-restful/user/console/delete",{params:{id:id},
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
                const list = (this.$refs.list as any);
                list.selectedRowKeys=[];
            });
        }

        /**
         * 判断(获取)是否有权限
         */
        private getAuthority(authorityCode:string):any{
            return authority(authorityCode);
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
