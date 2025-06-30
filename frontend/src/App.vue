
<template>
  <div id="app">
    <LoginPage v-if="!isLoggedIn" @login-success="isLoggedIn = true" />
    <div v-else>
      <!-- Deine Hauptkomponenten -->
      <MovieSearch />
      <MovieList />
    </div>
  </div>
</template>

<script>
import LoginPage from './components/LoginPage.vue'
import MovieSearch from './components/MovieSearch.vue'
import MovieList from "./components/MovieList.vue";

export default {
  components: {
    MovieList,
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
