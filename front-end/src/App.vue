<template>
  <div id="app">
    <div class="background">
      <vue-particles color="#dedede" :particleOpacity="0.7" :particlesNumber="80" shapeType="circle"
                     :particleSize="4" linesColor="#dedede" :linesWidth="1" :lineLinked="true" :lineOpacity="0.4"
                     :linesDistance="150" :moveSpeed="3" :hoverEffect="true" hoverMode="grab" :clickEffect="true"
                     clickMode="push">
      </vue-particles>
      <router-view></router-view>
    </div>
  </div>

</template>

<script>

export default {
  name: 'app',
}
</script>
<style>
/* 有了这一段，走马灯可以布满整个屏幕了 */
body {
  margin: 0;
  padding: 0;
  border: 0;
}

#picture {
  background: url("assets/test.png");
  width: 100%;
  height: 100%;
  position: fixed;
  background-size: 100% 100%;
}

#particles-js {
  width: 100%;
  height: 100%;
  position: absolute;
  /* 设置absolute,其他DIV设置为relative，这样这个例子效果就作为背景 */
}

.background {
  position: relative;
  min-height: 100vh;
  height: 100%;
  width: 100%;
  background: #00133d;
}
</style>