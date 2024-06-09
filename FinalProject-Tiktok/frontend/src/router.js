import Vue from 'vue'
import Router from 'vue-router'
//import permission from "@/components/permission.vue";

Vue.use(Router);

const router = new Router({
    mode: 'history',
    routes: [
        {
            path: '/',
            name: 'default',
            component: () => import('./components/main.vue')
        }
    ]
})

export default router