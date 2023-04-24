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

        // 从后端拿来初始信息
        a_id: 0,
        a_sx: 0,
        a_sy: 0,
        b_id: 0,
        b_sx: 0,
        b_sy: 0,
        gameObject: null,
        // 记录是谁输了
        loser: "none",  // none、all、A、B
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
        // 用来更新我们的地图 和 后端对应 
        // 更新之后 函数名 更改为 updateGame 传入全方位信息
        updateGame(state, game) {
            // 和 后端对应 game.map  在后端中 respGame.put 
            state.gamemap = game.map;
            state.a_id = game.a_id;
            state.a_sx = game.a_sx;
            state.a_sy = game.a_sy;
            state.b_id = game.b_id;
            state.b_sx = game.b_sx;
            state.b_sy = game.b_sy;
        },
        updateGameObject(state, gameObject) {
            state.gameObject = gameObject;
        },
        updateLoser(state, loser) {
            state.loser = loser;
        }


    },
    // 修改state的函数 都在actions里面
    actions: {
    },
    modules: {
    }
}