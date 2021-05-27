<template>
    <div>
        <h3>Корзина</h3>
        <table v-if="cart.length">
            <thead>
            <tr>
                <th>#</th>
                <th>Имя</th>
                <th>Количество в корзине</th>
                <th>Подитог</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="item in cart" :key="item.id">
                <td>{{ item.productId }}</td>
                <td>{{ products[item.productId]?.title }}</td>
                <td>{{ item.quantity }} шт.</td>
                <td>{{ Number(products[item.productId]?.price * item.quantity).toFixed(2) }} ₽</td>
                <td>
                    <input type="number" v-model.number="quantities[item.productId]" min="1" :max="item.quantity">
                    <button @click="removeFromCart(item, quantities[item.productId] || 1)">–</button>
                </td>
            </tr>
            </tbody>
        </table>

        <p v-else>Корзина пуста</p>

        <h4>Итог: {{ Number(totalPrice).toFixed(2) }} ₽</h4>
        <button @click="checkout">Купить</button>
    </div>
</template>

<script>
    module.exports = {
        name: 'Cart',

        data() {
            return {
                quantities: {},
                products: {},
                cart: [],
            }
        },

        computed: {
            totalPrice () {
                return this.cart.reduce((acc, { productId, quantity }) =>
                    acc + this.products[productId].price * quantity, 0)
            }
        },

        mounted() {
            this.load()
        },

        methods: {
            async load() {
                this.products = await fetch('/products/')
                    .then(x => x.json())
                    .then(x => Object.fromEntries(x.map(p => [p.id, p])))

                this.cart = await fetch('/cart/')
                        .then(x => x.json())

                if (Object.keys(this.quantities).length !== this.cart.length) {
                    this.quantities = Object.assign(
                        Object.fromEntries(this.cart.map(x => [x.productId, x.quantity])),
                        this.quantities
                    )
                }
            },

            async removeFromCart({productId}, quantity) {
                const response = await fetch('/cart/', {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json;charset=utf-8'
                    },
                    body: JSON.stringify({
                        productId,
                        quantity
                    })
                })

                if (response.ok) {
                    this.$emit('removed')
                } else {
                    this.$emit('error', response)
                }
            },

            checkout () {
                this.$emit('checkout')
            }
        }
    }
</script>

<style scoped>
    table {
        width: 100%;
    }
</style>