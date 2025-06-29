import { createRouter, createWebHashHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import WatchlistView from '../views/WatchlistView.vue'
import MovieDetailView from '../views/MovieDetailView.vue'

const routes = [
    { path: '/', component: HomeView },
    { path: '/liste', component: WatchlistView },
    { path: '/film/:id', component: MovieDetailView, props: true }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router
