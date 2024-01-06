package io.goboolean.streams.etcd;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class SerdeTests {

    public record Group(Map<String, String> kvPair, Model model, Model data) {}
    public record TestCase(Map<String, String> kvPair, Group[] group) {}

    public static class Product implements Model {
        @Etcd("id")
        private String id;

        @Etcd("platform")
        private String platform;

        @Etcd("symbol")
        private String symbol;

        @Etcd("locale")
        private String locale;

        @Etcd("market")
        private String market;

        public Product() {
        }

        public Product(String id, String platform, String symbol, String locale, String market) {
            this.id = id;
            this.platform = platform;
            this.symbol = symbol;
            this.locale = locale;
            this.market = market;
        }

        @Override
        public String getName() {
            return "product";
        }
    }

    public static TestCase[] testCases = new TestCase[]{
            new TestCase(
                    Map.of(
                            "/product/test.goboolean.kor", "",
                            "/product/test.goboolean.kor/platform", "kis",
                            "/product/test.goboolean.kor/symbol", "goboolean",
                            "/product/test.goboolean.kor/locale", "kor",
                            "/product/test.goboolean.kor/market", "stock",
                            "/product/test.goboolean.eng", "",
                            "/product/test.goboolean.eng/platform", "polygon",
                            "/product/test.goboolean.eng/symbol", "gofalse",
                            "/product/test.goboolean.eng/locale", "eng",
                            "/product/test.goboolean.eng/market", "stock"
                    ),
                    new Group[]{
                            new Group(
                                    Map.of(
                                            "/product/test.goboolean.kor", "",
                                            "/product/test.goboolean.kor/platform", "kis",
                                            "/product/test.goboolean.kor/symbol", "goboolean",
                                            "/product/test.goboolean.kor/locale", "kor",
                                            "/product/test.goboolean.kor/market", "stock"
                                    ),
                                    new Product(),
                                    new Product("test.goboolean.kor", "kis", "goboolean", "kor", "stock")
                            ),
                            new Group(
                                    Map.of(
                                            "/product/test.goboolean.eng", "",
                                            "/product/test.goboolean.eng/platform", "polygon",
                                            "/product/test.goboolean.eng/symbol", "gofalse",
                                            "/product/test.goboolean.eng/locale", "eng",
                                            "/product/test.goboolean.eng/market", "stock"
                                    ),
                                    new Product(),
                                    new Product("test.goboolean.eng", "polygon", "gofalse", "eng", "stock")
                            )
                    }
            )
    };

    @Test
    public void testGroupByPrefix() throws IllegalArgumentException {
        for (TestCase testCase : testCases) {
            List<Map<String, String>> got = Serde.groupByPrefix(testCase.kvPair);

            assert got.size() == testCase.group().length;

            Arrays.stream(testCase.group()).forEach(group -> {
                assert got.contains(group.kvPair);
            });
        }
    }
}
