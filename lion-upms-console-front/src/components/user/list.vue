<template>
    <a-table :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }" rowKey="id" :columns="columns" :dataSource="data" :loading="loading" :pagination="paginationProps">
        <span slot="action" slot-scope="text, record">
            <slot></slot>
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

        private columns :Array<any> = [
            { title: '姓名', dataIndex: 'name', key: 'name' },
            { title: '邮箱', dataIndex: 'email', key: 'email'},
            { title: '年龄', dataIndex: 'age', key: 'age' },
            { title: '操作', key: 'action', scopedSlots: { customRender: 'action' },width: 180,}
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
            (this.$parent.$parent as any).setPageInfo(pageNumber,pageSize);
            (this.$parent.$parent as any).search();
        }
    }
</script>

<style scoped>

</style>