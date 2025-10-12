import { createRouter, createWebHistory } from 'vue-router'
import UserAccountOperateView from "@/views/user/account/UserAccountOperateView.vue";
import navBar from "@/components/NavBar.vue";

const routes = [
  {
    path: "/",
    redirect: "/me" // 默认重定向到 /me
  },

  {
    path: "/me",
    name:"me",
    component: navBar,
  },

  {
    path: '/user/account',
    name: 'AccountOperate',
    component: UserAccountOperateView,
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
