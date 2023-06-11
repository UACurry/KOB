<template>
    <button @click="stop">停止</button>
    <button @click="start">游戏开始</button>
    <div style="color: red; text-align: center;font-size: 25px">score:{{ scroce }}</div>

    <div class="box" :style="{ width: boxWidth + 'px', height: boxHeight + 'px' }">
        <div class="str">{{ str }}</div>
        <div class="kuaiBox">
            <div class="kuai" v-for="(item, index) in arr" :key="index" :style="{ opacity: item.active ? '0' : '1' }"></div>
        </div>
        <div class="ball" :style="{ left: x + 'px', top: y + 'px', width: ball + 'px', height: ball + 'px' }"></div>
        <div class="bottomMove"
            :style="{ left: mx + 'px', top: my + 'px', width: moveBottomW + 'px', height: moveBottomH + 'px' }"></div>
    </div>
</template>

<script setup>
import { onMounted, onUnmounted, reactive, toRefs } from 'vue'

const boxWidth = 500, // 场景宽度
    boxHeight = 300, // 场景高度
    ball = 10,//小球的宽高
    moveBottomH = 20,//移动方块高度
    moveBottomW = 100//移动方块快读

const strArr = '恭喜你,挑战成功!!'

//用reactive 保存一些可观察信息
const state = reactive({
    x: boxWidth / 2 - ball / 2,  // 小球x轴位置信息 计算默认位置在中间
    y: boxHeight - ball - moveBottomH, // 小球Y轴的位置信息 计算默认位置在中间
    mx: boxWidth / 2 - moveBottomW / 2, //移动方块的位置信息 计算默认位置在中间
    my: boxHeight - moveBottomH, // 移动方块y轴的的位置信息  计算默认位置在中间
    // 被打击方块的数组
    arr: Array.from({ length: 50 }, (_, index) => {
        return {
            index,
            active: false
        }
    }),
    str: '', // 返回挑战成功字眼
    scroce: 0 // 分数
})
// 用toRefs将观察对象的信息解构出来供模板使用 
const { x, y, mx, my, arr, str, scroce } = toRefs(state)
let timer = null, // 小球定时器
    speed = 3,// 小球速度
    map = { x: 10, y: 10 },
    timer2 = null, // 挑战成功字眼显示定时器
    index = 0//挑战成功字眼续个显示的索引值

// 挑战成功字眼续个显示的方法
const strFun = () => {
    if (strArr.length === index) clearInterval(timer2)
    state.str += strArr.substr(index, 1)
    index++
}


//移动小球的方法  
// 1.这里同过变量map 对象来记录坐标信息, 确定小球碰到 左右上 及移动方块是否回弹
// 2.循环砖块检测小球碰撞到砖块消失
const moveBall = () => {
    const { offsetTop, offsetHeight, offsetLeft, offsetWidth } = document.querySelector('.bottomMove')
    if (state.x <= 0) {
        map.x = speed
    } else if (state.x > boxWidth - ball) {
        map.x = -speed
    }
    if (state.y <= 0) {
        map.y = speed
    }
    if (state.y >= offsetTop - offsetHeight &&
        state.y <= offsetTop + offsetHeight &&
        state.x >= offsetLeft &&
        state.x < offsetLeft + offsetWidth) {
        map.y = -speed
    }
    if (state.y > boxHeight) {
        clearInterval(timer)
        alert('game over')
        window.location.reload()
    }
    Array.from(state.arr).forEach((item, index) => {
        const {
            offsetLeft,
            offsetTop,
            offsetWidth,
            offsetHeight
        } = document.querySelectorAll('.kuai')[index]
        if (state.x > offsetLeft
            && state.x < offsetLeft + offsetWidth
            && state.y > offsetTop
            && state.y < offsetTop + offsetHeight) {
            if (!state.arr[index].active) {
                state.scroce += 100
            }
            state.arr[index].active = true
        }
    })
    if (Array.from(state.arr).every(item => item.active)) {
        clearInterval(timer)
        timer2 = setInterval(strFun, 1000)
    }
    state.x = state.x += map.x
    state.y = state.y += map.y
}

//移动方块左右移动方法 ,接住小球
const bottomMove = ev => {
    if (ev.code === 'Space') clearInterval(timer)
    switch (ev.key) {
        case 'ArrowRight':
            state.mx += 100
            break
        case 'ArrowLeft':
            state.mx -= 100
            break
    }
    state.mx = state.mx < 0 ? 0 : state.mx
    state.mx = state.mx > boxWidth - moveBottomW ? boxWidth - moveBottomW : state.mx
}
// 暂停游戏
const stop = () => {
    clearInterval(timer)
}
// 开始游戏 
const start = () => {
    timer = setInterval(moveBall, 20)
}

// 绑定移动方块事件
onMounted(() => {
    document.addEventListener('keyup', bottomMove)
})
// 移动出移动方块事件
onUnmounted(() => {
    clearInterval(timer)
})
</script>

<style>
.bottomMove {
    width: 100px;
    height: 10px;
    background: red;
    position: absolute;
    transition-duration: 100ms;
    transition-timing-function: ease-out;
}

.ball {
    width: 20px;
    height: 20px;
    background-color: red;
    border-radius: 50%;
    position: absolute;
}

.kuaiBox {
    display: flex;
    flex-wrap: wrap;
}

.kuai {
    width: 30px;
    height: 10px;
    background: red;
    margin: 10px;
    transition-duration: 100ms;
    transition-timing-function: ease-in;
}

.str {
    text-align: center;
    font-size: 50px;
    color: red;

}

.box {

    justify-content: center;
    width: 500px;
    height: 500px;
    margin: 0 auto;
    position: relative;
    border: 5px solid red;
    overflow: hidden;
}

.picker {
    width: 50px;
    height: 50px;
}
</style>