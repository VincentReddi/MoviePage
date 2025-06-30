<template>
  <div class="movie-list">
    <h2>🎬 Gespeicherte Filme</h2>

    <div v-if="movies.length" class="movie-grid">
      <div v-for="movie in movies" :key="movie.tmdbId" class="movie-card">
        <img :src="'https://image.tmdb.org/t/p/w300' + movie.posterPath" :alt="movie.title" />
        <h3>{{ movie.title }}</h3>
        <p>Status: {{ movie.status }}</p>
      </div>
    </div>

    <p v-else class="no-movies">Noch keine Filme gespeichert.</p>
  </div>
</template>

<script>
export default {
  name: 'MovieList',
  data() {
    return {
      movies: []
    }
  },
  async mounted() {
    try {
      const res = await fetch('https://popcornpilot-backend-new.onrender.com/api/movies')
      const data = await res.json()
      this.movies = data
    } catch (err) {
      console.error('Fehler beim Laden der Filme:', err)
    }
  }
}
</script>

<style scoped>
.movie-list {
  padding: 2rem;
  color: white;
  max-width: 1000px;
  margin: auto;
}
.movie-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap: 1.5rem;
}
.movie-card {
  background: #222;
  border-radius: 1rem;
  padding: 1rem;
  text-align: center;
  box-shadow: 0 4px 8px rgba(0,0,0,0.3);
}
.movie-card img {
  width: 100%;
  border-radius: 0.75rem;
  margin-bottom: 0.5rem;
}
.no-movies {
  text-align: center;
  margin-top: 2rem;
  color: #bbb;
}


</style>


