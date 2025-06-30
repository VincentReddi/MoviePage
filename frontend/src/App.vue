<template>
  <div id="app">
    <h1 class="title">🍿 PopcornPilot</h1>
    <MovieSearch />
  </div>
</template>

<script>
import MovieSearch from './components/MovieSearch.vue'

export default {
  name: 'App',
  components: {
    MovieSearch
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  padding: 2rem;
}
</style>
