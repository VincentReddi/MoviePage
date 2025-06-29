import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../components/HomeView.vue'

const routes = [
    {
        path: '/',
        name: 'Home',
        component: HomeView // Default route points to HomeView.vue
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