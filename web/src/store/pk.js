// 用来存储和pk相关的socket变量

// 存socket信息
export default{

    state: {
        status: "matching",  // matching表示匹配界面，playing表示对战界面
        socket: null,
        opponent_username: "",
        opponent_photo: "",
        // 前端接收一下后端的 map
        gamemap: null,

    },
    // 用来修改数据
    mutations: {
        // 创建好之后 用来存储
        updateSocket(state, socket) {
            state.socket = socket;
        },
        updateOpponent(state, opponent) {
            state.opponent_username = opponent.username;
            state.opponent_photo = opponent.photo;
        },
        updateStatus(state, status) {
            state.status = status;
        },
        // 用来更新我们的地图
        updateGamemap(state, gamemap) {
            state.gamemap = gamemap;
        }

    },
    // 修改state的函数 都在actions里面
    actions: {
    },
    modules: {
    }
}