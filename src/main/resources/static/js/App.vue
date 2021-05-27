<template>
    <div>
        <div class="container">
            <products ref="products" @added="reloadCart" @error="addError"></products>
            <cart ref="cart" @removed="reloadProductsAndCart" @checkout="checkout" @error="addError"></cart>
        </div>

        <div class="error-holder">
            <span class="error" v-for="({ id, error }) in displayErrors" :key="id">
                {{ error }}
                <button @click="closeError(id)">&times;</button>
            </span>
        </div>
    </div>
</template>

<script>
    const next = (() => {
        let x = 0
        return () => x++
    })()

    module.exports = {
        name: 'App',

        components: {
            Cart: 'url:/js/components/Cart.vue',
            Products: 'url:/js/components/Products.vue',
        },

        data() {
            return {
                errors: []
            }
        },

        computed: {
            displayErrors() {
                return this.errors
                    .map(({id, message}) => ({id, error: message}))
                    .reverse()
            }
        },

        methods: {
            async addError(response) {
                const id = next()

                this.errors.push({
                    id,
                    timer: setTimeout(() => this.closeError(id), 5_000),
                    message: await response.json()
                        .then(x => x.error)
                })
            },

            closeError(id) {
                this.errors = this.errors.filter(e => {
                    if (e.id === id) {
                        clearTimeout(e.timer)
                        return false
                    }

                    return true
                })
            },

            reloadCart() {
                console.log('reloadCart', this.$refs)
                this.$refs.cart.load()
            },

            reloadProductsAndCart() {
                this.$refs.cart.load()
                this.$refs.products.load()
            },

            async checkout() {
                const response = await fetch('/cart/checkout', {
                    method: 'POST',
                })

                if (response.ok) {
                    this.reloadProductsAndCart()
                } else {
                    this.addError(response)
                }
            }
        }
    }
</script>

<style scoped>
    .container {
        display: flex;
        justify-content: space-between;
    }

    .container > div {
        width: calc(50% - 5px);
    }

    .error-holder {
        display: flex;
        flex-direction: column;
    }

    .error {
        margin-top: 5px;
        padding: 10px 15px;
        background-color: #c76565;
        color: white;
    }

    .error > button {
        float: right;
    }
</style>