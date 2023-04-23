import { AcGameObject } from "./AcGameObject";
import { Snake } from "./Snake";
import { Wall } from "./Wall";

export class GameMap extends AcGameObject {
    // ctx 画布  parent 画布的父元素 用来动态修改画布的长宽
    constructor(ctx, parent, store){
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.store = store;

        // 绝对距离并不重要 因为浏览器会变大变小
        this.L = 0;

        // 变成长方形，从而防止 头节点同时走到某个结点
        this.rows = 13;
        this.cols = 14;


        this.inner_walls_count = 20;

        // 用来存储所有的墙 开一个数组
        this.walls = [];

        // 创建蛇
        this.snakes = [
            new Snake({id: 0,color: "#4876EC",r: this.rows - 2,c: 1},this),
            new Snake({id: 0,color: "#F94848",r: 1,c: this.cols - 2},this),
        ]

 
    }

    // 已在后端实现
    // // 判断是否连通
    // check_connectivity(g,sx,sy,tx,ty){
    //     if(sx == tx && sy == ty){
    //         return true;
    //     }
    //     g[sx][sy] = true;
        
    //     let dx = [-1,0,1,0],dy = [0,1,0,-1];

    //     for(let i =0;i < 4;i++){
    //         let x = sx + dx[i], y = sy + dy[i];
    //         if(!g[x][y] && this.check_connectivity(g, x, y, tx, ty)){
    //             return true;
    //         }
    //     }

    //     return false;
    // }

    create_walls(){
        const g = this.store.state.pk.gamemap;
        // 一个bool数组
        // const g = [];
        // for(let r = 0;r < this.rows;r++){
        //     g[r] = [];
        //     for(let c = 0;c < this.cols;c++){
        //         g[r][c] = false;
        //     }
        // }

        // // 给四周加上障碍物
        // for(let r = 0;r <this.rows;r++){
        //     // 左右两边加上墙
        //     g[r][0] = g[r][this.cols - 1] = true;
        // }

        // // 上下两行
        // for(let c = 0;c < this.cols;c++){
        //     g[0][c] = g[this.rows-1][c] = true;
        // }

        // // 创建随机障碍物 中心对称放入
        // for(let i = 0;i < this.inner_walls_count / 2;i++){
        //     for(let j = 0;j < 1000;j++){
        //         let r = parseInt(Math.random() * this.rows);
        //         let c = parseInt(Math.random() * this.cols);
        //         // 中心对称
        //         if(g[r][c] || g[this.rows - 1 - r][this.cols - 1 -c]){
        //             continue;
        //         }

        //         // 不能覆盖左下角右下角
        //         if(r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2){
        //             continue;
        //         }
                
        //         // 中心对称
        //         g[r][c] = g[this.rows - 1 - r][this.cols - 1 -c] = true;
        //         break;
        //     }
        // }

        // // 检查是否 连通 先复制一遍
        // const copy_g = JSON.parse(JSON.stringify(g));

        // if(!this.check_connectivity(copy_g, this.rows - 2, 1, 1, this.cols - 2)){
        //     return false;
        // }

        // 添加墙
        for(let r = 0;r < this.rows;r++){
            for(let c = 0;c < this.cols;c++){
                if(g[r][c]){
                    this.walls.push(new Wall(r,c,this));
                }
            }
        }

        // return true;
    }

    // 绑定事件 获取输入
    add_listening_events(){
        // 聚焦于canvas
        this.ctx.canvas.focus();

        // 绑定keydown事件
        this.ctx.canvas.addEventListener("keydown", e =>{
            // 移动的方向
            let d = -1;
            if (e.key === 'w') d = 0;
            else if (e.key === 'd') d = 1;
            else if (e.key === 's') d = 2;
            else if (e.key === 'a') d = 3;

            // 如果操作
            if (d >= 0) {
                this.store.state.pk.socket.send(JSON.stringify({
                    event: "move",
                    direction: d,
                }));
            }

        })
    }

    start(){
        // 如果不连通 就试1000次
        this.create_walls();
        
        // 自动聚焦于 canvas
        this.add_listening_events();
    }

    update_size(){
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));

        // 求canvas 的长宽
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    // 用作裁判,判断两条蛇是否都有新的指令 
    check_ready(){
        for(const snake of this.snakes){
            if(snake.status !== "idle"){
                return false;
            }

            if(snake.direction === -1){
                return false;
            }
        }
        return true;
    } 

    // 检测目标位置是否合法
    check_valid(cell){
        for(const wall of this.walls){
            if(wall.r === cell.r && wall.c === cell.c){
                return false;
            }
        }

        for(const snake of this.snakes){
            let k = snake.cells.length;
            // 蛇尾会前进时候，无需判断最后一个格子
            if(!snake.check_tail_increasing()){
                k--;
            }

            for(let i = 0;i < k;i++){
                if(snake.cells[i].r === cell.r && snake.cells[i].c === cell.c){
                    return false;
                }
            }
        }
        return true;
    }

    // 让两条蛇进入下一回合
    next_step(){
        for(const snake of this.snakes){
            snake.next_step();
        }
    }

    update(){
        this.update_size();

        // 两条蛇都准备好
        if(this.check_ready()){
            this.next_step();
        }

        this.render();
    }

    render(){
        const color_even = "#AAD751",color_odd = "#A2D149";
        for(let r = 0;r < this.rows;r++){
            for(let c = 0;c < this.cols;c++){
                if((r + c) % 2 == 0){
                    this.ctx.fillStyle = color_even;
                }
                else{
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
            }
        }

    }

}