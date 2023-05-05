<template>
    <div class="body">

        <div class="stars" ref="starsRef">
            <div class="star" v-for="(item, index) in starsCount" :key="index">
                <p class="words" text="老鹅生日快乐">

                </p>
            </div>

        </div>


    </div>



    <!-- <div id="carouselExampleDark" class="carousel carousel-dark slide" data-bs-ride="carousel">
        <div class="carousel-indicators">
            <button type="button" data-bs-target="#carouselExampleDark" data-bs-slide-to="0" class="active"
                aria-current="true" aria-label="Slide 1"></button>
            <button type="button" data-bs-target="#carouselExampleDark" data-bs-slide-to="1" aria-label="Slide 2"></button>
            <button type="button" data-bs-target="#carouselExampleDark" data-bs-slide-to="2" aria-label="Slide 3"></button>
        </div>
        <div class="carousel-inner">
            <div class="carousel-item active" data-bs-interval="10000">
                <img src="../../assets/img/background.png" class="d-block w-100" alt="...">
                <div class="carousel-caption d-none d-md-block">
                    <h5>First slide label</h5>
                    <p>Some representative placeholder content for the first slide.</p>
                </div>
            </div>
            <div class="carousel-item" data-bs-interval="2000">
                <img src="../../assets/img/background.png" class="d-block w-100" alt="...">
                <div class="carousel-caption d-none d-md-block">
                    <h5>Second slide label</h5>
                    <p>Some representative placeholder content for the second slide.</p>
                </div>
            </div>
            <div class="carousel-item">
                <img src="../../assets/img/background.png" class="d-block w-100" alt="...">
                <div class="carousel-caption d-none d-md-block">
                    <h5>Third slide label</h5>
                    <p>Some representative placeholder content for the third slide.</p>
                </div>
            </div>
        </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleDark" data-bs-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Previous</span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleDark" data-bs-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Next</span>
        </button>
    </div> -->
</template>

<script>
export default {
    data() {
        return {
            starsCount: 800, //星星数量
            distance: 900, //间距
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