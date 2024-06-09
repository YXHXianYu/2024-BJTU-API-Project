<template>
    <el-container class="main-container">
        <el-col class="content">
            <h3 style="text-align: center">主菜单</h3>
            <el-row class="row">
                <el-col class="col">
                    <h4 class="title">用户面板</h4>
                    <el-input class="input" v-model="user.username" placeholder="用户名"></el-input>
                    <el-input class="input" v-model="user.password" placeholder="密码"></el-input>
                    <el-input class="input" v-model="user.email" placeholder="邮箱"></el-input>
                    <el-input class="input" v-model="user.telephone" placeholder="手机号"></el-input>
                    <el-input class="input" v-model="user.uuid" placeholder="UUID"></el-input>
                    <el-button class="button" type="primary" size="medium" @click="register" plain>注册</el-button>
                    <el-button class="button" type="primary" size="medium" @click="login" plain>登入</el-button>
                    <el-button class="button" type="primary" size="medium" @click="getUser" plain>根据UUID查询单个用户</el-button>
                    <el-button class="button" type="primary" size="medium" @click="getAllUsers" plain>查询所有用户</el-button>
                </el-col>
                <el-col class="col">
                    <h4 class="title">博客面板</h4>
                    <el-input class="input" v-model="blog.title" placeholder="标题"></el-input>
                    <el-input class="input" v-model="blog.content" placeholder="内容" type="textarea" :autosize="{minRows: 6, maxRows: 6}"></el-input>
                    <el-input class="input" v-model="blog.uuid" placeholder="UUID"></el-input>
                    <el-button class="button" type="primary" size="medium" @click="createBlog" plain>提交博客</el-button>
                    <el-button class="button" type="primary" size="medium" @click="getBlog" plain>根据UUID查询单个博客</el-button>
                    <el-button class="button" type="primary" size="medium" @click="updateBlog" plain>根据UUID更新单个博客</el-button>
                    <el-button class="button" type="primary" size="medium" @click="getAllBlogs" plain>查询所有博客</el-button>
                </el-col>
            </el-row>
            <el-input class="output" v-model="token" placeholder="Token" disabled></el-input>
            <el-input class="output" v-model="output" placeholder="输出区域" type="textarea" :autosize="{minRows: 13, maxRows: 13}"></el-input>
        </el-col>
    </el-container>
</template>

