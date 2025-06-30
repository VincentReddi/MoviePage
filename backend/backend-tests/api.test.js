const request = require('supertest');

const BASE_URL = 'https://popcornpilot-backend-new.onrender.com';

describe('PopcornPilot Backend API', () => {
    let movieId;

    // Render "aufwecken", falls es schläft
    beforeAll(async () => {
        await request(BASE_URL).get('/api/movies');
    });

    it('should get the movie list', async () => {
        const response = await request(BASE_URL).get('/api/movies');
        expect(response.statusCode).toBe(200);
        expect(Array.isArray(response.body)).toBe(true);
    }, 15000); // Timeout auf 15s

    it('should add a new movie', async () => {
        const newMovie = {
            tmdbId: 123456,
            title: 'Test Movie',
            rating: 8,
            status: 'Geplant'
        };

        const response = await request(BASE_URL)
            .post('/api/movies')
            .send(newMovie);

        console.log("POST Response:", response.body); // <-- debug

        expect(response.statusCode).toBe(200);
        expect(response.body.title).toBe('Test Movie');

        movieId = response.body.id;
    }, 15000);
});
