<template>
  <div class="home-view">
    <div v-if="!selectedMovie" class="search-bar">
      <h1>🎥 Discover Movies</h1>
      <input v-model="query" @input="searchMovies" placeholder="Search for movies..." />
      <ul v-if="results.length" class="results">
        <li v-for="movie in results" :key="movie.id" class="movie-card" @click="openDetail(movie.id)">
          <img :src="getPosterUrl(movie.poster_path)" alt="Poster" />
          <div class="movie-info">
            <h3>{{ movie.title }}</h3>
            <p>{{ movie.release_date?.slice(0, 4) || '–' }}</p>
          </div>
        </li>
      </ul>
      <div v-else-if="searched" class="no-results">No results found.</div>
    </div>
    <div v-else class="movie-detail">
      <button @click="closeDetail" class="back-button">Back</button>
      <div class="movie-info">
        <img :src="getPosterUrl(selectedMovie.poster_path)" alt="Poster" />
        <h1>{{ selectedMovie.title }}</h1>
        <p><strong>Release Date:</strong> {{ selectedMovie.release_date }}</p>
        <p><strong>Overview:</strong> {{ selectedMovie.overview || 'No description available.' }}</p>
        <div class="rating-section">
          <label for="rating">Rate this movie:</label>
          <input type="range" id="rating" v-model="rating" min="0" max="10" step="1" />
          <p>Your rating: {{ rating }}/10</p>
          <button @click="updateRating(selectedMovie.id)">Save Rating</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'

export default {
  name: 'HomeView',
  setup() {
    const apiKey = '3e1a60c002b082d8f881975fa6a5fe50'
    const query = ref('')
    const results = ref([])
    const searched = ref(false)
    const selectedMovie = ref(null)
    const rating = ref(0)

    const searchMovies = async () => {
      if (!query.value.trim()) return
      const url = `https://api.themoviedb.org/3/search/movie?api_key=${apiKey}&query=${encodeURIComponent(query.value)}`
      const res = await fetch(url)
      const data = await res.json()
      results.value = data.results || []
      searched.value = true
    }

    const getPosterUrl = (path) =>
        path ? `https://image.tmdb.org/t/p/w200${path}` : 'https://via.placeholder.com/200x300?text=No+Image'

    const openDetail = async (id) => {
      const url = `https://api.themoviedb.org/3/movie/${id}?api_key=${apiKey}`
      const res = await fetch(url)
      const data = await res.json()
      selectedMovie.value = data
    }

    const closeDetail = () => {
      selectedMovie.value = null
      rating.value = 0
    }

    const updateRating = async (tmdbId) => {
      if (!rating.value) {
        alert('Please select a rating before saving.')
        return
      }

      try {
        const response = await fetch(`https://popcornpilot-backend-new.onrender.com/api/movies/${tmdbId}/rating`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ rating: rating.value })
        })

        if (!response.ok) {
          const errorData = await response.json()
          console.error('Error saving rating:', errorData)
          alert('Failed to save rating. Please try again.')
          return
        }

        alert('Rating saved successfully!')
      } catch (error) {
        console.error('Network error:', error)
        alert('Error saving rating. Please check your connection.')
      }
    }

    return {
      query,
      results,
      searched,
      selectedMovie,
      rating,
      searchMovies,
      getPosterUrl,
      openDetail,
      closeDetail,
      updateRating,
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
  cursor: pointer;
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

.no-results {
  color: #999;
  font-size: 1.2rem;
  margin-top: 20px;
}

.movie-detail {
  text-align: center;
  margin-top: 20px;
}

.movie-detail img {
  width: 300px;
  border-radius: 10px;
  margin-bottom: 20px;
}

.movie-detail h1 {
  font-size: 2rem;
  color: #333;
  margin-bottom: 10px;
}

.movie-detail p {
  font-size: 1rem;
  color: #666;
  margin-bottom: 10px;
}

.rating-section {
  margin-top: 20px;
}

.rating-section label {
  font-size: 1rem;
  color: #333;
}

.rating-section input {
  margin: 10px 0;
  width: 100%;
}

.rating-section p {
  font-size: 1rem;
  color: #666;
}

.back-button {
  padding: 10px 20px;
  margin-bottom: 20px;
  background: #007bff;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.back-button:hover {
  background: #0056b3;
}
</style>