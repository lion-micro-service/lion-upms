<template>
    <a-modal destroyOnClose style="max-height: 500px;" v-model:value="modal" width="800px"  title="角色部门" centered @ok="save" :maskClosable="maskClosable" cancelText="关闭" okText="保存">

    </a-modal>
</template>

<script lang="ts">
  import {Options, Vue} from 'vue-property-decorator';
  @Options({components:{}})
  export default class roleDepartment extends Vue{
      private modal:boolean=false;
      private maskClosable:boolean=false;
      private roleId?:string;

      private save():void{

      }
  }
</script>

<style scoped>

</style>