// frontend/src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import SearchMovies from '../components/SearchMovies.vue'

const routes = [
    {
        path: '/',
        name: 'Home',
        component: SearchMovies, // Stellt sicher, dass die Suchfunktion auf der Startseite angezeigt wird
    },
    {
        path: '/film/:id',
        name: 'FilmDetail',
        component: () => import('../components/MovieDetailView.vue'), // Beispiel für eine Detailseite
    },
    {
        path: '/liste',
        name: 'Watchlist',
        component: () => import('../components/WatchlistView.vue'), // Beispiel für die Watchlist-Seite
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router