import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';
import router from './router'
import './theme/index.css';
import axios from 'axios';
import md5 from 'js-md5';

Vue.config.productionTip = false;

Vue.use(ElementUI);
Vue.prototype.$axios = axios;
Vue.prototype.$md5 = md5;
Vue.prototype.$server = "http://localhost";
Vue.prototype.$localServer = "http://localhost";
Vue.prototype.$url = Vue.prototype.$server +  ":8080";
Vue.prototype.$salt = "QYXTQL%%%%%11231LDLJHHAHSACOASJCPJASODPP:LNKJDAS@123123qwe123dfaf513234";

new Vue({
  router,
  render: h => h(App),

}).$mount('#app');
