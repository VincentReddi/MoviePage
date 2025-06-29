<template>
  <div class="movie-search">
    <input
        type="text"
        v-model="query"
        placeholder="Nach Filmen suchen..."
        @input="searchMovies"
    />
    <div v-if="results.length">
      <h3>Suchergebnisse:</h3>
      <ul>
        <li v-for="movie in results" :key="movie.id">
          {{ movie.title }} ({{ movie.release_date?.slice(0, 4) || 'N/A' }})
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MovieSearch',
  data() {
    return {
      query: '',
      results: []
    }
  },
  methods: {
    async searchMovies() {
      if (this.query.length < 2) {
        this.results = []
        return
      }

      const apiKey = '3e1a60c002b082d8f881975fa6a5fe50'
      const url = `https://api.themoviedb.org/3/search/movie?query=${encodeURIComponent(this.query)}&api_key=${apiKey}&language=de`

      try {
        const response = await fetch(url)
        const data = await response.json()
        this.results = data.results
      } catch (error) {
        console.error('Fehler bei der TMDb-Suche:', error)
        this.results = []
      }
    }
  }
}
</script>

<style scoped>
.movie-search {
  padding: 1rem;
}
input[type="text"] {
  width: 100%;
  padding: 0.5rem;
  font-size: 1rem;
  margin-bottom: 1rem;
}
</style>
