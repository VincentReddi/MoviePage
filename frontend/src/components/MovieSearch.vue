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
            <p class="overview">{{ movie.description?.slice(0, 150) || 'Keine Beschreibung vorhanden...' }}</p>
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

        // Wenn Genre gefiltert wird
        if (this.selectedGenre) {
          data.results = data.results.filter(movie => movie.genre_ids?.includes(Number(this.selectedGenre)))
        }

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
        posterPath: movie.poster_path,
        releaseDate: movie.release_date,
        description: movie.overview,
        status: 'Geplant',
        genre: 'Unbekannt',
        platform: 'Unbekannt',
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
.movie-search {
  padding: 2rem;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  color: #f0f0f0;
  box-sizing: border-box;
}
.search-input, .genre-select {
  width: 100%;
  margin-bottom: 1rem;
  padding: 0.75rem 1rem;
  font-size: 1rem;
  border: none;
  border-radius: 1rem;
  background-color: #1e1e1e;
  color: #fff;
  box-shadow: 0 2px 6px rgba(0,0,0,0.6);
}
.genre-select {
  margin-top: 0.5rem;
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
.add-btn,
.close-btn {
  margin-top: 1rem;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 1rem;
  cursor: pointer;
  font-weight: bold;
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
  background-color: #555;
  color: white;
}
.close-btn:hover {
  background-color: #333;
}
</style>
