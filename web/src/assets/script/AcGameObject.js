// 实现是一个物体移动的基类  

// 存放所有游戏对象
const AC_GAME_OBJECT =  []

// 引入这个类 要export出去
export class AcGameObject{
    constructor() {
        AC_GAME_OBJECT.push(this);
        this.timedelta  = 0;
        this.has_called_start = false;
    }

    // 只执行一次
    start(){

    }

    // 每一帧执行一次
    update(){

    }
    
    // 在删除之前执行
    on_destroy(){

    }

    // 删除
    destroy(){
        // 在删除前执行
        this.on_destroy();

        for(let i in AC_GAME_OBJECT){
            const obj = AC_GAME_OBJECT[i];
            if(obj === this){
                AC_GAME_OBJECT.splice(i);
                break;
            }
        }
    }
}

// 上一次执行时刻
let last_timestamp;

// 保证浏览器每一帧都执行 传入 timestamp 表示当前函数执行的时刻
const step = timestamp => {
    for(let obj of AC_GAME_OBJECT){
        // 第一帧执行一下
        if(!obj.has_called_start){
            obj.has_called_start = true;
            obj.start();
        }
        else{
            // 如果执行过start函数 就执行update函数
            obj.timedelta = timestamp - last_timestamp;
            obj.update();
        }
    }

    last_timestamp = timestamp;
    // 第二帧继续执行
    requestAnimationFrame(step)
}

requestAnimationFrame(step)