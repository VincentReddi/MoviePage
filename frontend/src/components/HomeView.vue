<template>
  <div class="home-view">
    <div class="search-bar">
      <h1>🎥 Discover Movies</h1>
      <input v-model="query" @input="searchMovies" placeholder="Search for movies..." />
      <ul v-if="results.length" class="results">
        <li v-for="movie in results" :key="movie.id" class="movie-card">
          <img :src="getPosterUrl(movie.poster_path)" alt="Poster" />
          <div class="movie-info">
            <h3>{{ movie.title }}</h3>
            <p>{{ movie.release_date?.slice(0, 4) || '–' }}</p>
          </div>
        </li>
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

    const getPosterUrl = (path) =>
        path ? `https://image.tmdb.org/t/p/w200${path}` : 'https://via.placeholder.com/200x300?text=No+Image'

    return {
      query,
      results,
      searchMovies,
      getPosterUrl,
    }
  },
}
</script>

<style scoped>
.home-view {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background: #f9f9f9;
  min-height: 100vh;
}

.search-bar {
  text-align: center;
  margin-bottom: 30px;
}

.search-bar h1 {
  font-size: 2.5rem;
  color: #333;
  margin-bottom: 20px;
}

.search-bar input {
  width: 80%;
  max-width: 500px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 1rem;
  margin-bottom: 20px;
}

.results {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
}

.movie-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 200px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.movie-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 10px rgba(0, 0, 0, 0.15);
}

.movie-card img {
  width: 100%;
  height: auto;
}

.movie-info {
  padding: 10px;
  text-align: center;
}

.movie-info h3 {
  font-size: 1.2rem;
  color: #333;
  margin: 5px 0;
}

.movie-info p {
  font-size: 0.9rem;
  color: #666;
}
</style>