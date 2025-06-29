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
        <button @click="addToList(selectedMovie.title)">Add to My List</button>
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
    }

    const addToList = async (movieTitle) => {
      try {
        const response = await fetch('https://popcornpilot-backend-new.onrender.com/api/movies', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ title: movieTitle })
        })

        if (!response.ok) {
          const errorData = await response.json()
          console.error('Error adding movie to list:', errorData)
          alert('Failed to add movie to your list. Please try again.')
          return
        }

        alert('Movie added to your list successfully!')
      } catch (error) {
        console.error('Network error:', error)
        alert('Error adding movie to your list. Please check your connection.')
      }
    }

    return {
      query,
      results,
      searched,
      selectedMovie,
      searchMovies,
      getPosterUrl,
      openDetail,
      closeDetail,
      addToList,
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