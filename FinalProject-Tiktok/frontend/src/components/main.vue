<template>
    <el-container class="main-container">
        <el-col class="content">
            <h3 style="text-align: center; margin: 10px">主菜单</h3>
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
                    <el-row>
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
                    </el-row>
                    
                    <el-row class="row">
                        <el-col class="subcol">
                            <el-row class="subrow">
                                <el-button class="button" type="primary" size="medium" @click="lastVideo" plain>上个视频</el-button>
                                <el-button class="button" type="primary" size="medium" @click="nextVideo" plain>推荐视频!</el-button>
                            </el-row>
                            <el-input class="input" v-model="video.uuid" placeholder="UUID"></el-input>
                            <el-button class="button" type="primary" size="medium" @click="getVideo" plain>根据UUID查询单个视频</el-button>
                            <el-button class="button" type="primary" size="medium" @click="deleteVideo" plain>根据UUID删除单个视频</el-button>
                            <el-button class="button" type="primary" size="medium" @click="getAllVideos" plain>查询所有视频</el-button>
                            
                        </el-col>
                        <el-col class="subcol">
                            <el-row class="subrow">
                                <el-button :disabled="isVideoLiked" class="button" type="primary" size="medium" @click="likeVideo" plain>点赞视频!</el-button>
                                <el-button class="button" type="primary" size="medium" @click="resetRecommendation" plain>重置推荐</el-button>
                            </el-row>
                            <el-tooltip content="分页页码" placement="top">
                                <el-input-number class="input" v-model="video_page" :min="1" placeholder="分页页码" />
                            </el-tooltip>
                            <el-button class="button" type="primary" size="medium" @click="playVideo" plain>根据UUID播放单个视频</el-button>
                            <el-button class="button" type="primary" size="medium" @click="stopPlayingVideo" plain>关闭视频页</el-button>
                            <el-button class="button" type="primary" size="medium" @click="getMyVideos" plain>查询我的视频</el-button>
                        </el-col>
                    </el-row>
                </el-col>
            </el-row>
            <el-input class="output" v-model="token" placeholder="Token" disabled></el-input>
            <div v-if="videoUrl" class="subrow">
                <p>点赞数：</p>
                <el-input style="width: auto" v-model="videoLikes" placeholder="0" disabled></el-input>
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
            videoLikes: 0,
            recommendationsList: [],
            recommendationsCur: 0,

            haveLikedVideos: {},
            isVideoLiked: true,
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
                    password: that.encode(that.user.password),
                    email: that.user.email,
                    telephone: that.user.telephone,
                }),
            }) .then(function (response) {
                response
                that.output += "注册成功！"
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
                    password: that.encode(that.user.password),
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
        getMyVideos() {
            const that = this
            that.output = new Date().toLocaleString() + "\n"
            
            this.$axios({
                url: `http://localhost:8080/api/v1/my_videos?page=${that.video_page}`,
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
        getUuidFromVideoUrl() {
            const that = this
            if (that.videoUrl === "") {
                return null
            } else {
                const uuidPattern = /[a-zA-Z0-9-]+$/
                const match = that.videoUrl.match(uuidPattern)
                if (!match) {
                    return null
                }
                return match[0]
            }
        },
        updateIsVideoLiked() {
            const that = this
            window.console.log("!")
            if (that.videoUrl !== "") {
                const uuid = that.getUuidFromVideoUrl()
                that.video.uuid = uuid
                that.videoLikes = that.getVideoLikes(uuid)
                that.isVideoLiked = uuid in that.haveLikedVideos
            } else {
                that.isVideoLiked = true
            }
        },
        updateVideoUrl(url) {
            const that = this
            that.videoUrl = url
            that.updateIsVideoLiked()
        },
        playVideo() {
            const that = this
            that.output = new Date().toLocaleString() + "\n"
            if (that.video.uuid == "") {
                that.output += "Error: UUID is empty"
                return
            }
            that.updateVideoUrl("http://localhost:8080/api/v1/playable_videos/" + that.video.uuid)
        },
        stopPlayingVideo() {
            const that = this
            that.updateVideoUrl("")
        },
        // Recommendations
        lastVideo() {
            const that = this
            if (that.recommendationsCur > 0) {
                that.recommendationsCur -= 1;
                that.updateVideoUrl("http://localhost:8080/api/v1/playable_videos/" + that.recommendationsList[that.recommendationsCur].uuid)
            } else {
                window.alert("没有上一个视频啦/(ㄒoㄒ)/~~")
            }
        },
        nextVideo() {
            const that = this
            if (that.recommendationsList.length === 0) {
                that.$axios({
                    url: `http://localhost:8080/api/v1/videos/recommendations`,
                    method: 'get',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': that.token,
                    },
                }) .then(function (response) {
                    that.recommendationsList = response.data.data
                    that.recommendationsCur = 0
                    window.console.log(JSON.stringify(response.data.data, null, 4))
                    that.updateVideoUrl("http://localhost:8080/api/v1/playable_videos/" + that.recommendationsList[that.recommendationsCur].uuid)
                }) .catch(function (error) {
                    that.errorHandle(error)
                    that.recommendationsList = []
                    that.recommendationsCur = 0
                    that.updateVideoUrl("")
                })
            } else {
                if (that.recommendationsCur < that.recommendationsList.length - 1) {
                    that.recommendationsCur += 1
                    that.updateVideoUrl("http://localhost:8080/api/v1/playable_videos/" + that.recommendationsList[that.recommendationsCur].uuid)
                } else {
                    window.alert("整个网站的视频已经看完啦～(∠・ω< )⌒★！")
                }
            }
        },
        likeVideo() {
            const that = this
            if (that.videoUrl === "") return
            const uuidPattern = /[a-zA-Z0-9-]+$/
            const match = that.videoUrl.match(uuidPattern)
            if (!match) {
                window.alert("警告！")
            }
            const videoUuid = match[0]

            that.$axios({
                url: `http://localhost:8080/api/v1/videos/${videoUuid}/likes`,
                method: 'post',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': that.token,
                },
            }) .then(function (response) {
                response
                window.alert("点赞成功！")
                that.haveLikedVideos[videoUuid] = true
                that.videoLikes += 1
                that.isVideoLiked = true
            }) .catch(function (error) {
                that.errorHandle(error)
            })
        },
        resetRecommendation() {
            const that = this
            that.recommendationsList = []
            that.recommendationsCur = 0
            that.updateVideoUrl("")
        },
        getVideoLikes(videoUuid) {
            const that = this
            that.$axios({
                url: `http://localhost:8080/api/v1/videos/${videoUuid}/likes`,
                method: 'get',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': that.token,
                },
            }) .then(function (response) {
                that.videoLikes = response.data.data
            }) .catch(function (error) {
                that.errorHandle(error)
            })
        },
        encode(passwd) {
            let new_passwd = passwd
            for (let i = 0, j = 0; i < passwd.length; i++, j++) {
                new_passwd += passwd.charCodeAt(i) + j
            }
            for (let i = 0, j = 0; i < passwd.length; i++, j++) {
                new_passwd += passwd.charCodeAt(i) - j
            }
            return new_passwd.substring(0, passwd.length)
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

    .subrow {
        width: 100%;

        margin: 0;

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
        margin: 10px;
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
