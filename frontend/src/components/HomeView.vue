<template>
  <div class="home-view">
    <div class="search-bar">
      <h1>🎥 Discover Movies</h1>
      <input v-model="query" @input="searchMovies" placeholder="Search for movies..." />
      <input v-model="genreQuery" placeholder="Search by genre..." />
      <button @click="writeTestToDatabase">Create Test Movie</button>
    </div>
    <ul v-if="filteredResults.length" class="results">
      <li v-for="movie in filteredResults" :key="movie.id" class="movie-card" @click="openDetail(movie.id)">
        <img :src="getPosterUrl(movie.poster_path)" alt="Poster" />
        <div class="movie-info">
          <h3>{{ movie.title }}</h3>
          <p>{{ movie.release_date?.slice(0, 4) || '–' }}</p>
          <p><strong>Genre:</strong> {{ movie.genre }}</p>
        </div>
      </li>
    </ul>
    <div v-else-if="searched" class="no-results">No results found.</div>
  </div>
</template>

<script>
import { ref, computed } from 'vue'

export default {
  name: 'HomeView',
  setup() {
    const apiKey = '3e1a60c002b082d8f881975fa6a5fe50'
    const query = ref('')
    const genreQuery = ref('')
    const results = ref([])
    const searched = ref(false)

    const searchMovies = async () => {
      if (!query.value.trim()) return
      const url = `https://api.themoviedb.org/3/search/movie?api_key=${apiKey}&query=${encodeURIComponent(query.value)}`
      const res = await fetch(url)
      const data = await res.json()
      results.value = data.results || []
      searched.value = true
    }

    const filteredResults = computed(() => {
      if (!genreQuery.value.trim()) return results.value
      return results.value.filter(movie => movie.genre?.toLowerCase().includes(genreQuery.value.toLowerCase()))
    })

    const getPosterUrl = (path) =>
        path ? `https://image.tmdb.org/t/p/w200${path}` : 'https://via.placeholder.com/200x300?text=No+Image'

    const openDetail = async (id) => {
      const url = `https://api.themoviedb.org/3/movie/${id}?api_key=${apiKey}`
      const res = await fetch(url)
      const data = await res.json()
      selectedMovie.value = data
    }

    const writeTestToDatabase = async () => {
      const testMovie = {
        title: "Interstellar",
        genre: "Sci-Fi",
        platform: "Netflix",
        personalRating: null,
        tmdbId: "123",
        posterUrl: "https://image.tmdb.org/t/p/original/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
        status: "Watched"
      };

      try {
        const response = await fetch('https://popcornpilot-backend-new.onrender.com/api/movies', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(testMovie)
        });

        if (!response.ok) {
          const errorData = await response.json();
          console.error('Error writing test movie to database:', errorData);
          alert(`Failed to write test movie to database. Error: ${errorData.message || 'Unknown error'}`);
          return;
        }

        alert('Test movie written to database successfully!');
      } catch (error) {
        console.error('Network error:', error);
        alert(`Error writing test movie to database. Network error: ${error.message}`);
      }
    }

    return {
      query,
      genreQuery,
      results,
      searched,
      searchMovies,
      filteredResults,
      getPosterUrl,
      openDetail,
      writeTestToDatabase,
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
</style>