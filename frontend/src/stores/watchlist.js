// frontend/src/stores/watchlist.js
import { defineStore } from 'pinia'

export const useWatchlistStore = defineStore('watchlist', {
    state: () => ({
        movies: []
    }),
    actions: {
        addMovie(movie) {
            if (!this.movies.find(m => m.id === movie.id)) {
                this.movies.push({ ...movie, status: 'geplant', rating: null })
            }
        },
        setStatus(id, status) {
            const m = this.movies.find(m => m.id === id)
            if (m) m.status = status
        },
        setRating(id, rating) {
            const m = this.movies.find(m => m.id === id)
            if (m) m.rating = rating
        },
        removeMovie(id) {
            this.movies = this.movies.filter(m => m.id !== id)
        }
    }
})
