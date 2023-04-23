package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 用来标记蛇的身体的每一个单元
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cell {
    int x,y;
}
