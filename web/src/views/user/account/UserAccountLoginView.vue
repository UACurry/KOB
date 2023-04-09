<template>
    <ContentFiled>
        <div class="row justify-content-md-center">
            <div class="col-3 ">
                <form>
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input type="text" class="form-control" id="username" placeholder="输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input type="password" class="form-control" id="password" placeholder="输入密码">
                    </div>
                    <button type="submit" class="btn btn-primary">提交</button>
                </form>
            </div>
        </div>
    </ContentFiled>
</template>

<script>
import ContentFiled from "../../../components/ContentField.vue"

export default {
    components: {
        ContentFiled
    }
}
</script>

<style scoped>
button {
    width: 100%;
}
</style>