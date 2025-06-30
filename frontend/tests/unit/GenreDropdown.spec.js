import { mount } from '@vue/test-utils'
import MovieSearch from '@/components/MovieSearch.vue'

// 🔁 Mock für fetch-Aufrufe inkl. Genreliste
global.fetch = jest.fn((url) => {
    if (url.includes('/genre')) {
        return Promise.resolve({
            json: () =>
                Promise.resolve({
                    genres: [
                        { id: 1, name: 'Action' },
                        { id: 2, name: 'Drama' },
                        { id: 3, name: 'Comedy' }
                    ]
                })
        })
    }

    return Promise.resolve({
        json: () => Promise.resolve({ results: [] })
    })
})

describe('GenreDropdown.vue', () => {
    it('lädt Genres und zeigt sie im Dropdown an', async () => {
        const wrapper = mount(MovieSearch)

        // Warten bis fetch durch ist
        await new Promise((resolve) => setTimeout(resolve, 0))

        const options = wrapper.findAll('select.genre-select option')

        // Erste Option ist "🎬 Genre auswählen", danach 3 Genres
        expect(options).toHaveLength(4)
        expect(options[1].text()).toBe('Action')
        expect(options[2].text()).toBe('Drama')
        expect(options[3].text()).toBe('Comedy')
    })
})
