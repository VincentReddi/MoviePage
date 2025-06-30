<template>
  <div class="movie-search">
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

    <!-- Zufallsfilm-Button -->
    <button @click="getRandomMovie" class="clear-btn">🎲 Zufallsfilm</button>

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
          <p class="overview">{{ movie.overview?.slice(0, 150) }}...</p>
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
            <button @click="deleteMovie(movie.id)" class="delete-btn">❌</button>
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
      savedMovies: []
    }
  },
  async mounted() {
    await this.fetchSavedMovies()
    await this.fetchGenres()
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
    async fetchGenres() {
      const apiKey = '3e1a60c002b082d8f881975fa6a5fe50'
      try {
        const response = await fetch(`https://api.themoviedb.org/3/genre/movie/list?api_key=${apiKey}&language=de`)
        const data = await response.json()
        this.genres = data.genres
      } catch (e) {
        console.error('Fehler beim Laden der Genres:', e)
      }
    },
    async searchMovies() {
      if (this.query.length < 2) {
        this.results = []
        return
      }

      const apiKey = '3e1a60c002b082d8f881975fa6a5fe50'
      let url = `https://api.themoviedb.org/3/search/movie?query=${encodeURIComponent(this.query)}&api_key=${apiKey}&language=de`

      try {
        const response = await fetch(url)
        let data = await response.json()

        if (this.selectedGenre) {
          data.results = data.results.filter(movie => movie.genre_ids?.includes(Number(this.selectedGenre)))
        }

        this.results = data.results
      } catch (error) {
        console.error('Fehler bei der TMDb-Suche:', error)
        this.results = []
      }
    },
    async getRandomMovie() {
      const apiKey = '3e1a60c002b082d8f881975fa6a5fe50'
      const page = Math.floor(Math.random() * 500) + 1
      try {
        const response = await fetch(`https://api.themoviedb.org/3/discover/movie?api_key=${apiKey}&language=de&page=${page}`)
        const data = await response.json()
        const random = data.results[Math.floor(Math.random() * data.results.length)]
        this.results = [random]
      } catch (e) {
        alert('Zufallsfilm konnte nicht geladen werden.')
        console.error(e)
      }
    },
    selectMovie(movie) {
      this.selectedMovie = movie
    },
    async addToList(movie) {
      const payload = {
        title: movie.title,
        posterPath: movie.poster_path,
        releaseDate: movie.release_date,
        status: 'Geplant',
        rating: 0
      }

      try {
        const res = await fetch('https://popcornpilot-backend-new.onrender.com/api/movies', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload)
        })

        if (res.ok) {
          alert('Film wurde gespeichert!')
          await this.fetchSavedMovies()
          this.selectedMovie = null
        } else {
          alert('Fehler beim Speichern.')
        }
      } catch (e) {
        alert('Verbindungsfehler oder ungültige Daten.')
        console.error(e)
      }
    },
    async clearMovieList() {
      if (!confirm("Willst du wirklich alle Filme löschen?")) return;

      try {
        await fetch('https://popcornpilot-backend-new.onrender.com/api/movies', {
          method: 'DELETE'
        });
        this.savedMovies = []
        alert("Liste wurde geleert.")
      } catch (e) {
        alert("Fehler beim Leeren der Liste.")
        console.error(e)
      }
    },
    async deleteMovie(id) {
      try {
        await fetch(`https://popcornpilot-backend-new.onrender.com/api/movies/${id}`, {
          method: 'DELETE'
        });
        this.savedMovies = this.savedMovies.filter(movie => movie.id !== id)
      } catch (e) {
        alert('Fehler beim Löschen des Films.')
        console.error(e)
      }
    },
    async updateMovieStatus(movie) {
      try {
        await fetch(`https://popcornpilot-backend-new.onrender.com/api/movies/${movie.id}/status`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(movie.status)
        })
      } catch (e) {
        alert('Fehler beim Aktualisieren des Status')
        console.error(e)
      }
    },
    async updateMovieRating(movie) {
      try {
        await fetch(`https://popcornpilot-backend-new.onrender.com/api/movies/${movie.id}/rating`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(movie.rating)
        })
      } catch (e) {
        alert('Fehler beim Aktualisieren der Bewertung')
        console.error(e)
      }
    }
  }
}
</script>

<style scoped>
/* ... dein vorhandenes CSS bleibt gleich ... */
.delete-btn {
  margin-top: 0.5rem;
  padding: 0.3rem 0.8rem;
  background-color: #ff4d4d;
  border: none;
  border-radius: 0.75rem;
  color: white;
  font-weight: bold;
  cursor: pointer;
}
.delete-btn:hover {
  background-color: #cc0000;
}
</style>
