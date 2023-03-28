<template>
    <div class="container content-field">
        <div class="card">
            <div class="card-body">
                <!-- 要填充的内容 都放到slot里面  在view里面填写的内容都到slot里面-->
                <slot></slot>
            </div>
        </div>
    </div>
</template>

<script>

</script>

<style scoped>
div.content-field {
    margin-top: 20px;
}
</style>