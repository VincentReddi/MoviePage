<template>
  <div :class="['movie-search', { 'dark-mode': darkMode }]">
    <!-- Dark Mode Toggle -->
    <button class="theme-toggle" @click="toggleDarkMode">
      {{ darkMode ? '☀️ Light Mode' : '🌙 Dark Mode' }}
    </button>

    <!-- Suchleiste -->
    <input
        type="text"
        v-model="query"
        placeholder="🔍 Nach Filmen suchen..."
        @input="searchMovies"
        class="search-input"
    />

    <!-- Genre-Dropdown -->
    <select v-model="selectedGenre" @change="searchMovies" class="genre-select">
      <option value="">🎬 Genre auswählen</option>
      <option v-for="genre in genres" :key="genre.id" :value="genre.id">
        {{ genre.name }}
      </option>
    </select>

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
        </div>
      </div>
    </div>

    <!-- Gespeicherte Filme -->
    <div v-if="savedMovies.length" class="saved-movies">
      <h2 class="saved-title">📌 Gespeicherte Filme</h2>
      <button @click="clearMovieList" class="clear-btn">🗑️ Liste leeren</button>
      <div class="results">
        <div
            v-for="movie in savedMovies"
            :key="movie.id"
            class="movie-card"
        >
          <img
              v-if="movie.posterPath"
              :src="'https://image.tmdb.org/t/p/w300' + movie.posterPath"
              :alt="movie.title"
              class="poster"
          />
          <div class="info">
            <h2>{{ movie.title }}</h2>
            <div class="controls">
              <label>Status:
                <select v-model="movie.status" @change="updateMovieStatus(movie)">
                  <option value="Geplant">Geplant</option>
                  <option value="Geschaut">Geschaut</option>
                </select>
              </label>
              <label>Bewertung:
                <input
                    type="number"
                    min="1"
                    max="10"
                    v-model.number="movie.rating"
                    @change="updateMovieRating(movie)"
                />
              </label>
              <button @click="deleteMovie(movie.id)" class="delete-single-btn">❌</button>
            </div>
          </div>
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
          <p><strong>Bewertung:</strong> ⭐ {{ selectedMovie.vote_average?.toFixed(1) || '–' }}</p>
          <p class="modal-overview">{{ selectedMovie.overview }}</p>
          <button @click="addToList(selectedMovie)" class="add-btn">Zur Liste hinzufügen</button>
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
      selectedGenre: '',
      genres: [],
      results: [],
      selectedMovie: null,
      savedMovies: [],
      darkMode: false
    };
  },
  async mounted() {
    await this.fetchSavedMovies();
    await this.fetchGenres();
    this.loadDarkModePreference();
  },
  methods: {
    toggleDarkMode() {
      this.darkMode = !this.darkMode;
      localStorage.setItem('darkMode', this.darkMode);
    },
    loadDarkModePreference() {
      const savedMode = localStorage.getItem('darkMode');
      this.darkMode = savedMode === 'true';
    },
    async fetchSavedMovies() {
      try {
        const res = await fetch('https://popcornpilot-backend-new.onrender.com/api/movies');
        this.savedMovies = await res.json();
      } catch (e) {
        console.error('Fehler beim Laden gespeicherter Filme:', e);
      }
    },
    async fetchGenres() {
      const apiKey = '3e1a60c002b082d8f881975fa6a5fe50';
      try {
        const res = await fetch(`https://api.themoviedb.org/3/genre/movie/list?api_key=${apiKey}&language=de`);
        const data = await res.json();
        this.genres = data.genres;
      } catch (e) {
        console.error('Fehler beim Laden der Genres:', e);
      }
    },
    async searchMovies() {
      const apiKey = '3e1a60c002b082d8f881975fa6a5fe50';

      let url = '';
      if (this.query.length >= 2) {
        url = `https://api.themoviedb.org/3/search/movie?query=${encodeURIComponent(this.query)}&api_key=${apiKey}&language=de`;
      } else if (this.selectedGenre) {
        url = `https://api.themoviedb.org/3/discover/movie?api_key=${apiKey}&with_genres=${this.selectedGenre}&language=de`;
      } else {
        this.results = [];
        return;
      }

      try {
        const res = await fetch(url);
        const data = await res.json();
        this.results = data.results;
      } catch (e) {
        console.error('Fehler bei TMDb-Suche:', e);
      }
    },
    selectMovie(movie) {
      this.selectedMovie = movie;
    },
    async addToList(movie) {
      if (this.savedMovies.some(m => m.title === movie.title)) {
        alert('Dieser Film ist bereits gespeichert.');
        return;
      }

      const payload = {
        title: movie.title,
        posterPath: movie.poster_path,
        status: 'Geplant',
        rating: 0
      };
      try {
        const res = await fetch('https://popcornpilot-backend-new.onrender.com/api/movies', {
          method: 'POST',
          headers: {'Content-Type': 'application/json'},
          body: JSON.stringify(payload)
        });
        if (res.ok) {
          alert('Film gespeichert!');
          await this.fetchSavedMovies();
          this.selectedMovie = null;
        } else {
          alert('Fehler beim Speichern.');
        }
      } catch (e) {
        alert('Verbindungsfehler.');
        console.error(e);
      }
    },
    async clearMovieList() {
      if (!confirm("Willst du wirklich alle Filme löschen?")) return;
      try {
        await fetch('https://popcornpilot-backend-new.onrender.com/api/movies', {
          method: 'DELETE'
        });
        this.savedMovies = [];
      } catch (e) {
        alert("Fehler beim Leeren der Liste.");
        console.error(e);
      }
    },
    async deleteMovie(id) {
      try {
        await fetch(`https://popcornpilot-backend-new.onrender.com/api/movies/${id}`, {
          method: 'DELETE'
        });
        this.savedMovies = this.savedMovies.filter(movie => movie.id !== id);
      } catch (e) {
        console.error("Fehler beim Löschen des Films", e);
      }
    },
    async updateMovieStatus(movie) {
      try {
        await fetch(`https://popcornpilot-backend-new.onrender.com/api/movies/${movie.id}/status`, {
          method: 'PUT',
          headers: {'Content-Type': 'application/json'},
          body: JSON.stringify({ status: movie.status })
        });
      } catch (e) {
        alert('Fehler beim Aktualisieren des Status');
        console.error(e);
      }
    },
    async updateMovieRating(movie) {
      try {
        await fetch(`https://popcornpilot-backend-new.onrender.com/api/movies/${movie.id}/rating`, {
          method: 'PUT',
          headers: {'Content-Type': 'application/json'},
          body: JSON.stringify({ rating: movie.rating })
        });
      } catch (e) {
        alert('Fehler beim Aktualisieren der Bewertung');
        console.error(e);
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
  color: #111;
  background-color: #f4f4f4;
}

.dark-mode {
  background-color: #121212;
  color: #ffffff;
}

.search-input,
.genre-select {
  width: 100%;
  margin-bottom: 1rem;
  padding: 0.75rem;
  border-radius: 8px;
  border: none;
  background: #eaeaea;
  color: #111;
}

.dark-mode .search-input,
.dark-mode .genre-select {
  background: #222;
  color: #fff;
}

.results {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
}

.movie-card {
  background: #ffffff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.6);
  width: 240px;
}

.dark-mode .movie-card {
  background: #1a1a1a;
}

.poster {
  width: 100%;
  height: 360px;
  object-fit: cover;
}

.info {
  padding: 1rem;
}

.controls {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.delete-single-btn {
  background: #ff4d4d;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 0.3rem 0.6rem;
  cursor: pointer;
  font-weight: bold;
  width: fit-content;
}

.delete-single-btn:hover {
  background: #e60000;
}

.clear-btn {
  margin-bottom: 1rem;
  background: #b30000;
  color: white;
  border: none;
  padding: 0.6rem 1rem;
  border-radius: 10px;
  cursor: pointer;
}

.clear-btn:hover {
  background: #990000;
}

.modal {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.9);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.modal-content {
  background: #fff;
  padding: 2rem;
  border-radius: 12px;
  color: #000;
  max-width: 800px;
  display: flex;
  gap: 2rem;
}

.dark-mode .modal-content {
  background: #222;
  color: white;
}

.modal-poster {
  width: 300px;
  object-fit: cover;
  border-radius: 8px;
}

.add-btn,
.close-btn {
  margin-top: 1rem;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: bold;
}

.add-btn {
  background: #28a745;
  color: white;
}

.close-btn {
  background: #555;
  color: white;
}

.theme-toggle {
  position: fixed;
  top: 1rem;
  left: 1rem;
  z-index: 1000;
  padding: 0.5rem 1rem;
  background: #444;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.theme-toggle:hover {
  background: #666;
}
</style>
