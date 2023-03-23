<!-- html -->
<template>
  <div>
    <div>
      Bot昵称:{{ bot_name }}
    </div>
    <div>
      Bot战力:{{ bot_rating }}
    </div>
  </div>
  <router-view />
</template>

<!-- js -->
<script>
import $ from 'jquery';
import { ref } from 'vue';

export default {
  // 对象的名称
  name: "App",
  // 整个函数的入口
  setup: () => {
    let bot_name = ref("");
    let bot_rating = ref("");

    // 访问后端链接 用ajax来写
    $.ajax({
      url: "http://127.0.0.1:1793/pk/getbotinfo/",
      type: "get",
      success: resp => {
        // 在f12 控制台输出
        console.log(resp);
        bot_name.value = resp.name;
        bot_rating.value = resp.rating;
      }
    });

    return {
      bot_name,
      bot_rating
    }
  }
}
</script>

<!-- css -->
<style>
body {
  background-image: url("./assets/background.png");
  /* 百分百填充 */
  background-size: cover;
}
</style>
