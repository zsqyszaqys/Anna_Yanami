import { createRouter, createWebHistory } from 'vue-router'
import UserAccountOperateView from "@/views/user/account/UserAccountOperateView.vue";
import navBar from "@/components/NavBar.vue";
import store from "@/store";

const routes = [
  {
    path: '/user/account',
    name: 'AccountOperate',
    component: UserAccountOperateView,
    meta:{
      requestAuth: false,
    }
  },

  {
    path: "/",
    redirect: "/me",
    meta:{
      requestAuth: true,
    }
  },

  {
    path: "/me",
    name:"me",
    component: navBar,
    meta:{
      requestAuth: true,
    }
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})
router.beforeEach((to,from,next)=>{
  let jwt_token=localStorage.getItem('jwt_token');

  if(jwt_token){
    store.commit("updateToken", jwt_token);
    store.dispatch("getInfo", {
      success(resp){
        console.log(resp);
        next();
      },
      error(){
        next({name: "AccountOperate"});
      }
    })
  }else{
    if(to.meta.requestAuth && !store.state.user.is_login)
      next({name:"AccountOperate"});
    else
      next();
  }
})



export default router
