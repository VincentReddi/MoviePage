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
      <p><strong>Meine Bewertung:</strong> {{ movie.personalRating || "Keine" }}/10</p>

      <input
          type="number"
          v-model="ratingInputs[movie.tmdbId]"
          placeholder="Neue Bewertung (z. B. 4.5)"
          step="0.1"
          min="0"
          max="10"
      />
      <button @click="updateRating(movie.tmdbId)">Speichern</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PopcornPilot',
  data() {
    return {
      movies: [],
      ratingInputs: {}
    };
  },
  mounted() {
    fetch('https://popcornpilot-backend-new.onrender.com/api/movies')
        .then(response => response.json())
        .then(data => {
          this.movies = data;
        });
  },
  methods: {
    updateRating(tmdbId) {
      const value = this.ratingInputs[tmdbId];
      if (!value) return;

      fetch(`https://popcornpilot-backend-new.onrender.com/api/movies/${tmdbId}/rating`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(value)
      })
          .then(() => alert("Bewertung gespeichert!"))
          .catch(() => alert("Fehler beim Speichern."));
    }
  }
};
</script>

<style scoped>
.movie-card {
  border: 1px solid #ddd;
  padding: 16px;
  margin: 12px auto;
  border-radius: 8px;
  background-color: #1e1e1e;
  color: #f0f0f0;
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
