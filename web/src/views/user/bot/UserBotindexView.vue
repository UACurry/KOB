<template>
    <ContentFiled>
        我的Bot
    </ContentFiled>
</template>

<script>
import ContentFiled from "../../../components/ContentField.vue"
import $ from 'jquery'
import { useStore } from 'vuex'



export default {
    components: {
        ContentFiled
    },
    setup() {
        const store = useStore();
        // $.ajax({
        //     url: "http://127.0.0.1:1793/user/bot/add/",
        //     type: "post",
        //     // 看后端需要什么东西， controller里面
        //     data: {
        //         title: "我的bot",
        //         description: "描述",
        //         content: "#include <stdio>",
        //     },
        //     // 报文
        //     headers: {
        //         Authorization: "Bearer " + store.state.user.token,
        //     },
        //     success(resp) {
        //         console.log(resp);
        //     },
        //     error(resp) {
        //         console.log(resp);
        //     }
        // })

        // $.ajax({
        //     url: "http://127.0.0.1:1793/user/bot/remove/",
        //     type: "post",
        //     // 看后端需要什么东西， controller里面
        //     data: {
        //         bot_id: 2,
        //     },
        //     // 报文
        //     headers: {
        //         Authorization: "Bearer " + store.state.user.token,
        //     },
        //     success(resp) {
        //         console.log(resp);
        //     },
        //     error(resp) {
        //         console.log(resp);
        //     }
        // })


        // $.ajax({
        //     url: "http://127.0.0.1:1793/user/bot/update/",
        //     type: "post",
        //     // 看后端需要什么东西， controller里面
        //     data: {
        //         bot_id: 4,
        //         title: "我的bot_update",
        //         description: "描述_update",
        //         content: "#include <stdio>_update",
        //     },
        //     // 报文
        //     headers: {
        //         Authorization: "Bearer " + store.state.user.token,
        //     },
        //     success(resp) {
        //         console.log(resp);
        //     },
        //     error(resp) {
        //         console.log(resp);
        //     }
        // })

        $.ajax({
            url: "http://127.0.0.1:1793/user/bot/getlist/",
            type: "get",
            // 看后端需要什么东西， controller里面
            // 报文 但凡需要验证的（登陆验证）的页面 都需要加这个
            headers: {
                Authorization: "Bearer " + store.state.user.token,
            },
            success(resp) {
                console.log(resp);
            },
            error(resp) {
                console.log(resp);
            }
        })
    }
}
</script>

<style scoped></style>