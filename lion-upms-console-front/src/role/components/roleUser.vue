<template>
    <a-modal v-model="modal" width="1000px"  title="角色用户" centered @ok="save" :maskClosable="maskClosable" cancelText="关闭" okText="保存">
        <search-from ref="searchFrom">
            <a-row >
                <a-col :span="24" style="text-align:right;">
                    <a-form-item>
                        <a-button type="primary" icon="search" @click="search()">查询</a-button>
                    </a-form-item>
                </a-col>
            </a-row>
        </search-from>

        <list ref="list"></list>
    </a-modal>
</template>

<script lang="ts">
    import {Component, Emit, Inject, Model, Prop, Provide, Vue, Watch} from 'vue-property-decorator';
    import axios from "@lion/lion-front-core/src/network/axios";
    import { message } from 'ant-design-vue';
    import SearchFrom from "@/components/user/searchFrom.vue";
    import List from "@/components/user/list.vue";
    @Component({
        components: {List, SearchFrom}
    })
    export default class roleUser extends Vue{
        private modal:boolean=false;
        private maskClosable:boolean=false;
        private roleId?:string;
        private save():void{

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
            list.columns= [
                { title: '姓名', dataIndex: 'name', key: 'name' },
                { title: '邮箱', dataIndex: 'email', key: 'email'},
                { title: '年龄', dataIndex: 'age', key: 'age' }
            ];
            const searchFrom = (this.$refs.searchFrom as any);
            list.loading=true;
            const _this = this;
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