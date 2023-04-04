export class Cell{
    constructor(r,c){
        this.r = r;
        this.c = c;

        // 拿到真正的坐标
        this.x = c + 0.5
        this.y = r + 0.5
    }
}