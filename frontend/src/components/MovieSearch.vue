<template>
  <div class="movie-search">
    <input
        type="text"
        v-model="query"
        placeholder="🔍 Nach Filmen suchen..."
        @input="searchMovies"
        class="search-input"
    />

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
          <p class="overview">{{ movie.overview.slice(0, 150) }}...</p>
        </div>
      </div>
    </div>

    <!-- Gespeicherte Filme -->
    <div v-if="savedMovies.length" class="saved-movies">
      <h2 class="saved-title">📌 Meine Liste</h2>
      <button @click="clearMovieList" class="clear-btn">🗑️ Liste leeren</button>
      <div class="results">
        <div
            v-for="movie in savedMovies"
            :key="movie.tmdbId"
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
            <p class="meta">Status: {{ movie.status }}</p>
            <p class="overview">{{ movie.description.slice(0, 150) }}...</p>
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
          <p><strong>Bewertung:</strong> ⭐ {{ selectedMovie.vote_average ? selectedMovie.vote_average.toFixed(1) : '–' }}</p>
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
      results: [],
      selectedMovie: null,
      savedMovies: []
    }
  },
  async mounted() {
    await this.fetchSavedMovies()
  },
  methods: {
    async fetchSavedMovies() {
      try {
        const res = await fetch('https://popcornpilot-backend-new.onrender.com/api/movies')
        const data = await res.json()
        this.savedMovies = data
      } catch (e) {
        console.error('Fehler beim Laden gespeicherter Filme:', e)
      }
    },
    async searchMovies() {
      if (this.query.length < 2) {
        this.results = []
        return
      }

      const apiKey = '3e1a60c002b082d8f881975fa6a5fe50'
      const url = `https://api.themoviedb.org/3/search/movie?query=${encodeURIComponent(this.query)}&api_key=${apiKey}&language=de`

      try {
        const response = await fetch(url)
        const data = await response.json()
        this.results = data.results
      } catch (error) {
        console.error('Fehler bei der TMDb-Suche:', error)
        this.results = []
      }
    },
    selectMovie(movie) {
      this.selectedMovie = movie
    },
    async addToList(movie) {
      const payload = {
        title: movie.title,
        posterPath: movie.poster_path  // TMDb liefert `poster_path`, dein Backend erwartet `posterPath`
      };

      try {
        const res = await fetch('https://popcornpilot-backend-new.onrender.com/api/movies', {
          method: 'POST',
          headers: {'Content-Type': 'application/json'},
          body: JSON.stringify(payload)
        })

        if (res.ok) {
          alert('Film wurde gespeichert!')
          await this.fetchSavedMovies()
          this.selectedMovie = null
        } else {
          alert('Fehler beim Speichern. Prüfe die Felder.')
        }
      } catch (e) {
        console.error('Fehler beim Speichern:', e)
        alert('Verbindungsfehler oder ungültige Daten.')
      }
    },
    async clearMovieList() {
      if (!confirm("Willst du wirklich alle Filme löschen?")) return;

      try {
        await fetch('https://popcornpilot-backend-new.onrender.com/api/movies', {
          method: 'DELETE'
        });
        this.savedMovies = [];
        alert("Liste wurde geleert.");
      } catch (e) {
        alert("Fehler beim Leeren der Liste.");
        console.error(e);
      }
    }
  }
}
</script>

<style scoped>
.movie-search {
  padding: 2rem;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  color: #f0f0f0;
  box-sizing: border-box;
}

.search-input {
  width: 100%;
  padding: 0.75rem 1rem;
  font-size: 1rem;
  border: none;
  border-radius: 1rem;
  background-color: #1e1e1e;
  color: #fff;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.6);
}

.search-input::placeholder {
  color: #aaa;
}

.results {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-top: 2rem;
}

.movie-card {
  background: #1a1a1a;
  border-radius: 1.25rem;
  overflow: hidden;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.6);
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
  object-fit: cover;
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

.saved-title {
  margin-top: 3rem;
  font-size: 1.5rem;
  font-weight: 600;
  color: #fff;
}

.clear-btn {
  margin-bottom: 1rem;
  padding: 0.5rem 1rem;
  background-color: #ff3b30;
  color: white;
  border: none;
  border-radius: 1rem;
  cursor: pointer;
  font-weight: bold;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.5);
}

.clear-btn:hover {
  background-color: #ff1f1a;
}

/* Modal Fix */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.modal-content {
  background: #2c2c2e;
  padding: 2rem;
  border-radius: 1.25rem;
  max-width: 600px;
  width: 90%;
  color: white;
  display: flex;
  gap: 1.5rem;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);
}

.modal-poster {
  width: 200px;
  border-radius: 1rem;
}

.modal-info {
  flex: 1;
}

.modal-overview {
  margin-top: 1rem;
  color: #ccc;
  line-height: 1.4;
}

.add-btn, .close-btn {
  margin-top: 1rem;
  padding: 0.6rem 1rem;
  border: none;
  border-radius: 1rem;
  font-weight: bold;
  cursor: pointer;
}

.add-btn {
  background-color: #34c759;
  color: white;
  margin-right: 1rem;
}

.add-btn:hover {
  background-color: #28a745;
}

.close-btn {
  background-color: #ff3b30;
  color: white;
}

.close-btn:hover {
  background-color: #e0281f;
}
</style>
