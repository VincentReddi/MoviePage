<template>
  <div class="home-view">
    <div class="search-bar">
      <input v-model="query" @input="searchMovies" placeholder="Search for movies..." />
      <ul v-if="results.length">
        <li v-for="movie in results" :key="movie.id">{{ movie.title }}</li>
      </ul>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'

export default {
  name: 'HomeView',
  setup() {
    const query = ref('')
    const results = ref([])

    const searchMovies = async () => {
      if (query.value.trim()) {
        try {
          const apiKey = '3e1a60c002b082d8f881975fa6a5fe50'
          const response = await fetch(`https://api.themoviedb.org/3/search/movie?api_key=${apiKey}&query=${query.value}`)
          const data = await response.json()
          results.value = data.results || []
        } catch (error) {
          console.error('Error fetching movies:', error)
        }
      } else {
        results.value = []
      }
    }

    return {
      query,
      results,
      searchMovies,
    }
  },
}
</script>

<style scoped>
.search-bar {
  margin: 20px;
}
</style>