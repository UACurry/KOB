<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
      <!-- <a class="navbar-brand" href="/">King of X</a> -->
      <!-- 防止刷新 -->
      <router-link class="navbar-brand" :to="{ name: 'home' }">King Of X</router-link>

      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <!-- <a class="nav-link" aria-current="page" href="/pk/">对战</a> -->
            <router-link class="nav-link" active-class="active" :to="{ name: 'pk_index' }">对战</router-link>
          </li>
          <li class="nav-item">
            <!-- <a class="nav-link" href="/record/">对局列表</a> -->
            <router-link class="nav-link" active-class="active" :to="{ name: 'record_index' }">对局列表</router-link>
          </li>
          <li class="nav-item">
            <!-- <a class="nav-link" href="/ranklist/">排行榜</a> -->
            <router-link class="nav-link" active-class="active" :to="{ name: 'ranklist_index' }">排行榜</router-link>
          </li>

          <li class="nav-item">
            <!-- <a class="nav-link" href="/ranklist/">排行榜</a> -->
            <router-link class="nav-link" active-class="active" :to="{ name: 'cake' }">陆婉莹24岁生日 粉丝应援</router-link>
          </li>
        </ul>

        <ul class="navbar-nav" v-if="$store.state.user.is_login">
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              {{ $store.state.user.username }}
            </a>
            <ul class="dropdown-menu">
              <li>
                <!-- <a class="dropdown-item" href="/user/bot/">我的Bot</a> -->
                <router-link class="dropdown-item" :to="{ name: 'user_bot_index' }">我的Bot</router-link>
              </li>
              <li>
                <hr class="dropdown-divider">
              </li>
              <li><a class="dropdown-item" href="#" @click="logout">退出</a></li>
            </ul>
          </li>
        </ul>

        <ul class="navbar-nav" v-else>
          <li class="nav-item dropdown">
            <router-link class="nav-link" :to="{ name: 'user_account_login' }" role="button">
              登陆
            </router-link>
          </li>
          <li class="nav-item dropdown">
            <router-link class="nav-link" :to="{ name: 'user_account_register' }" role="button">
              注册
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import { useRoute } from 'vue-router'
import { computed } from 'vue'
import { useStore } from 'vuex';

export default {
  setup() {
    const store = useStore();
    const route = useRoute();
    let route_name = computed(() => route.name)

    const logout = () => {
      store.dispatch("logout");
    }

    return {
      route_name,
      logout
    }
  }
}

</script>

<!-- css   加一个scoped 加上一个随机字符串， 使得这个样式 不会影响到组件以外的-->
<style scoped></style>