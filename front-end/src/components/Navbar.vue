<template>
  <div class="clientNavbar">
    <el-menu class="navbar" :default-active="this.$route.path" router mode="horizontal" @open="handleOpen"
             @close="handleClose" background-color="#00133d" text-color="#fff" active-text-color="#ffd04b">

      <div class="navbar-container">
        <el-menu-item index="/numberOfAnswers">Number Of Answers</el-menu-item>
        <el-menu-item index="/acceptedAnswers">Accepted Answers</el-menu-item>
        <el-menu-item index="/tags">Tags</el-menu-item>
        <el-menu-item index="/users">Users</el-menu-item>
        <el-menu-item index="/javaAPI">Java API</el-menu-item>
      </div>
    </el-menu>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isLogin: false,
      role: "",
      name: ""
    }
  },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    logOut() {
      this.isLogin = false
      sessionStorage.removeItem('state')
      this.$message({
        showClose: true,
        message: "您已退出登录",
        type: "success"
      });
    }
  },
  mounted() {
    //用state判断，防止显示出错误
    if (sessionStorage.getItem('state')) {
      this.isLogin = true
      this.role = JSON.parse(sessionStorage.getItem('state')).user.role
      this.name = JSON.parse(sessionStorage.getItem('state')).user.name
    }
  },
}
</script>

<style scoped>
.navbar {
  position: relative;
  z-index: 100;
}

.navbar-container {
  display: flex;
  justify-content: flex-end;
}
.el-menu{
  border:0!important;
}
</style>

