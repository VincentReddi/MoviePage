import { createRouter, createWebHistory } from 'vue-router'
import SearchMovies from '../components/SearchMovies.vue'

const routes = [
    {
        path: '/',
        name: 'Home',
        component: SearchMovies // Ensure the search bar is displayed on the homepage
    },
    {
        path: '/film/:id',
        name: 'FilmDetail',
        component: () => import('../components/MovieDetailView.vue')
    },
    {
        path: '/liste',
        name: 'Watchlist',
        component: () => import('../components/WatchlistView.vue')
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router