<template>
    <div>
        <a-card class="card" style="border-bottom-width: 5px;" >
            <a-form ref="from" >
                <a-row >
                    <a-col :span="24" style="text-align:right;">
                        <a-form-item>
                          <a-button type="primary" v-if="getAuthority('SYSTEM_SETTINGS_RESOURCES_LIST')" @click="add(0)">
                            <template #icon><PlusOutlined /></template>
                            新增
                          </a-button>
                        </a-form-item>
                    </a-col>
                </a-row>
            </a-form>
        </a-card>

        <a-card class="card" :bordered="false">
            <a-table bordered rowKey="id" :columns="columns" :dataSource="listData" :loading="loading" :pagination="false">
              <template #bodyCell="{ column ,record}">
                <template v-if="column.key === 'operation'">
                  <a-space :size="size">
                    <a-button size="small" v-if="getAuthority('SYSTEM_SETTINGS_DEPARTMENT_UPDATE')" @click="getDetails(record.id)">
                      <template #icon><EditOutlined /></template>
                      修改
                    </a-button>
                    <a-button size="small" v-if="getAuthority('SYSTEM_SETTINGS_DEPARTMENT_ADD')" @click="add(record.id)">
                      <template #icon><PlusOutlined /></template>
                      新增子部门
                    </a-button>
                    <a-button size="small" v-if="getAuthority('SYSTEM_SETTINGS_DEPARTMENT_USER')" @click="departmentUser(record.id)">
                      <template #icon><UserOutlined /></template>
                      用户
                    </a-button>
                    <a-button size="small" type="danger" v-if="getAuthority('SYSTEM_SETTINGS_DEPARTMENT_DELETE')" @click="del(record.id)">
                      <template #icon><DeleteOutlined /></template>
                      删除
                    </a-button>
                  </a-space>
                </template>
              </template>
            </a-table>
        </a-card>

        <a-modal destroyOnClose v-model:visible="modal" width="800px" title="添加/修改部门" :maskClosable="maskClosable"  centered @ok="addOrUpdate" cancelText="关闭" okText="保存">
            <a-form ref="addOrUpdateForm" :model="addOrUpdateModel" :rules="rules" >
                <a-row>
                    <a-col :span="24">
                        <a-form-item label="名称" name="name" ref="name" >
                            <a-input  v-model:value="addOrUpdateModel.name" />
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col :span="24">
                        <a-form-item label="备注" name="remark" ref="remark">
                            <a-textarea  placeholder="请输入备注" :rows="4" v-model:value="addOrUpdateModel.remark"/>
                        </a-form-item>
                    </a-col>
                </a-row>
            </a-form>
        </a-modal>

        <department-user ref="departmentUser" ></department-user>
    </div>
</template>

