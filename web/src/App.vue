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
import $ from 'jquery'

export default {
  components: {
    NavBar
  },
  setup() {
    $.ajax({
      url: "http://127.0.0.1:1793/user/account/token/",
      type: "post",
      data: {
        username: 'lx',
        password: '123456',
      },
      // resp对象将服务器端的数据发送到客户端浏览器。
      success(resp) {
        console.log(resp);
      },
      error(resp) {
        console.log(resp);
      }
    });

    $.ajax({
      url: "http://127.0.0.1:1793/user/account/info/",
      type: "get",
      // 报文
      headers: {
        Authorization: "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxNTVhNWY0NDM0MjA0MWNlOWE1M2M4NTQ2NmZmZGIxMSIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY4MTA0MTE4OSwiZXhwIjoxNjgyMjUwNzg5fQ.LD8ezj4RY12FdRiD1IRrJFVZE08MBQZHZF9I5HAxjzQ"
      },
      // resp对象将服务器端的数据发送到客户端浏览器。
      success(resp) {
        console.log(resp);
      },
      error(resp) {
        console.log(resp);
      }
    });

    $.ajax({
      url: "http://127.0.0.1:1793/user/account/register/",
      type: "post",
      data: {
        username: "lx_2",
        password: "123456",
        confirmedPassword: "123456",
      },
      // resp对象将服务器端的数据发送到客户端浏览器。
      success(resp) {
        console.log(resp);
      },
      error(resp) {
        console.log(resp);
      }
    });


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
