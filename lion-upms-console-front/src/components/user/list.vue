<template>
    <a-table :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange ,getCheckboxProps:getCheckboxProps}" rowKey="id" :columns="columns" :dataSource="data" :loading="loading" :pagination="paginationProps">
        <span slot="action" slot-scope="text, record">
            <a-button icon="edit" size="small" @click="edit(record.id)">修改</a-button>
            <a-button type="danger" icon="delete" size="small" @click='del(record.id)'>删除</a-button>
        </span>
    </a-table>
</template>

<script lang="ts">
    import {Component, Emit, Inject, Model, Prop, Provide, Vue, Watch} from 'vue-property-decorator';
    @Component({})
    export default class list extends Vue{
        private selectedRowKeys:Array<number> = [];
        private data:Array<any> = [];
        private loading:boolean=false;
        private notCheckUserId:Array<string>=[];
        private columns :Array<any> = [
            { title: '姓名', dataIndex: 'name', key: 'name' },
            { title: '邮箱', dataIndex: 'email', key: 'email'},
            { title: '年龄', dataIndex: 'age', key: 'age' },
            // { title: '操作', key: 'action', scopedSlots: { customRender: 'action' },width: 180,}
        ];
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

        private onSelectChange(selectedRowKeys:Array<number>):void{
            this.selectedRowKeys = selectedRowKeys;
        }

        private paginationSearch(pageNumber:number, pageSize: number):void{
            this.setPageInfo(pageNumber,pageSize);
            this.search();
        }

        public getCheckboxProps(record:any):any{
            return {
                props: {
                    disabled: this.notCheckUserId.includes(record.id),
                },
            };
        }

        @Emit()
        private edit(is:string):void{}

        @Emit()
        private del(is:string):void{}

        @Emit()
        private search():void{}

        @Emit("set-page-info")
        private setPageInfo(pageNumber:number, pageSize: number):void{}
    }
</script>

<style scoped>

</style>