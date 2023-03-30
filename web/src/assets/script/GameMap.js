import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall";

export class GameMap extends AcGameObject {
    // ctx 画布  parent 画布的父元素 用来动态修改画布的长宽
    constructor(ctx, parent){
        super();

        this.ctx = ctx;
        this.parent = parent;

        // 绝对距离并不重要 因为浏览器会变大变小
        this.L = 0;

        this.rows = 13;
        this.cols = 13;


        this.inner_walls_count = 20;

        // 用来存储所有的墙 开一个数组
        this.walls = [];
    }

    // 判断是否连通
    check_connectivity(g,sx,sy,tx,ty){
        if(sx == tx && sy == ty){
            return true;
        }
        g[sx][sy] = true;
        
        let dx = [-1,0,1,0],dy = [0,1,0,-1];

        for(let i =0;i < 4;i++){
            let x = sx + dx[i], y = sy + dy[i];
            if(!g[x][y] && this.check_connectivity(g, x, y, tx, ty)){
                return true;
            }
        }

        return false;
    }

    create_walls(){
        // 一个bool数组
        const g = [];
        for(let r = 0;r < this.rows;r++){
            g[r] = [];
            for(let c = 0;c < this.cols;c++){
                g[r][c] = false;
            }
        }

        // 给四周加上障碍物
        for(let r = 0;r <this.rows;r++){
            // 左右两边加上墙
            g[r][0] = g[r][this.cols - 1] = true;
        }

        // 上下两行
        for(let c = 0;c < this.cols;c++){
            g[0][c] = g[this.rows-1][c] = true;
        }

        // 创建随机障碍物 抽对称放入
        for(let i = 0;i < this.inner_walls_count / 2;i++){
            for(let j = 0;j < 1000;j++){
                let r = parseInt(Math.random() * this.rows);
                let c = parseInt(Math.random() * this.cols);
                if(g[r][c] || g[c][r]){
                    continue;
                }

                // 不能覆盖左下角右下角
                if(r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2){
                    continue;
                }

                g[r][c] = g[c][r] = true;
                break;
            }
        }

        // 检查是否 连通 先复制一遍
        const copy_g = JSON.parse(JSON.stringify(g));

        if(!this.check_connectivity(copy_g, this.rows - 2, 1, 1, this.cols - 2)){
            return false;
        }

        // 添加墙
        for(let r = 0;r < this.rows;r++){
            for(let c = 0;c < this.cols;c++){
                if(g[r][c]){
                    this.walls.push(new Wall(r,c,this));
                }
            }
        }

        return true;
    }

    start(){
        // 如果不连通 就试1000次
        for(let i = 0;i < 1000;i++){
            if(this.create_walls()){
                break;
            }
        }
        
    }

    update_size(){
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));

        // 求canvas 的长宽
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    update(){
        this.update_size();
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