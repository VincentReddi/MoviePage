import { mount } from '@vue/test-utils'
import MovieSearch from '@/components/MovieSearch.vue'

// ✅ globales fetch-Mock für alle API-Calls
global.fetch = jest.fn(() =>
    Promise.resolve({
        json: () => Promise.resolve({ results: [], genres: [] })
    })
)

describe('MovieSearch.vue', () => {
    it('renders the search input', () => {
        const wrapper = mount(MovieSearch)
        const input = wrapper.find('input[type="text"]')
        expect(input.exists()).toBe(true)
    })

    it('updates input value when typing', async () => {
        const wrapper = mount(MovieSearch)
        const input = wrapper.find('input[type="text"]')
        await input.setValue('Inception')
        expect(input.element.value).toBe('Inception')
    })
})
