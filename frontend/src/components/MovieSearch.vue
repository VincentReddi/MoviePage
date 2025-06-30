<template>
  <div class="movie-search">
    <!-- Suchleiste + Genre Dropdown + Zufallsfilm Button -->
    <div class="top-controls">
      <input
          type="text"
          v-model="query"
          placeholder="🔍 Nach Filmen suchen..."
          @input="searchMovies"
          class="search-input"
      />

      <select v-model="selectedGenre" @change="searchMovies" class="genre-select">
        <option value="">🎞️ Genre auswählen</option>
        <option v-for="genre in genres" :key="genre.id" :value="genre.id">
          {{ genre.name }}
        </option>
      </select>

      <button @click="showRandomMovie" class="random-btn">🎲 Zufallsfilm</button>
    </div>

    <!-- Suchergebnisse -->
    <div v-if="results.length" class="results">
      <div
          v-for="movie in results"
          :key="movie.id"
          class="movie-card"
          @click="selectMovie(movie)"
      >
        <img
            v-if="movie.poster_path"
            :src="'https://image.tmdb.org/t/p/w300' + movie.poster_path"
            :alt="movie.title"
            class="poster"
        />
        <div class="info">
          <h2>{{ movie.title }}</h2>
          <p class="meta">
            {{ movie.release_date?.slice(0, 4) || 'N/A' }} · ⭐
            {{ movie.vote_average ? movie.vote_average.toFixed(1) : '–' }}
          </p>
          <p class="overview">{{ movie.overview?.slice(0, 150) || 'Keine Beschreibung.' }}...</p>
        </div>
      </div>
    </div>

    <!-- Modal -->
    <div v-if="selectedMovie" class="modal" @click.self="selectedMovie = null">
      <div class="modal-content">
        <img
            v-if="selectedMovie.poster_path"
            :src="'https://image.tmdb.org/t/p/w500' + selectedMovie.poster_path"
            :alt="selectedMovie.title"
            class="modal-poster"
        />
        <div class="modal-info">
          <h2>{{ selectedMovie.title }}</h2>
          <p><strong>Veröffentlichung:</strong> {{ selectedMovie.release_date }}</p>
          <p><strong>Bewertung:</strong> ⭐ {{ selectedMovie.vote_average ? selectedMovie.vote_average.toFixed(1) : '–' }}</p>
          <p class="modal-overview">{{ selectedMovie.overview }}</p>
          <button @click="selectedMovie = null" class="close-btn">Schließen</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MovieSearch',
  data() {
    return {
      query: '',
      results: [],
      genres: [],
      selectedGenre: '',
      selectedMovie: null
    };
  },
  async mounted() {
    await this.fetchGenres();
  },
  methods: {
    async fetchGenres() {
      const apiKey = '3e1a60c002b082d8f881975fa6a5fe50';
      try {
        const res = await fetch(
            `https://api.themoviedb.org/3/genre/movie/list?api_key=${apiKey}&language=de`
        );
        const data = await res.json();
        this.genres = data.genres;
      } catch (error) {
        console.error('Fehler beim Laden der Genres:', error);
      }
    },
    async searchMovies() {
      if (this.query.length < 2 && !this.selectedGenre) {
        this.results = [];
        return;
      }

      const apiKey = '3e1a60c002b082d8f881975fa6a5fe50';
      let url = `https://api.themoviedb.org/3/search/movie?api_key=${apiKey}&language=de&query=${encodeURIComponent(this.query)}`;

      if (this.selectedGenre) {
        url += `&with_genres=${this.selectedGenre}`;
      }

      try {
        const res = await fetch(url);
        const data = await res.json();
        this.results = data.results || [];
      } catch (error) {
        console.error('Fehler bei der Filmsuche:', error);
      }
    },
    selectMovie(movie) {
      this.selectedMovie = movie;
    },
    showRandomMovie() {
      if (this.results.length > 0) {
        const index = Math.floor(Math.random() * this.results.length);
        this.selectedMovie = this.results[index];
      } else {
        alert("Keine Filme vorhanden. Bitte zuerst suchen.");
      }
    }
  }
};
</script>

<style scoped>
.movie-search {
  padding: 2rem;
  max-width: 1200px;
  margin: auto;
  color: #f0f0f0;
}

.top-controls {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  margin-bottom: 2rem;
  align-items: center;
}

.search-input,
.genre-select {
  padding: 0.75rem 1rem;
  font-size: 1rem;
  border: none;
  border-radius: 1rem;
  background-color: #1e1e1e;
  color: #fff;
  box-shadow: 0 2px 6px rgba(0,0,0,0.6);
}

.random-btn {
  background-color: #ffc107;
  padding: 0.75rem 1.2rem;
  border-radius: 1rem;
  border: none;
  cursor: pointer;
  font-weight: bold;
  box-shadow: 0 2px 6px rgba(0,0,0,0.4);
}

.results {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
}

.movie-card {
  background: #1a1a1a;
  border-radius: 1.25rem;
  overflow: hidden;
  box-shadow: 0 6px 18px rgba(0,0,0,0.6);
  display: flex;
  flex-direction: column;
  transition: transform 0.2s ease;
  cursor: pointer;
}

.movie-card:hover {
  transform: translateY(-5px);
}

.poster {
  width: 100%;
  height: auto;
}

.info {
  padding: 1rem;
}

.info h2 {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: #fff;
}

.meta {
  font-size: 0.9rem;
  color: #999;
  margin-bottom: 0.5rem;
}

.overview {
  font-size: 0.9rem;
  color: #ccc;
}

.modal {
  position: fixed;
  inset: 0;
  background-color: rgba(0,0,0,0.85);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 99;
}

.modal-content {
  background: #222;
  border-radius: 1rem;
  max-width: 800px;
  padding: 2rem;
  color: white;
  display: flex;
  gap: 2rem;
  flex-wrap: wrap;
}

.modal-poster {
  max-width: 300px;
  border-radius: 0.5rem;
}

.modal-info {
  flex: 1;
}

.close-btn {
  margin-top: 1rem;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 1rem;
  cursor: pointer;
  font-weight: bold;
  background-color: #555;
  color: white;
}
.close-btn:hover {
  background-color: #333;
}
</style>
