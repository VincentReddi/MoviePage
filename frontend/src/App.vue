<script setup>
import MovieList from './components/MovieList.vue'
import SearchMovies from './components/SearchMovies.vue'
</script>

<template>
  <div>
    <h1>PopcornPilot</h1>
    <SearchMovies />
    <MovieList />
  </div>
</template>

<style scoped>
h1 {
  text-align: center;
  margin-top: 20px;
}
</style>