<script>
export default {
    data() {
        return {
            user: {
                username: "user",
                password: "20021012",
                email: "2943003@qq.com",
                telephone: "18123456789",
                uuid: "",
            },
            blog: {
                title: "博客标题",
                content: "博客内容\n第一行\n第二行",
                uuid: "",
            },
            output: "",
            token: "",
        }
    },
    methods: {
        errorHandle(error) {
            const that = this;
            if (error.response) {
                that.output += "Error: " + error.response.data.message
            } else if (error.request) {
                that.output += "Error: No response from server. Please check if the server is running and the network connection."
            } else {
                that.output += "Error: " + error.message
            }
        },
        // === User ===
        register() {
            const that = this
            that.output = new Date().toLocaleString() + "\n"
            
            this.$axios({
                url: "http://localhost:8080/api/v1/users",
                method: 'post',
                headers: { 'Content-Type': 'application/json' },
                data: JSON.stringify({
                    username: that.user.username,
                    password: that.user.password,
                    email: that.user.email,
                    telephone: that.user.telephone,
                }),
            }) .then(function (response) {
                that.output = JSON.stringify(response.data.data, null, 4)
            }) .catch(function (error) {
                that.errorHandle(error)
            })
        },
        login() {
            const that = this
            that.output = new Date().toLocaleString() + "\n"
            
            this.$axios({
                url: "http://localhost:8080/api/v1/sessions",
                method: 'post',
                headers: { 'Content-Type': 'application/json' },
                data: JSON.stringify({
                    username: that.user.username,
                    password: that.user.password,
                }),
            }) .then(function (response) {
                that.output += JSON.stringify(response.data.data, null, 4)
                that.token = response.data.data.token
            }) .catch(function (error) {
                that.errorHandle(error)
            })
        },
        getUser() {
            const that = this
            that.output = new Date().toLocaleString() + "\n"

            if (that.user.uuid == "") {
                that.output += "Error: UUID is empty"
                return
            }

            this.$axios({
                url: "http://localhost:8080/api/v1/users/" + that.user.uuid,
                method: 'get',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': that.token,
                },
                data: JSON.stringify({
                    uuid: that.user.uuid,
                }),
            }) .then(function (response) {
                that.output += JSON.stringify(response.data.data, null, 4)
            }) .catch(function (error) {
                that.errorHandle(error)
            })
        },
        getAllUsers() {
            const that = this
            that.output = new Date().toLocaleString() + "\n"
            
            this.$axios({
                url: "http://localhost:8080/api/v1/users",
                method: 'get',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': that.token,
                },
            }) .then(function (response) {
                that.output += JSON.stringify(response.data.data, null, 4)
            }) .catch(function (error) {
                that.errorHandle(error)
            })
        },
        // === Blog ===
        createBlog() {
            const that = this
            that.output = new Date().toLocaleString() + "\n"
            
            this.$axios({
                url: "http://localhost:8080/api/v1/blogs",
                method: 'post',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': that.token,
                },
                data: JSON.stringify({
                    title: that.blog.title,
                    content: that.blog.content,
                }),
            }) .then(function (response) {
                that.output += JSON.stringify(response.data.data, null, 4)
            }) .catch(function (error) {
                that.errorHandle(error)
            })
        },
        getBlog() {
            const that = this
            that.output = new Date().toLocaleString() + "\n"

            if (that.blog.uuid == "") {
                that.output += "Error: UUID is empty"
                return
            }

            this.$axios({
                url: "http://localhost:8080/api/v1/blogs/" + that.blog.uuid,
                method: 'get',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': that.token,
                },
                data: JSON.stringify({
                    uuid: that.blog.uuid,
                }),
            }) .then(function (response) {
                that.output += JSON.stringify(response.data.data, null, 4)
            }) .catch(function (error) {
                that.errorHandle(error)
            })
        },
        getAllBlogs() {
            const that = this
            that.output = new Date().toLocaleString() + "\n"
            
            this.$axios({
                url: "http://localhost:8080/api/v1/blogs",
                method: 'get',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': that.token,
                },
            }) .then(function (response) {
                that.output += JSON.stringify(response.data.data, null, 4)
            }) .catch(function (error) {
                that.errorHandle(error)
            })
        },
        updateBlog() {
            const that = this
            that.output = new Date().toLocaleString() + "\n"

            if (that.blog.uuid == "") {
                that.output += "Error: UUID is empty"
                return
            }

            this.$axios({
                url: "http://localhost:8080/api/v1/blogs/" + that.blog.uuid,
                method: 'patch',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': that.token,
                },
                data: JSON.stringify({
                    title: that.blog.title,
                    content: that.blog.content,
                }),
            }) .then(function (response) {
                that.output += JSON.stringify(response.data.data, null, 4)
            }) .catch(function (error) {
                that.errorHandle(error)
            })
        },
    }
}
</script>

<style scoped>

    .main-container {
        width: 100%;
        height: 100%;

        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
    }

    .content {
        width: 95%;
        height: 95%;

        display: flex;
        /* justify-content: center; */
        align-items: center;
        flex-direction: column;

        background-color: #fff;
    }

    .row {
        width: 100%;
        height: 100%;

        display: flex;
        justify-content: center;
        align-items: center;
        flex-wrap: nowrap;
        /* flex-direction: row; */
    }

    .col {
        margin: 5px;
        width: 50%;
        height: 100%;

        display: flex;
        /* align-items: flex-start; */
        flex-wrap: wrap;
        flex-direction: column;

        border: #000 2px solid;
        border-radius: 10px;
    }

    .title {
        text-align: center;
    }
    .button {
        margin: 2px;
    }
    .input {
        margin: 2px;
        width: auto;
    }
    .output {
        margin: 2px;
        width: 90%;
    }
</style>
