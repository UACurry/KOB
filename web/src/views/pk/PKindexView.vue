<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'" />
    <MatchGround v-if="$store.state.pk.status === 'matching'" />
</template>

<script>
import PlayGround from "../../components/PlayGround.vue"
import MatchGround from "../../components/MatchGround.vue"
import { onMounted, onUnmounted } from "vue";
import { useStore } from 'vuex'

export default {
    components: {
        PlayGround,
        MatchGround,
    },
    setup() {
        const store = useStore();
        const socketUrl = `ws://127.0.0.1:1793/websocket/${store.state.user.token}/`;

        let socket = null;

        // 当组件被挂载时候 调用
        onMounted(() => {
            // 调用pk.js 中的函数
            store.commit("updateOpponent", {
                username: "我的对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            })
            socket = new WebSocket(socketUrl);

            socket.onopen = () => {
                console.log("connected!");
                // 将socket存到全局变量
                store.commit("updateSocket", socket);
            }

            socket.onmessage = msg => {
                // 从后端接受过来的匹配信息
                const data = JSON.parse(msg.data);
                if (data.event === "is-matched") {  // 匹配成功
                    // 匹配成功 更新对手信息
                    store.commit("updateOpponent", {
                        username: data.opponent_username,
                        photo: data.opponent_photo,
                    });
                    setTimeout(() => {
                        store.commit("updateStatus", "playing");
                    }, 2000);
                    // 更新一下地图
                    store.commit("updateGamemap", data.gamemap);
                }
                console.log(data);
            }

            //关闭
            socket.onclose = () => {
                console.log("disconnected!");
            }
        });

        // 当前链接被卸载 （页面被跳转或者关闭）
        onUnmounted(() => {
            socket.close();
            // pk页面被卸载时候 需要将状态改为match  重新进行匹配
            store.commit("updateStatus", "matching");
        })
    }

}
</script>

<style scoped></style>