import { AcGameObject } from "./AcGameObject";
import { Cell } from "./Cell";

export class Snake extends AcGameObject{
    // 传过来蛇的信息和地图
    constructor(info, gamemap){
        super();

        this.id = info.id;
        this.color = info.color;
        // 可以调用地图的一些参数
        this.gamemap = gamemap;

        // 存放蛇的身体 cell[0] 为蛇头
        this.cells = [new Cell(info.r, info.c)];

        // 下一步的操作
        this.next_cell = null;

        // 每秒 走五个格子
        this.speed = 5;

        // 记录下一步指令
        // -1 表示没有指令, 0 1 2 3 表示上右下左
        this.direction = -1;

        // 表示当前蛇的状态  idle 不动  move 正在移动 die 表示死亡
        this.status = "idle"

        // 往四个方向走的偏移量
        this.dr = [-1,0,1,0];
        this.dc = [0,1,0,-1];

        // 表示回合数
        this.step = 0;

        // 误差允许范围
        this.eps = 1e-2;

        this.eye_direction = 0;
        // 左下角初试朝上，右上角朝下
        if(this.id === 1){
            this.eye_direction = 2;
        }

        // 不同方向眼睛的偏移量 打表 两个眼睛 x方向的偏移量
        this.eye_dx =  [
            [-1,1],
            [1,1],
            [1,-1],
            [-1,-1],
        ];

        this.eye_dy = [
            [-1,-1],
            [-1,1],
            [1,1],
            [-1,1],
        ]
    }

    start(){

    }

    // 设置一个统一的接口 获得方向 从而可以获得后端的输入
    set_direction(d){
        this.direction = d;
    }

    // 检测当前回合，蛇的长度是否增加
    check_tail_increasing(){
        if(this.step <= 10){
            return true;
        }
        if(this.step % 3 === 1){
            return true;
        }
        return false;
    }

    // 将蛇的状态变为走下一步
    next_step(){
        const d = this.direction;
        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
        this.direction = -1; // 清空方向
        this.eye_direction = d;
        this.status = "move";
        this.step++;

        const k = this.cells.length;
        // 每个元素 向后移动一位 要用深层拷贝 下标k 就是新的位置
        for(let i = k;i > 0;i--){
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i-1]));
        }

        // 下一步操作非法 移动到后端判断
        // if(!this.gamemap.check_valid(this.next_cell)){
        //     this.status = "die";
        // }
    }

    // 移动  判断两条蛇是否都准备好下一回合
    // 如何移动： 从头部抛出一个新的球 虚拟球，中间除了尾部都不动
    update_move(){
        // this.cells[0].x += this.speed * this.timedelta / 1000;

        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx * dx + dy * dy);
        
        // 认为重合 走到目标点了
        if(distance < this.eps){
            // 添加一个新的蛇头
            this.cells[0] = this.next_cell;
            this.next_cell = null; 
            // 走完停下来
            this.status = "idle";

            // 如果蛇没有变长 就砍掉蛇尾
            if(!this.check_tail_increasing()){
                this.cells.pop();
            }
        }
        // 不重合就移动
        else{
             // 每一帧移动距离
            //  移动蛇头
            const move_distance = this.speed * this.timedelta / 1000;
            this.cells[0].x += move_distance * dx / distance;
            this.cells[0].y += move_distance * dy / distance;

            // 移动蛇尾
            if(!this.check_tail_increasing()){
                const k = this.cells.length;
                const tail = this.cells[k-1];
                const tail_target = this.cells[k-2];
                // 两个点之间横坐标的差值
                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;

                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;
            }
        }
    }

    // 每一帧画一遍
    update(){
        if(this.status === 'move'){
            this.update_move();
        }
        this.render();
    }

    render(){
        // 每个单元格的长度
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        // 画圆 也就是画蛇
        ctx.fillStyle = this.color;

        if(this.status === "die"){
            ctx.fillStyle = "white";
        }

        for(const cell of this.cells){
            ctx.beginPath();
            // 把蛇画瘦一点 半径定义为 百分之八十的 L
            ctx.arc(cell.x * L,cell.y * L, L / 2 * 0.8, 0, Math.PI * 2);
            ctx.fill();
        }

        // 美化蛇
        for(let i = 1; i < this.cells.length;i++){
            // 一条蛇的 每个身子 从头部后面开始美化
            const a = this.cells[i-1], b = this.cells[i];

            if(Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps){
                continue;
            }
            // 竖直方向
            if(Math.abs(a.x - b.x) < this.eps){
                ctx.fillRect((a.x - 0.5 + 0.1) * L, Math.min(a.y,b.y) * L,L * 0.8, Math.abs(a.y - b.y) * L);
            }
            // 水平方向 画长方形
            else{
                ctx.fillRect(Math.min(a.x,b.x) * L,(a.y - 0.5 + 0.1) * L, Math.abs(a.x - b.x) * L, L * 0.8)
            }
        }
        
        ctx.fillStyle = "black";
        // i 枚举左眼右眼
        for(let i = 0;i < 2;i++){
            const eye_x = (this.cells[0].x + this.eye_dx[this.eye_direction][i]  * 0.15) * L;
            const eye_y = (this.cells[0].y + this.eye_dy[this.eye_direction][i] * 0.15) * L ;

            ctx.beginPath();
            ctx.arc(eye_x, eye_y, L * 0.05,0, Math.PI * 2)
            ctx.fill();
        }
    }
}