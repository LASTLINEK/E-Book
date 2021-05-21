import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

// 引入样式
import ElementUI from 'element-ui'
 import Confirm from 'element-ui'
 import 'element-ui/lib/theme-chalk/index.css'

// 粒子特效
import VueParticles from 'vue-particles'

import 'font-awesome/css/font-awesome.css'

// 引入axios
import axios from "axios"
import VueAxios from "vue-axios"

// 引入dialog可移动
import './directives.js'

Vue.use(ElementUI)
// 粒子特效
Vue.use(VueParticles)
Vue.use(VueAxios, axios)
Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
