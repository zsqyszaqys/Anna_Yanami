<template>
 <div>
   <NavBar></NavBar>
 </div>
  <router-view/>
</template>


<script>
import NavBar from "@/components/NavBar.vue";

export default{
  components: {
    NavBar,
  },
}
</script>


<style>
</style>
