<template>
  <div class="watchlist">
    <h1>📋 Meine Liste</h1>
    <div v-if="watchlist.length === 0">Noch keine Filme in der Liste.</div>
    <div v-for="movie in watchlist" :key="movie.id" class="movie-card">
      <h3>{{ movie.title }}</h3>
      <label>Status:
        <select v-model="movie.status" @change="updateStatus(movie.id, movie.status)">
          <option value="geplant">Geplant</option>
          <option value="gesehen">Gesehen</option>
          <option value="abgebrochen">Abgebrochen</option>
        </select>
      </label>
      <label>Bewertung:
        <input type="number" min="0" max="10" step="0.5" v-model.number="movie.rating" @change="updateRating(movie.id, movie.rating)" />
      </label>
      <button @click="removeFromList(movie.id)">Entfernen</button>
    </div>
  </div>
</template>

<script>
import { useWatchlistStore } from '../stores/watchlist'
import { storeToRefs } from 'pinia'

export default {
  setup() {
    const store = useWatchlistStore()
    const { movies: watchlist } = storeToRefs(store)

    const updateStatus = (id, status) => store.setStatus(id, status)
    const updateRating = (id, rating) => store.setRating(id, rating)
    const removeFromList = (id) => store.removeMovie(id)

    return {
      watchlist,
      updateStatus,
      updateRating,
      removeFromList
    }
  }
}
</script>

<style scoped>
.watchlist {
  padding: 20px;
}
.movie-card {
  margin-bottom: 20px;
  padding: 10px;
  border: 1px solid #ddd;
  background: #f9f9f9;
}
</style>
