<template>
  <div class="home">
    <h1>🎬 Willkommen bei PopcornPilot</h1>
    <SearchMovies />
  </div>
</template>

<script>
import SearchMovies from './SearchMovies.vue'

export default {
  components: {
    SearchMovies
  }
}
</script>

<style scoped>
.home {
  padding: 20px;
}
h1 {
  text-align: center;
  margin-bottom: 30px;
}
</style>