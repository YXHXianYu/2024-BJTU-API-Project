<template>
    <el-container class="main-container">
        <el-col class="content">
            <h3 style="text-align: center">主菜单</h3>
            <el-row class="row">
                <el-col class="col">
                    <h4 class="title">用户面板</h4>
                    <el-row class="row">
                        <el-col class="subcol">
                            <el-input class="input" v-model="user.username" placeholder="用户名"></el-input>
                            <el-input class="input" v-model="user.password" placeholder="密码"></el-input>
                            <el-input class="input" v-model="user.email" placeholder="邮箱"></el-input>
                            <el-input class="input" v-model="user.telephone" placeholder="手机号"></el-input>
                            <el-input class="input" v-model="user.uuid" placeholder="UUID"></el-input>
                        </el-col>
                        <el-col class="subcol">
                            <el-button class="button" type="primary" size="medium" @click="register" plain>注册</el-button>
                            <el-button class="button" type="primary" size="medium" @click="login" plain>登入</el-button>
                            <el-button class="button" type="primary" size="medium" @click="getUser" plain>根据UUID查询单个用户</el-button>
                            <el-button class="button" type="primary" size="medium" @click="getAllUsers" plain>查询所有用户</el-button>
                        </el-col>
                    </el-row>
                </el-col>
                <el-col class="col">
                    <h4 class="title">视频面板</h4>
                    <el-input v-model="video.title" placeholder="Video Title" />
                    <el-upload
                        class="upload"
                        ref="upload"
                        action="http://localhost:8080/api/v1/videos"
                        :on-preview="handlePreview"
                        :on-remove="handleRemove"
                        :on-success="handleSuccess"
                        :on-error="handleError"
                        :before-upload="beforeUpload"
                        :data="uploadData"
                        :file-list="fileList"
                        :auto-upload=true
                        :multiple=false
                        list-type="text">
                            <el-button class="button" type="primary" size="medium" slot="trigger" plain>上传视频</el-button>
                    </el-upload>
                    <!-- <el-button class="button" type="primary" size="medium" @click="submitUpload" plain>上传视频</el-button> -->
                    
                    <el-row class="row">
                        <el-col class="subcol">
                            <el-input class="input" v-model="video.uuid" placeholder="UUID"></el-input>
                            <el-button class="button" type="primary" size="medium" @click="getVideo" plain>根据UUID查询单个视频</el-button>
                            <el-button class="button" type="primary" size="medium" @click="deleteVideo" plain>根据UUID删除单个视频</el-button>
                            <el-button class="button" type="primary" size="medium" @click="getAllVideos" plain>查询所有视频</el-button>
                        </el-col>
                        <el-col class="subcol">
                            <el-tooltip content="分页页码" placement="top">
                                <el-input-number class="input" v-model="video_page" :min="1" placeholder="分页页码" />
                            </el-tooltip>
                            <el-button class="button" type="primary" size="medium" @click="playVideo" plain>根据UUID播放单个视频</el-button>
                            <el-button class="button" type="primary" size="medium" @click="stopPlayingVideo" plain>关闭视频页</el-button>
                        </el-col>
                    </el-row>
                </el-col>
            </el-row>
            <el-input class="output" v-model="token" placeholder="Token" disabled></el-input>
            <div v-if="videoUrl">
                <video controls :src="videoUrl" width="600"></video>
            </div>
            <el-input class="output" v-model="output" placeholder="输出区域" type="textarea" :autosize="{minRows: 19, maxRows: 19}"></el-input>
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
            video: {
                title: "视频标题",
                uuid: "",
            },
            output: "",
            token: "",
            fileList: [],
            videoUrl: "",
            video_page: 1,
        }
    },
    computed: {
        uploadData() {
            return {
                token: this.token,
                title: this.video.title,
            }
        }
    },
    methods: {
        errorHandle(error) {
            const that = this
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
                that.output += JSON.stringify(response.data.data, null, 4)
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
        // === Upload Video ===
        handlePreview(file) {
            window.console.log("handlePreview: ", file)
        },
        handleRemove(file, fileList) {
            window.console.log("handleRemove: ", file, fileList)
        },
        handleSuccess(response, file, fileList) {
            window.console.log("handleSuccess: ", response, file, fileList)
        },
        handleError(error, file, fileList) {
            const that = this
            that.output = new Date().toLocaleString() + "\n"

            window.console.log("handleError: ", error, file, fileList)
            that.output += "Error: " + error.message
        },
        beforeUpload(file) {
            window.console.log("1")
            if (!(file.type === 'video/mp4')) {
                this.$message.error('上传视频只能是 MP4 格式!')
                return false
            }
            if (this.fileList.length !== 0) {
                this.$message.error('只能选择一个视频文件');
                return false;
            }
            if (this.video.title == "") {
                this.$message.error('请填写视频标题!')
                return false
            }
            return true
        },
        // === Video ===
        createVideo() {
            return
            // const that = this
            // that.output = new Date().toLocaleString() + "\n"
            // this.$axios({
            //     url: "http://localhost:8080/api/v1/videos",
            //     method: 'post',
            //     headers: {
            //         'Content-Type': 'application/json',
            //         'Authorization': that.token,
            //     },
            //     data: JSON.stringify({
            //         title: that.video.title,
            //         content: that.video.content,
            //     }),
            // }) .then(function (response) {
            //     that.output += JSON.stringify(response.data.data, null, 4)
            // }) .catch(function (error) {
            //     that.errorHandle(error)
            // })
        },
        getVideo() {
            const that = this
            that.output = new Date().toLocaleString() + "\n"

            if (that.video.uuid == "") {
                that.output += "Error: UUID is empty"
                return
            }

            this.$axios({
                url: "http://localhost:8080/api/v1/videos/" + that.video.uuid,
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
        getAllVideos() {
            const that = this
            that.output = new Date().toLocaleString() + "\n"
            
            this.$axios({
                url: `http://localhost:8080/api/v1/videos?page=${that.video_page}`,
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
        deleteVideo() {
            const that = this
            that.output = new Date().toLocaleString() + "\n"

            if (that.video.uuid == "") {
                that.output += "Error: UUID is empty"
                return
            }

            this.$axios({
                url: "http://localhost:8080/api/v1/videos/" + that.video.uuid,
                method: 'delete',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': that.token,
                },
            }) .then(function (response) {
                if (response.data.data === undefined) {
                    that.output += response.data.message
                } else {
                    that.output += JSON.stringify(response.data.data, null, 4)
                }
            }) .catch(function (error) {
                that.errorHandle(error)
            })
        },
        playVideo() {
            const that = this
            that.output = new Date().toLocaleString() + "\n"
            if (that.video.uuid == "") {
                that.output += "Error: UUID is empty"
                return
            }
            that.videoUrl = "http://localhost:8080/api/v1/playable_videos/" + that.video.uuid
        },
        stopPlayingVideo() {
            const that = this
            that.videoUrl = ""
        },
    }
}
</script>

<style scoped>

    .upload {
        display: flex;
        flex-direction: column;
        align-items: flex-start;
    }

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
        /* flex-wrap: wrap; */
        flex-direction: column;

        border: #000 2px solid;
        border-radius: 10px;
    }

    .subcol {
        margin: 5px;
        width: 50%;
        height: 100%;

        display: flex;
        /* align-items: flex-start; */
        /* flex-wrap: wrap; */
        flex-direction: column;
    }

    .upload {
        margin: 2px;
        width: auto;

        display: flex;
        flex-direction: column;
        align-items: center;
    }
    .upload-container {
        width: 100%;
        display: flex;
        flex-direction: row;
        align-items: center;
    }

    .title {
        text-align: center;
    }
    .button {
        margin: 2px;
        width: auto;
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
