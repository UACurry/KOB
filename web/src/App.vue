<!-- html -->
<template>
  <NavBar></NavBar>
  <router-view />
</template>

<!-- js -->
<script>
// import $ from 'jquery';
// import { ref } from 'vue';

// 导入组件
import NavBar from './components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"

export default {
  components: {
    NavBar
  }
  //   // 对象的名称
  //   name: "App",
  //   // 整个函数的入口
  //   setup: () => {
  //     let bot_name = ref("");
  //     let bot_rating = ref("");

  //     // 访问后端链接 用ajax来写
  //     $.ajax({
  //       url: "http://127.0.0.1:1793/pk/getbotinfo/",
  //       type: "get",
  //       success: resp => {
  //         // 在f12 控制台输出
  //         console.log(resp);
  //         bot_name.value = resp.name;
  //         bot_rating.value = resp.rating;
  //       }
  //     });

  //     return {
  //       bot_name,
  //       bot_rating
  //     }
  //   }
}
</script>

<!-- css -->
<style>
body {
  background-image: url("./assets/img/background.png");
  /* 百分百填充 */
  background-size: cover;
}
</style>
