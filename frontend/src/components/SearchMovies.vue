<template>
  <div class="search-container">
    <h2>🔍 Filme suchen</h2>
    <input v-model="query" @keyup.enter="searchMovies" placeholder="Film eingeben..." />
    <button @click="searchMovies">Suchen</button>

    <div v-if="results.length > 0" class="results">
      <div v-for="movie in results" :key="movie.id" class="movie-card">
        <img :src="getPosterUrl(movie.poster_path)" alt="Poster" />
        <div>
          <h3>{{ movie.title }} ({{ movie.release_date?.slice(0, 4) || '–' }})</h3>
          <p>{{ movie.overview || "Keine Beschreibung verfügbar." }}</p>
        </div>
      </div>
    </div>
    <p v-else-if="searched">Keine Ergebnisse gefunden.</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      query: "",
      results: [],
      searched: false,
    };
  },
  methods: {
    async searchMovies() {
      if (!this.query.trim()) return;

      const apiKey = "3e1a60c002b082d8f881975fa6a5fe50";
      const url = `https://api.themoviedb.org/3/search/movie?api_key=${apiKey}&language=de&query=${encodeURIComponent(this.query)}`;

      try {
        const response = await fetch(url);
        const data = await response.json();
        this.results = data.results;
        this.searched = true;
      } catch (error) {
        console.error("Fehler bei der API-Anfrage:", error);
      }
    },
    getPosterUrl(path) {
      return path
          ? `https://image.tmdb.org/t/p/w200${path}`
          : "https://via.placeholder.com/200x300?text=Kein+Bild";
    },
  },
};
</script>

<style scoped>
.search-container {
  padding: 20px;
}
input {
  padding: 10px;
  width: 300px;
}
button {
  padding: 10px;
  margin-left: 10px;
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
