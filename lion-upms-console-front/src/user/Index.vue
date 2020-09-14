<template>
    <div>
        <keep-alive>
            <router-view v-if="$route.meta.keepAlive"></router-view>
        </keep-alive>
        <router-view v-if="!$route.meta.keepAlive"></router-view>
    </div>
</template>

<script lang="ts">
    import {Component, Emit, Inject, Model, Prop, Provide, Vue, Watch} from 'vue-property-decorator';
    import moment from 'moment';
    import 'moment/locale/zh-cn';
    @Component({})
    export default class Index extends Vue{
        private moment:any = moment;
        private created() {
        }
    }
</script>

<style lang="less" scoped>
</style>


