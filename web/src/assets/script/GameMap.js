import { extend } from "jquery";
import { AcGameObject } from "./AcGameObject";


export class GameMap extends AcGameObject{
    // ctx 画布  parent 画布的父元素 用来动态修改画布的长宽
    constructor(ctx, parent){
        super();

        this.ctx = ctx;
        this.parent = parent;
    }
}