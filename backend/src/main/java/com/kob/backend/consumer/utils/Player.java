package com.kob.backend.consumer.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id;

//    起始坐标
    private Integer sx;
    private Integer sy;

//    记录每名玩家走的方向序列 存每一次的指令  playerA.getSteps().add(nextStepA);
    private List<Integer> steps;

    // 检验当前回合，蛇的长度是否增加
    private boolean check_tail_increasing(int step) {
        if (step <= 10) return true;
        return step % 3 == 1;
    }

    //    判断蛇的身体有哪些
    public List<Cell> getCells(){
        List<Cell> res = new ArrayList<>();

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
//        回合数
        int step = 0;
//        加入起点
        res.add(new Cell(x, y));
        for (int d: steps) {
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            if (!check_tail_increasing( ++ step)) {
//               如果蛇尾 不增加 删掉蛇尾 因为蛇尾在第一个 所以index 为 0
                res.remove(0);
            }
        }
        return res;

    }

}
