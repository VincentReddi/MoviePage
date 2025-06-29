<template>
  <div class="search-container">
    <h2>🎬 Filme suchen oder nach Genre filtern</h2>

    <!-- Eingabe für Titelsuche -->
    <input v-model="query" @keyup.enter="searchMovies" placeholder="Film eingeben..." />
    <button @click="searchMovies">Suchen</button>

    <!-- Dropdown für Genres -->
    <select v-model="selectedGenre" @change="filterByGenre">
      <option value="">Genre auswählen</option>
      <option v-for="genre in genres" :key="genre.id" :value="genre.id">
        {{ genre.name }}
      </option>
    </select>

    <!-- Gefundene Filme -->
    <div v-if="results.length > 0" class="results">
      <div v-for="movie in results" :key="movie.id" class="movie-card">
        <img :src="getPosterUrl(movie.poster_path)" alt="Poster" />
        <div>
          <h3>{{ movie.title }} ({{ movie.release_date?.slice(0, 4) || '–' }})</h3>
          <p>{{ movie.overview || 'Keine Beschreibung verfügbar.' }}</p>
          <button @click="addToWatchlist(movie)">Zur Liste hinzufügen</button>
        </div>
      </div>
    </div>

    <p v-else-if="searched">Keine Ergebnisse gefunden.</p>

    <!-- Watchlist -->
    <div v-if="watchlist.length > 0" class="watchlist">
      <h3>🎞️ Meine Watchlist</h3>
      <ul>
        <li v-for="movie in watchlist" :key="movie.id">
          {{ movie.title }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      query: '',
      selectedGenre: '',
      results: [],
      genres: [],
      watchlist: [],
      searched: false
    };
  },
  async mounted() {
    await this.loadGenres();
  },
  methods: {
    async loadGenres() {
      const apiKey = '3e1a60c002b082d8f881975fa6a5fe50';
      const url = `https://api.themoviedb.org/3/genre/movie/list?api_key=${apiKey}&language=de`;

      try {
        const res = await fetch(url);
        const data = await res.json();
        this.genres = data.genres;
      } catch (err) {
        console.error('Fehler beim Laden der Genres:', err);
      }
    },
    async searchMovies() {
      if (!this.query.trim()) return;

      const apiKey = '3e1a60c002b082d8f881975fa6a5fe50';
      const url = `https://api.themoviedb.org/3/search/movie?api_key=${apiKey}&language=de&query=${encodeURIComponent(this.query)}`;

      try {
        const response = await fetch(url);
        const data = await response.json();
        this.results = data.results;
        this.searched = true;
      } catch (error) {
        console.error('Fehler bei der API-Anfrage:', error);
      }
    },
    async filterByGenre() {
      if (!this.selectedGenre) return;

      const apiKey = '3e1a60c002b082d8f881975fa6a5fe50';
      const url = `https://api.themoviedb.org/3/discover/movie?api_key=${apiKey}&language=de&with_genres=${this.selectedGenre}`;

      try {
        const res = await fetch(url);
        const data = await res.json();
        this.results = data.results;
        this.searched = true;
      } catch (err) {
        console.error('Fehler beim Filtern:', err);
      }
    },
    getPosterUrl(path) {
      return path
          ? `https://image.tmdb.org/t/p/w200${path}`
          : 'https://via.placeholder.com/200x300?text=Kein+Bild';
    },
    addToWatchlist(movie) {
      if (!this.watchlist.find(m => m.id === movie.id)) {
        this.watchlist.push(movie);
      }
    }
  }
};
</script>

<style scoped>
.search-container {
  padding: 20px;
}
input, select {
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
.watchlist {
  margin-top: 40px;
  background: #f7f7f7;
  padding: 15px;
  border-radius: 10px;
}
</style>
