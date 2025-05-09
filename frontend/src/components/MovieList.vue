<template>
  <div>
    <h2>Filmliste</h2>
    <div v-if="movies.length === 0">Lade Filme...</div>
    <div v-else class="movie-card" v-for="movie in movies" :key="movie.tmdbId">
      <img :src="movie.posterUrl" :alt="movie.title" width="150" />
      <h3>{{ movie.title }}</h3>
      <p><strong>Genre:</strong> {{ movie.genre }}</p>
      <p><strong>Plattform:</strong> {{ movie.platform }}</p>
      <p><strong>Status:</strong> {{ movie.status }}</p>
      <p><strong>Meine Bewertung:</strong> {{ movie.personalRating }}/10</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PopcornPilot',
  data() {
    return {
      movies: []
    };
  },
  mounted() {
    fetch('http://localhost:8080/movies')
        .then(response => response.json())
        .then(data => {
          this.movies = data;
        });
  }
};
</script>

<style scoped>
.movie-card {
  border: 1px solid #ddd;
  padding: 16px;
  margin: 12px auto;
  border-radius: 8px;
  background-color: #1e1e1e; /* dunkler Hintergrund */
  color: #f0f0f0; /* heller Text */
  width: 90%;
  max-width: 500px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
  text-align: left;
}
.movie-card img {
  width: 100px;
  float: left;
  margin-right: 16px;
}
.movie-card h3 {
  margin-top: 0;
}
.movie-card::after {
  content: "";
  display: block;
  clear: both;
}
</style>

