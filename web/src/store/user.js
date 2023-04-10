import $ from 'jquery'

// 存用户的全局信息
export default{
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
                url: "http://127.0.0.1:1793/user/account/token/",
                type: "post",
                data: {
                  username: data.username,
                  password: data.password,
                },
                // resp对象将服务器端的数据发送到客户端浏览器。
                success(resp) {
                    // resp 里面返回的东西 都是后端定义好的 后端最后的map的返回 包括 resp.token
                    if(resp.error_message === "success"){
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
                url: "http://127.0.0.1:1793/user/account/info/",
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
            context.commit("logout");
        }
    },
    modules: {
    }
}