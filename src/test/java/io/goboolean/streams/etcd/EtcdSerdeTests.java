package io.goboolean.streams.etcd;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class EtcdSerdeTests {

    private record Group(Map<String, String> kvPair, Product data) {}
    private record TestCase(Map<String, String> kvPair, Group[] group) {}

    private static TestCase[] testCases = new TestCase[]{
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
                                    new Product("test.goboolean.eng", "polygon", "gofalse", "eng", "stock")
                            )
                    }
            )
    };

    private EtcdSerde<Product> serde = new EtcdSerde<>(Product.class);

    @Test
    public void testGroupByPrefix() throws IllegalArgumentException {
        for (TestCase testCase : testCases) {
            List<Map<String, String>> got = serde.groupByPrefix(testCase.kvPair);
            assert got.size() == testCase.group().length;

            Arrays.stream(testCase.group()).forEach(group -> {
                assert got.contains(group.kvPair);
            });
        }
    }

    @Test
    public void testSerialize() throws IllegalAccessException {
        for (TestCase testCase : testCases) {
            for (Group group : testCase.group()) {
                Map<String, String> got = serde.serialize(group.data);

                assert got.equals(group.kvPair);
            }
        }
    }

    @Test
    public void testDeserialize() throws IllegalAccessException {
        for (TestCase testCase : testCases) {
            for (Group group : testCase.group()) {
                Map<String, String> kvPair = group.kvPair;
                Product result = serde.deserialize(kvPair);

                assert result.equals(group.data());
            }
        }
    }

    @Test
    public void testSerializeDeserialize() throws IllegalAccessException {
        for (TestCase testCase : testCases) {
            for (Group group : testCase.group()) {
                Map<String, String> kvPair = serde.serialize(group.data);
                assert kvPair.equals(group.kvPair);

                Product got = serde.deserialize(kvPair);
                assert got.equals(group.data);
            }
        }
    }

    @Test
    public void testSerializeList() throws IllegalAccessException {
        for (TestCase testCase : testCases) {
            List<Product> models = Arrays.asList(testCase.group()[0].data(), testCase.group()[1].data());
            Map<String, String> got = serde.serializeList(models);

            assert got.equals(testCase.kvPair);
        }
    }
}
