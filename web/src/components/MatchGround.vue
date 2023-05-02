<template>
    <!-- 在playground上 形成一个新的蒙版 -->
    <div class="matchground">
        <div class="row">
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.user.photo" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.user.username }}
                </div>
            </div>

            <div class="col-4">
                <div class="user-select-bot">
                    <select v-model="select_bot" class="form-select" aria-label="Default select example">
                        <option value="-1" selected>亲自出马</option>
                        <option v-for="bot in bots" :key="bot.id" :value="bot.id">
                            {{ bot.title }}
                        </option>
                    </select>
                </div>
            </div>


            <div class="col-4">
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
import $ from 'jquery';

export default {
    setup() {
        const store = useStore();
        let bots = ref([]);

        // 选择哪个bot
        let select_bot = ref("-1");


        // 点完之后 变成 取消匹配
        let match_btn_info = ref("开始匹配");

        const click_match_btn = () => {
            if (match_btn_info.value === "开始匹配") {
                match_btn_info.value = "取消";
                // console.log(select_bot.value);
                // 前后端使用 json通信 点击开始匹配后 向后端发送请求 start-matching
                // 之后 后端可以在WebSocketService 中的 onMessage中接收到请求
                store.state.pk.socket.send(JSON.stringify({
                    event: "start-matching",
                    bot_id: select_bot.value,
                }));
            } else {
                match_btn_info.value = "开始匹配";
                // 发送请求 stop-matching
                store.state.pk.socket.send(JSON.stringify({
                    event: "stop-matching",
                }));
            }
        }

        // 从云端动态获取bots
        const refresh_bots = () => {
            $.ajax({
                url: "http://127.0.0.1:1799/user/bot/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    bots.value = resp;
                }
            })
        }

        refresh_bots();  // 从云端动态获取bots

        return {
            match_btn_info,
            click_match_btn,
            bots,
            select_bot,
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

div.user-select-bot {
    padding-top: 20vh;
}

div.user-select-bot>select {
    width: 60%;
    margin: 0 auto;
}
</style>
