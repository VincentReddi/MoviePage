<template>
  <div class="search-container">
    <h2>🎬 Filme suchen oder entdecken</h2>

    <input v-model="query" @keyup.enter="searchMovies" placeholder="Film eingeben..." />
    <button @click="searchMovies">Suchen</button>

    <select v-model="selectedGenre" @change="filterByGenre">
      <option value="">Genre auswählen</option>
      <option v-for="genre in genres" :key="genre.id" :value="genre.id">{{ genre.name }}</option>
    </select>

    <button @click="loadRandomMovie">🎲 Zufallsvorschlag</button>

    <div v-if="results.length > 0" class="results">
      <div v-for="movie in results" :key="movie.id" class="movie-card">
        <img :src="getPosterUrl(movie.poster_path)" alt="Poster" />
        <div>
          <h3 @click="openDetail(movie.id)" style="cursor: pointer;">
            {{ movie.title }} ({{ movie.release_date?.slice(0, 4) || '–' }})
          </h3>
          <p>{{ movie.overview || 'Keine Beschreibung verfügbar.' }}</p>
          <button @click="addToWatchlist(movie)">Zur Liste hinzufügen</button>
        </div>
      </div>
    </div>

    <p v-else-if="searched">Keine Ergebnisse gefunden.</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useWatchlistStore } from '../stores/watchlist'

const apiKey = '3e1a60c002b082d8f881975fa6a5fe50'
const query = ref('')
const selectedGenre = ref('')
const genres = ref([])
const results = ref([])
const searched = ref(false)
const router = useRouter()
const store = useWatchlistStore()

const getPosterUrl = (path) =>
    path ? `https://image.tmdb.org/t/p/w200${path}` : 'https://via.placeholder.com/200x300?text=Kein+Bild'

const openDetail = (id) => {
  router.push(`/film/${id}`)
}

const addToWatchlist = (movie) => {
  store.addMovie(movie)
}

const searchMovies = async () => {
  if (!query.value.trim()) return
  const url = `https://api.themoviedb.org/3/search/movie?api_key=${apiKey}&language=de&query=${encodeURIComponent(query.value)}`
  const res = await fetch(url)
  const data = await res.json()
  results.value = data.results
  searched.value = true
}

const loadGenres = async () => {
  const url = `https://api.themoviedb.org/3/genre/movie/list?api_key=${apiKey}&language=de`
  const res = await fetch(url)
  const data = await res.json()
  genres.value = data.genres
}

const filterByGenre = async () => {
  if (!selectedGenre.value) return
  const url = `https://api.themoviedb.org/3/discover/movie?api_key=${apiKey}&language=de&with_genres=${selectedGenre.value}`
  const res = await fetch(url)
  const data = await res.json()
  results.value = data.results
  searched.value = true
}

const loadRandomMovie = async () => {
  const randomPage = Math.floor(Math.random() * 500) + 1
  const url = `https://api.themoviedb.org/3/discover/movie?api_key=${apiKey}&language=de&page=${randomPage}&sort_by=popularity.desc`
  const res = await fetch(url)
  const data = await res.json()
  results.value = [data.results[Math.floor(Math.random() * data.results.length)]]
  searched.value = true
}

onMounted(loadGenres)
</script>

<style scoped>
.search-container {
  padding: 20px;
}
input,
select {
  padding: 10px;
  margin-right: 10px;
  width: 300px;
}
button {
  padding: 10px;
  margin-top: 10px;
  cursor: pointer;
}
.results {
  margin-top: 20px;
}
.movie-card {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  background: #fff;
  padding: 10px;
  border-radius: 8px;
}
.movie-card img {
  width: 120px;
  border-radius: 5px;
}
</style>