<script lang="ts">
    import {Options,  Vue} from 'vue-property-decorator';
    import { SearchOutlined,PlusOutlined,DeleteOutlined,EditOutlined,SecurityScanOutlined,UserOutlined } from '@ant-design/icons-vue';
    import axios from "@lion/lion-frontend-core/src/network/axios";
    import { message,Modal } from 'ant-design-vue';
    import qs from "qs";
    import departmentUser from "@/department/components/departmentUser.vue";
    import authority from "@lion/lion-frontend-core/src/security/authority";
    @Options({
        components: {departmentUser,SearchOutlined,PlusOutlined,DeleteOutlined,EditOutlined,SecurityScanOutlined,UserOutlined }
    })
    export default class List extends Vue{
        //列表数据
        private listData:Array<any>=[];
        //列表是否加载中（转圈圈图标）
        private loading:boolean=false;
        //是否显示modal（新增窗口）
        private modal:boolean=false;
        //是否点击阴影层关闭modal（新增窗口）
        private maskClosable:boolean=false;
        //新增/修改数据模型
        private addOrUpdateModel:any={
            parentId:0,
        };
        //校验规则
        private rules:any={
            name:[{required:true,validator:(rule :any, value:string) =>{return this.checkNameIsExist(rule,value,this)},trigger:'blur'}],
        };
        //检查名称是否存在
        private async checkNameIsExist(rule :any, value:string, _this:any){
          let promise:any = null;
          if (!value || value.trim() === ''){
            return Promise.reject("请输入名称");
          }else if (value && value.trim() !== ''){
            await axios.get("/lion-upms-console-restful/department/console/check/name/exist",{params:{"name":_this.addOrUpdateModel.name,"id":_this.addOrUpdateModel.id,"parentId":_this.addOrUpdateModel.parentId}})
            .then((data)=> {
              if (Object(data).status !== 200){
                promise = Promise.reject("异常错误！请检查")
              }
              if (data.data) {
                promise = Promise.reject("名称在同节点已存在")
              }else {
                promise = Promise.resolve();
              }
            })
            .catch(fail => {
            })
            .finally(()=>{
            });
          }
          return promise;
        }
        //表格列定义
        private columns :Array<any> = [
            { title: '名称', dataIndex: 'name', key: 'name' },
            { title: '备注', dataIndex: 'remark', key: 'remark',width: '300px'},
            { title: '操作', key: 'operation',width: 380,}
        ];

        /**
         * 页面挂载后触发的事件
         */
        public mounted():void {
            this.search();
        }

         /**
          * 查询
          */
        private search():void{
            this.loading=true;
            axios.get("/lion-upms-console-restful/department/console/list/tree",{params:{}})
            .then((data)=>{
                this.listData=data.data;
            })
            .catch(fail => {
            })
            .finally(()=>{
                this.loading=false;
            });
        }

        /**
         * 显示新增窗口
         * @param parentId
         */
        private add(parentId:string):void{
            this.addOrUpdateModel={};
            this.addOrUpdateModel.parentId=parentId;
            this.modal=true;
        }

        /**
         * 提交新增/修改数据
         */
        private addOrUpdate():void{
            if (this.addOrUpdateModel.id){
                axios.put("/lion-upms-console-restful/department/console/update",this.addOrUpdateModel)
                .then((data)=>{
                    if (Object(data).status === 200){
                        message.success(Object(data).message);
                        this.success();
                    }
                })
                .catch(fail => {
                })
                .finally(()=>{
                    this.loading=false;
                });
            }else{
                axios.post("/lion-upms-console-restful/department/console/add",this.addOrUpdateModel)
                .then((data)=>{
                    if (Object(data).status === 200){
                        message.success(Object(data).message);
                        this.success();
                    }
                })
                .catch(fail => {
                })
                .finally(()=>{
                    this.loading=false;
                });
            }

        }

        /**
         * 删除警示
         * @param id
         */
        private del(id:any):void{
            const _this =this;
            if (!id){
                message.error("请选择要删除的数据");
                return;
            }
            Modal.confirm({
                title: '是否要删除该数据?(错误的操作会带来灾难性的后果)',
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
            axios.delete("/lion-upms-console-restful/department/console/delete",{params:{id:id},
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

        /**
         * 新增/修改/删除成功事件
         */
        private success():void{
            this.addOrUpdateModel ={parentId:0};
            (this.$refs.addOrUpdateForm as any).resetFields();
            this.modal = false;
            this.search();
        }

        /**
         * 修改获取详情
         * @param id
         */
        private getDetails(id:string):void{
            axios.get("/lion-upms-console-restful/department/console/details",{params:{id:id}})
            .then((data)=>{
                this.addOrUpdateModel=data.data;
                this.modal = true;
            })
            .catch(fail => {
            })
            .finally(()=>{
            });
        }

        /**
         * 显示部门关联用户窗口
         * @param id
         */
        private departmentUser(id:string):void{
            const child = (this.$refs.departmentUser as any);
            child.modal=true;
            child.departmentId=id;
            child.searchModel.pageNumber=1;
            setTimeout(function () {
                child.search();
            },500);
        }

        /**
         * 判断(获取)是否有权限
         */
        private getAuthority(authorityCode:string):any{
            return authority(authorityCode);
        }
    }
</script>

<style scoped>
    .ant-form-item >>> .ant-form-item-label{
        width: 50px;
    }
    .ant-form-item{
        width: 100%;
    }
    .ant-row >>> .ant-form-item-control{
        width: calc(100% - 50px);
    }
    .ant-card >>> .ant-card-body{
      padding-bottom: 0px;
    }
</style>