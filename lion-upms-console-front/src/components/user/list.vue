<template>
    <a-table bordered :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange ,getCheckboxProps:getCheckboxProps}"  :columns="columns" :dataSource="data" :loading="loading" :pagination="paginationProps">
        <span slot="department" slot-scope="text, record">
            <template>
                <samp v-if="record.department">{{departmentName(record.department)}}</samp>
            </template>
        </span>
        <span slot="role" slot-scope="text, record">
            <samp v-for="(role,index) in record.role">{{role.name}}<samp v-if="index<record.role.length-1">/</samp></samp>
        </span>
        <span slot="action" slot-scope="text, record">
            <a-button style="margin-left: 5px;" icon="edit" size="small" @click="edit(record.user.id)">修改</a-button>
            <a-button style="margin-left: 5px;" type="danger" icon="delete" size="small" @click='del(record.user.id)'>删除</a-button>
        </span>
    </a-table>
</template>

<script lang="ts">
    import {Component, Emit, Inject, Model, Prop, Provide, Vue, Watch} from 'vue-property-decorator';
    @Component({})
    export default class list extends Vue{
        private rowKey:any="user.id";
        //复选框选中的值
        private selectedRowKeys:Array<number> = [];
        //列表数据
        private data:Array<any> = [];
        //是否加载中（转圈圈图标）
        private loading:boolean=false;
        //不能选择的复选框
        private notCheckUserId:Array<string>=[];
        //表格列
        private columns :Array<any> = [
            { title: '姓名', dataIndex: 'user.name', key: 'name' },
            { title: '邮箱', dataIndex: 'user.email', key: 'email'},
            { title: '年龄', dataIndex: 'user.age', key: 'age' },
            { title: '生日', dataIndex: 'user.birthday', key: 'birthday' },
            { title: '部门', key: 'department', scopedSlots: { customRender: 'department' }},
            { title: '角色', key: 'role', scopedSlots: { customRender: 'role' }},
            // { title: '操作', key: 'action', scopedSlots: { customRender: 'action' },width: 180,}
        ];
        //分页参数设置
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

        /**
         * 复选框改变事件
         * @param selectedRowKeys
         */
        private onSelectChange(selectedRowKeys:Array<number>):void{
            this.selectedRowKeys = selectedRowKeys;
        }

        /**
         * 分页触发上下页/第几页事件
         * @param pageNumber
         * @param pageSize
         */
        private paginationSearch(pageNumber:number, pageSize: number):void{
            this.setPageInfo(pageNumber,pageSize);
            this.search();
        }

        /**
         * 设置复选框是否可选
         * @param record
         */
        public getCheckboxProps(record:any):any{
            return {
                props: {
                    disabled: this.notCheckUserId.includes(record.user.id),
                },
            };
        }

        /**
         * 获取上级部门名称
         */
        private departmentName(department:any):string{
            let departmentName:string ="";
            let parentDepartmentName:Array<string>=[];
            if (department){
                if (department.parentDepartment){
                    parentDepartmentName = this.getParentDepartmentName(department.parentDepartment,parentDepartmentName);
                }
                for(let j:number = 0,len=parentDepartmentName.length; j < len; j++) {
                    departmentName=departmentName+(departmentName!==""?"/":"")+parentDepartmentName[parentDepartmentName.length-(j+1)];
                }
                departmentName=departmentName+(departmentName!==""?"/":"")+department.name;
            }
            return departmentName;
        }

        /**
         * 获取上级部门名称
         */
        private getParentDepartmentName(department:any,parentDepartmentName:Array<string>):Array<string>{
            if (department) {
                parentDepartmentName[parentDepartmentName.length] = department.name;
                if (department.parentDepartment){
                    this.getParentDepartmentName(department.parentDepartment,parentDepartmentName);
                }
            }
            return parentDepartmentName;
        }

        /**
         * 事件传递调用父组件编辑方法
         * @param is
         */
        @Emit()
        private edit(is:string):void{}

        /**
         * 事件传递调用父组件删除方法
         * @param is
         */
        @Emit()
        private del(is:string):void{}

        /**
         * 事件传递调用父组件查询方法
         */
        @Emit()
        private search():void{}

        /**
         * 事件传递调用父组件设置分页信息
         * @param pageNumber
         * @param pageSize
         */
        @Emit("set-page-info")
        private setPageInfo(pageNumber:number, pageSize: number):void{}
    }
</script>

<style scoped>

</style>