package io.goboolean.streams.etcd;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class EtcdClientTests {

    @Autowired
    private EtcdClient etcdClient;

    private Product[] products = new Product[] {
            new Product("test.goboolean.kor", "kis", "goboolean", "kor", "stock"),
            new Product("test.goboolean.eng", "polygon", "gofalse", "eng", "crypto"),
            new Product("test.goboolean.jpn", "polygon", "gonil", "jpn", "option"),
            new Product("test.goboolean.chi", "kis", "gotrue", "chi", "stock")
    };

    @AfterEach
    public void cleanup() {
        etcdClient.deleteAllProducts();
    }

    @Test
    public void insertAndGetProducts() {
        for (Product product : products) {
            etcdClient.insertProduct(product);
        }

        List<Product> products = etcdClient.getAllProducts();

        Assertions.assertEquals(4, products.size());
        for (Product product : products) {
            Assertions.assertTrue(Arrays.asList(this.products).contains(product));
        }
    }
}
