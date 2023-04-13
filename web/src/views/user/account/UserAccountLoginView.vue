<template>
    <ContentFiled>
        <div class="row justify-content-md-center">
            <div class="col-3 ">
                <!-- 提交就触发login -->
                <form @submit.prevent="login">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="输入密码">
                    </div>
                    <div class="error-message">{{ error_message }}</div>
                    <button type="submit" class="btn btn-primary">提交</button>
                </form>
            </div>
        </div>
    </ContentFiled>
</template>

<script>
import ContentFiled from "../../../components/ContentField.vue"
import { useStore } from 'vuex'
import { ref } from 'vue'
import router from '../../../router/index'

export default {
    components: {
        ContentFiled
    },
    setup() {
        // 取出全局变量
        const store = useStore();
        // 一开始都为空
        let username = ref('');
        let password = ref('');
        let error_message = ref('');

        // 用于登陆持久化 判断是否有local store
        // const jwt_token = localStorage.getItem("jwt_token");
        // if (jwt_token) {
        //     // 调用user.js 中的 mutation 需要commit
        //     store.commit("updateToken", jwt_token);
        //     // 验证jwt_token 是否过期  调用user.js 里面的actions
        //     store.dispatch("getinfo", {
        //         // 没有过期 就跳转 home
        //         success() {
        //             router.push({ name: "home" });
        //         },
        //         error() {
        //         }
        //     })
        // }

        // 触发函数
        const login = () => {
            // 清空
            error_message.value = "";
            // 调用user.js 中的actions
            store.dispatch("login", {
                // 看后端需要什么东西， controller里面
                username: username.value,
                password: password.value,
                success() {
                    store.dispatch("getinfo", {
                        success() {
                            router.push({ name: 'home' });
                            console.log(store.state.user)
                        }
                    })
                },
                error() {
                    error_message.value = "用户名或密码错误";
                },
            })
        }

        return {
            username,
            password,
            error_message,
            login,
        }
    }
}
</script>

<style scoped>
button {
    width: 100%;
}

div.error-message {
    color: red;
}
</style>