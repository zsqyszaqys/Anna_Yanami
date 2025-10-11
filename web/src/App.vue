<template>
 <div>
    <UserLogin></UserLogin>
 </div>
  <router-view/>
</template>


<script>
//import NavBar from "@/components/NavBar.vue";
import UserLogin from "@/views/user/account/UserAccountLoginView.vue";

export default{
  components: {
  //  NavBar,
    UserLogin,
  },
}
</script>


<style>
</style>
