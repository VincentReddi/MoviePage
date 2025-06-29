<template>
  <div v-if="movie" class="movie-detail">
    <h1>{{ movie.title }}</h1>
    <img :src="getPosterUrl(movie.poster_path)" alt="Poster" />
    <p><strong>Release:</strong> {{ movie.release_date }}</p>
    <p><strong>Genres:</strong> {{ genreNames }}</p>
    <p><strong>Beschreibung:</strong> {{ movie.overview }}</p>

    <button @click="addToList">Zur Liste hinzufügen</button>

    <div v-if="trailerUrl" style="margin-top: 20px;">
      <h3>🎬 Trailer</h3>
      <iframe :src="trailerUrl" width="560" height="315" frameborder="0" allowfullscreen></iframe>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useWatchlistStore } from '../stores/watchlist'

export default {
  setup() {
    const route = useRoute()
    const movie = ref(null)
    const trailerUrl = ref(null)
    const store = useWatchlistStore()

    const fetchMovie = async () => {
      const apiKey = '3e1a60c002b082d8f881975fa6a5fe50'
      const id = route.params.id
      const res = await fetch(`https://api.themoviedb.org/3/movie/${id}?api_key=${apiKey}&language=de`)
      const data = await res.json()
      movie.value = data

      // Trailer laden
      const trailerRes = await fetch(`https://api.themoviedb.org/3/movie/${id}/videos?api_key=${apiKey}&language=de`)
      const trailerData = await trailerRes.json()
      const youtubeTrailer = trailerData.results.find(v => v.type === 'Trailer' && v.site === 'YouTube')
      if (youtubeTrailer) {
        trailerUrl.value = `https://www.youtube.com/embed/${youtubeTrailer.key}`
      }
    }

    const addToList = () => {
      if (movie.value) store.addMovie(movie.value)
    }

    const getPosterUrl = path =>
        path ? `https://image.tmdb.org/t/p/w300${path}` : ''

    onMounted(fetchMovie)

    return {
      movie,
      getPosterUrl,
      addToList,
      trailerUrl,
      genreNames: computed(() =>
          movie.value?.genres?.map(g => g.name).join(', ') || ''
      )
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
