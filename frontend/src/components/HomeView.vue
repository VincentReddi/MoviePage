<template>
  <div class="home-view">
    <div class="search-bar">
      <h1>🎥 Discover Movies</h1>
      <input v-model="query" @input="searchMovies" placeholder="Search for movies..." />
      <select v-model="selectedGenre" @change="searchMovies">
        <option value="">Select Genre</option>
        <option v-for="genre in genres" :key="genre.id" :value="genre.id">
          {{ genre.name }}
        </option>
      </select>
      <button @click="writeTestToDatabase">Create Test Movie</button>
    </div>
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
</template>

<script>
import { ref, onMounted } from 'vue'

export default {
  name: 'HomeView',
  setup() {
    const apiKey = '3e1a60c002b082d8f881975fa6a5fe50'
    const query = ref('')
    const selectedGenre = ref('')
    const genres = ref([])
    const results = ref([])
    const searched = ref(false)

    const fetchGenres = async () => {
      const url = `https://api.themoviedb.org/3/genre/movie/list?api_key=${apiKey}`
      const res = await fetch(url)
      const data = await res.json()
      genres.value = data.genres || []
    }

    const searchMovies = async () => {
      if (!query.value.trim() && !selectedGenre.value) return
      const url = `https://api.themoviedb.org/3/discover/movie?api_key=${apiKey}&query=${encodeURIComponent(query.value)}&with_genres=${selectedGenre.value}`
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
      console.log(data)
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

    onMounted(() => {
      fetchGenres()
    })

    return {
      query,
      selectedGenre,
      genres,
      results,
      searched,
      searchMovies,
      getPosterUrl,
      openDetail,
      writeTestToDatabase,
    }
  },
}
</script>

<style scoped>
/* Add your styles here */
</style>