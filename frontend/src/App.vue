<script setup>
import MovieList from './components/MovieList.vue'
</script>

<template>
  <div id="app">
    <h1>🎬 MoviePage</h1>
    <MovieSearch />
    <MovieList />
  </div>
</template>

<script>
import MovieSearch from './components/MovieSearch.vue'
import MovieList from './components/MovieList.vue'

export default {
  name: 'App',
  components: {
    MovieSearch,
    MovieList
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  padding: 2rem;
}
</style>