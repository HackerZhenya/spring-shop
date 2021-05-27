<template>
    <div>
        <h3>Товары</h3>
        <table v-if="products.length">
            <thead>
            <tr>
                <th>#</th>
                <th>Имя</th>
                <th>Цена</th>
                <th>Количество на складе</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="product in products" :key="product.id">
                <td>{{ product.id }}</td>
                <td>{{ product.title }}</td>
                <td>{{ Number(product.price).toFixed(2) }} ₽</td>
                <td>{{ product.quantity }} шт.</td>
                <td>
                    <input type="number" v-model.number="quantities[product.id]" min="1" :max="product.quantity">
                    <button @click="addToCart(product, quantities[product.id])">+</button>
                </td>
            </tr>
            </tbody>
        </table>
        <p v-else>Товары закончились ¯\_(ツ)_/¯</p>
    </div>
</template>

<script>
    module.exports = {
        name: 'Products',

        data() {
            return {
                quantities: {},
                products: [],
            }
        },

        mounted() {
            this.load()
        },

        methods: {
            async load() {
                this.products = await fetch('/products/')
                    .then(x => x.json())

                if (Object.keys(this.quantities).length !== this.products.length) {
                    this.quantities = Object.assign(
                        Object.fromEntries(this.products.map(x => [x.id, 1])),
                        this.quantities
                    )
                }
            },

            async addToCart({id}, quantity) {
                const response = await fetch('/cart/', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json;charset=utf-8'
                    },
                    body: JSON.stringify({
                        productId: id,
                        quantity
                    })
                })

                if (response.ok) {
                    this.$emit('added')
                } else {
                    this.$emit('error', response)
                }
            }
        }
    }
</script>

<style scoped>
    table {
        width: 100%;
    }
</style>