<template>
    <!-- 在playground上 形成一个新的蒙版 -->
    <div class="matchground">
        <div class="row">
            <div class="col-6">
                <div class="user-photo">
                    <img :src="$store.state.user.photo" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.user.username }}
                </div>
            </div>
            <div class="col-6">
                <div class="user-photo">
                    <img :src="$store.state.pk.opponent_photo" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.pk.opponent_username }}
                </div>
            </div>
            <div class="col-12" style="text-align: center; padding-top: 15vh;">
                <!-- @click="click_match_btn" 绑定事件 -->
                <button @click="click_match_btn" type="button" class="btn btn-warning btn-lg">{{ match_btn_info }}</button>
            </div>
        </div>
    </div>
</template>


<script>
import { ref } from 'vue'
import { useStore } from 'vuex';

export default {
    setup() {
        const store = useStore();

        // 点完之后 变成 取消匹配
        let match_btn_info = ref("开始匹配");

        const click_match_btn = () => {
            if (match_btn_info.value === "开始匹配") {
                match_btn_info.value = "取消";
                // 前后端使用 json通信 点击开始匹配后 向后端发送请求 start-matching
                // 之后 后端可以在WebSocketService 中的 onMessage中接收到请求
                store.state.pk.socket.send(JSON.stringify({
                    event: "start-matching",
                }));
            } else {
                match_btn_info.value = "开始匹配";
                // 发送请求 stop-matching
                store.state.pk.socket.send(JSON.stringify({
                    event: "stop-matching",
                }));
            }
        }

        return {
            match_btn_info,
            click_match_btn,
        }
    }

}
</script>

<style scoped>
div.matchground {
    width: 60vw;
    height: 70vh;
    /* 上方40  左右auto就是居中*/
    margin: 40px auto;
    background-color: rgba(50, 50, 50, 0.5);
}

div.user-photo {
    text-align: center;
    padding-top: 10vh;
}

/* 变成圆形 */
div.user-photo>img {
    border-radius: 50%;
    width: 20vh;
}

div.user-username {
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    color: white;
    padding-top: 2vh;
}
</style>
