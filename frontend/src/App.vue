
<template>
  <div id="app">
    <LoginPage v-if="!isLoggedIn" @login-success="isLoggedIn = true" />
    <div v-else>
      <!-- Deine Hauptkomponenten -->
      <MovieSearch />
    </div>
  </div>
</template>

<script>
import LoginPage from './components/LoginPage.vue'
import MovieSearch from './components/MovieSearch.vue'

export default {
  components: {
    LoginPage,
    MovieSearch
  },
  data() {
    return {
      isLoggedIn: false
    }
  }
}
</script>

<style>
/* Optional global styling */

</style>
