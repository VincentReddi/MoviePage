<template>
  <div v-if="movie" class="movie-detail">
    <h1>{{ movie.title }}</h1>
    <img :src="getPosterUrl(movie.poster_path)" alt="Poster" />
    <p><strong>Release:</strong> {{ movie.release_date }}</p>
    <p><strong>Beschreibung:</strong> {{ movie.overview }}</p>
    <button @click="addToList">Zur Liste hinzufügen</button>
  </div>
</template>

<script>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { useWatchlistStore } from '../stores/watchlist'

export default {
  setup() {
    const route = useRoute()
    const movie = ref(null)
    const store = useWatchlistStore()

    const fetchMovie = async () => {
      const apiKey = '3e1a60c002b082d8f881975fa6a5fe50'
      const url = `https://api.themoviedb.org/3/movie/${route.params.id}?api_key=${apiKey}&language=de`

      const res = await fetch(url)
      const data = await res.json()
      movie.value = data
    }

    const addToList = () => {
      if (movie.value) store.addMovie(movie.value)
    }

    const getPosterUrl = (path) =>
        path ? `https://image.tmdb.org/t/p/w300${path}` : ''

    onMounted(fetchMovie)

    return {
      movie,
      addToList,
      getPosterUrl
    }
  }
}
</script>

<style scoped>
.movie-detail {
  padding: 20px;
}
img {
  max-width: 300px;
  border-radius: 10px;
}
</style>
