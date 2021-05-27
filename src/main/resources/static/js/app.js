const { Vue, httpVueLoader } = window

Vue.use(httpVueLoader)

const App = new Vue({
    el: '#app',

    components: {
        App: 'url:/js/App.vue'
    },
})

export default App;