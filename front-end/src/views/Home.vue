<template>
  <div class="adminHome">
    <div class="logoBox">
      <router-link active-class="initial" to="HomePage">
        <div class="logo">
          <img src="../assets/YiJi.png" alt="">
        </div>
      </router-link>
    </div>
    <Navbar></Navbar>
  </div>
</template>

<script>
import Navbar from '@/components/Navbar.vue';

export default {
  name: "AdminHome",
  components: {
    Navbar
  },
  data() {
    return {
      path: ''
    }
  },
  // 判断路由
  mounted() {
    this.path = this.$route.path;
  },
  watch: {
    $route(to, from) {
      this.path = to.path
    }
  }
}
</script>

<style>
.logoBox {
  position: relative;
  z-index: 200;
  padding: 0 15px;
}

.logo {
  position: relative;
  float: left;
  margin-right: 8px;
  /* 当箭头鼠标移到会变成手式鼠标 */
  cursor: pointer;
}

.logo img {
  height: 44px;
  display: block;
  margin-top: calc((48px - 44px) / 2);
}

.show {
  display: flex;
  flex-wrap: wrap;
  text-align: center;
  width: 100%;
  margin-top: 3%;
  justify-content: center;
  align-items: center;
}

.text {
  font-size: 18px;
  line-height: 1.5;
  font-weight: bold;
  text-align: left;
  margin-bottom: 2%;
}

.title {
  font-size: 20px;
  color: #0c56ff;
  margin-bottom: 2%;
  font-style: oblique;
  font-weight: bold;
}
</style>