<!-- 用于在对战界面生成一个新的蒙版 -->
<template>
    <div ref="parent" class="gamemap">
        <!-- tabindex="0" 可以获得用户操作 -->
        <canvas ref="canvas" tabindex="0"></canvas>
    </div>
</template>

<script>
import { GameMap } from "../assets/script/GameMap"
import { ref, onMounted } from 'vue'
import { useStore } from "vuex";

export default {
    setup() {
        const store = useStore();
        let parent = ref(null);
        let canvas = ref(null);

        onMounted(() => {
            // 创建一个GameMap对象
            // 在 mdn 搜讲解
            // new GameMap(canvas.value.getContext('2d'), parent.value, store)
            store.commit(
                "updateGameObject",
                new GameMap(canvas.value.getContext('2d'), parent.value, store)
            );

        });

        return {
            parent,
            canvas
        }
    }
}
</script>

<style scoped>
div.gamemap {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>