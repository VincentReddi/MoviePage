<template>
  <div class="movie-search">
    <input
        type="text"
        v-model="query"
        placeholder="🔍 Nach Filmen suchen..."
        @input="searchMovies"
        class="search-input"
    />
    <div v-if="results.length" class="results">
      <div v-for="movie in results" :key="movie.id" class="movie-card">
        <img
            v-if="movie.poster_path"
            :src="'https://image.tmdb.org/t/p/w300' + movie.poster_path"
            :alt="movie.title"
            class="poster"
        />
        <div class="info">
          <h2>{{ movie.title }}</h2>
          <p class="meta">
            {{ movie.release_date?.slice(0, 4) || 'N/A' }} · ⭐ {{ movie.vote_average || '–' }}
          </p>
          <p class="overview">{{ movie.overview.slice(0, 150) }}...</p>
        </div>
      </div>
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
  padding: 2rem;
  max-width: 960px;
  margin: 0 auto;
}
.search-input {
  width: 100%;
  padding: 0.75rem 1rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 1rem;
  box-shadow: 0 2px 6px rgba(0,0,0,0.05);
  transition: all 0.3s ease;
}
.search-input:focus {
  outline: none;
  border-color: #007aff;
  box-shadow: 0 0 0 3px rgba(0,122,255,0.2);
}
.results {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-top: 2rem;
}
.movie-card {
  background: white;
  border-radius: 1.25rem;
  overflow: hidden;
  box-shadow: 0 6px 18px rgba(0,0,0,0.08);
  display: flex;
  flex-direction: column;
  transition: transform 0.2s ease;
}
.movie-card:hover {
  transform: translateY(-5px);
}
.poster {
  width: 100%;
  height: auto;
  object-fit: cover;
}
.info {
  padding: 1rem;
}
.info h2 {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: #111;
}
.meta {
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 0.5rem;
}
.overview {
  font-size: 0.9rem;
  color: #444;
}
</style>