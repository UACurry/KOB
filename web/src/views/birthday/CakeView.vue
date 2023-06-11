<template>
    <div class="body">
        <div class="stars" ref="starsRef">
            <div class="star" v-for="(item, index) in starsCount" :key="index">
                <p class="words" text="">
                    老鹅快乐常在!
                </p>
            </div>
        </div>
    </div>
</template>

<script>

export default {
    data() {
        return {
            starsCount: 666, //星星数量
            distance: 999, //间距
        }
    },
    mounted() {

        console.log(this.$refs.starsRef.children);
        let starNodes = Array.from(this.$refs.starsRef.children);
        starNodes.forEach((item) => {
            let speed = 0.2 + Math.random() * 1;
            let thisDistance = this.distance + Math.random() * 300;
            item.style.transformOrigin = `0 0 ${thisDistance}px`;
            item.style.transform =
                `
		        translate3d(0,0,-${thisDistance}px)
		        rotateY(${Math.random() * 360}deg)
		        rotateX(${Math.random() * -50}deg)
		        scale(${speed},${speed})`;
        });
    }
}
</script>

<style>
.body {
    position: absolute;
    width: 100%;
    height: 100%;
    margin: 0;
    padding: 0;
    background: radial-gradient(200% 100% at bottom center,
            #f7f7b6,
            #e96f92,
            #1b2947);
    background: radial-gradient(200% 105% at top center,
            #1b2947 10%,
            #75517d 40%,
            #e96f92 65%,
            #f7f7b6);
    background-attachment: fixed;
    overflow: hidden;
}

@keyframes rotate {
    0% {
        transform: perspective(400px) rotateZ(20deg) rotateX(-40deg) rotateY(0);
    }

    100% {
        transform: perspective(400px) rotateZ(20deg) rotateX(-40deg) rotateY(-360deg);
    }
}

.stars {
    transform: perspective(500px);
    transform-style: preserve-3d;
    position: absolute;
    perspective-origin: 50% 100%;
    left: 45%;
    animation: rotate 90s infinite linear;
    bottom: 0;
}

.star {
    width: 2px;
    height: 2px;
    background: #f7f7b6;
    position: absolute;
    left: 0;
    top: 0;
    backface-visibility: hidden;
}


/* * {
    margin: 0;
    padding: 0;
    font-size: 50px;
    color: #fff;
} */

.block {
    width: 500px;
    background-color: #65b4ae;
    white-space: nowrap;
    overflow: hidden;
}

.words {
    position: relative;
    width: fit-content;
    animation: move 4s linear infinite;
    padding-left: 50px;
    font-size: large;
}

.words::after {
    position: absolute;
    right: -100%;
    content: attr(text);
}

@keyframes move {
    0% {
        transform: translateX(0);
    }

    100% {
        transform: translateX(-100%);
    }
}
</style>