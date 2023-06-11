import $ from 'jquery'

// 存用户的全局信息
export default{
    // 需要修改state值的时候 才会放到user.js文件中
    state: {
        id: "",
        username: "",
        photo: "",
        token: "", // jwt token
        is_login: false,
    },
    // 用来修改数据
    mutations: {
        updateUser(state, user){
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.is_login = user.is_login;
        },
        updateToken(state,token){
            state.token = token
        },
        // token 是存在用户的  如果要登出 直接丢了token即可
        logout(state){
            state.id = "";
            state.username = "";
            state.photo = "";
            state.token = "";
            state.is_login = false;
        }
    },
    // 修改state的函数 都在actions里面
    actions: {
        login(context, data){
            $.ajax({
                url: "https://lwy.gowarriors.top/api/user/account/token/",
                type: "post",
                // 看后端需要什么东西， controller里面 map对应的
                data: {
                  username: data.username,
                  password: data.password,
                },
                // resp对象将服务器端的数据发送到客户端浏览器。
                success(resp) {
                    // resp 里面返回的东西 都是后端定义好的 后端最后的map的返回 包括 resp.token
                    if(resp.error_message === "success"){
                        // 实现登陆持久化
                        localStorage.setItem("jwt_token",resp.token);

                        context.commit("updateToken", resp.token);
                        data.success(resp);
                    }
                    else{
                        data.error(resp);
                    }
                },
                error(resp) {
                  data.error(resp);
                }
              }); 
        },
        getinfo(context,data){
            $.ajax({
                url: "https://lwy.gowarriors.top/api/user/account/info/",
                type: "get",
              // 报文
              headers: {
                Authorization: "Bearer " + context.state.token,
              },
              // resp对象将服务器端的数据发送到客户端浏览器。
              success(resp) {
                if(resp.error_message === "success"){
                    context.commit("updateUser",{
                        ...resp,
                        is_login: true,
                    });
                    data.success(resp);
                }else{
                    data.error(resp);
                }
              },
              error(resp) {
                data.error(resp);
              }
            })
        },
        logout(context){
            // 删除登陆持久化
            localStorage.removeItem("jwt_token");
            context.commit("logout");
        }
    },
    modules: {
    }
}