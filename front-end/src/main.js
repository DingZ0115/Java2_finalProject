import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import installElementPlus from './plugins/element'
import api from './apis'
import VueParticles from 'vue-particles'

const app = createApp(App)
installElementPlus(app)
app.use(VueParticles)
app.config.globalProperties.$api = api;
app.use(store).use(router).mount('#app')