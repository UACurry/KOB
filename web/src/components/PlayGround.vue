<template>
    <div class="playground">
        <!-- 在playground上 形成一个新的蒙版 -->
        <GameMap>

        </GameMap>
    </div>
</template>


<script>
import GameMap from './GameMap.vue'

export default {
    components: {
        GameMap,
    }
}
</script>

<style scoped>
div.playground {
    width: 60vw;
    height: 70vh;
    /* 上方40  左右auto就是居中*/
    margin: 40px auto;
}
</style>
