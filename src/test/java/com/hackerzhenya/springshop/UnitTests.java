package com.hackerzhenya.springshop;

import com.hackerzhenya.springshop.models.Product;
import com.hackerzhenya.springshop.services.CartService;
import com.hackerzhenya.springshop.services.ProductsService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.Assert;

@SpringBootTest
@AutoConfigureMockMvc
public class UnitTests {
    @Autowired
    private CartService cartService;

    @MockBean
    private ProductsService productsService;

    @Test
    void contextLoads() {
        Assert.notNull(cartService, "CartService must be initialized");
    }

    @Test
    void tryAddToCartMoreThenInStock() {
        Mockito.doReturn(new Product(1337, "Stub product", 228, 1))
               .when(productsService)
               .getItem(Mockito.anyLong());

        var exception = Assertions
                .assertThrows(RuntimeException.class, () -> cartService.addToCart(1337, 99999));

        Assertions.assertTrue(exception.getMessage().startsWith("Не достаточно товара на складе"));
    }

    @Test
    void checkout() {
        var stubs = Lists.list(
                new Product(1, "Stub product", 2, 4),
                new Product(8, "Stub product", 16, 32),
                new Product(64, "Stub product", 128, 256)
        );

        // region addToCart

        Mockito.doReturn(stubs.get(0))
               .when(productsService)
               .getItem(1);

        Mockito.doReturn(stubs.get(1))
               .when(productsService)
               .getItem(8);

        Mockito.doReturn(stubs.get(2))
               .when(productsService)
               .getItem(64);

        cartService.addToCart(1, 4);
        cartService.addToCart(8, 32);
        cartService.addToCart(64, 256);

        Mockito.verify(productsService, Mockito.times(3))
               .getItem(Mockito.anyLong());

        // endregion

        // region Checkout

        Mockito.doReturn(stubs)
               .when(productsService)
               .getItems();

        cartService.checkout();

        Mockito.verify(productsService, Mockito.times(1))
               .getItems();

        Mockito.verify(productsService, Mockito.times(3))
               .decreaseQuantity(Mockito.anyLong(), Mockito.anyLong());

        Mockito.verify(productsService, Mockito.times(1))
               .decreaseQuantity(1, 4);

        Mockito.verify(productsService, Mockito.times(1))
               .decreaseQuantity(8, 32);

        Mockito.verify(productsService, Mockito.times(1))
               .decreaseQuantity(64, 256);

        // endregion
    }
}
