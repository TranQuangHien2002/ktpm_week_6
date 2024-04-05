package fit.ktpm_tuan_06_20036311_tranquanghien;

import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import fit.ktpm_tuan_06_20036311_tranquanghien.converts.CodingEncryption;
import fit.ktpm_tuan_06_20036311_tranquanghien.converts.ConvertObjectWithGson;
import fit.ktpm_tuan_06_20036311_tranquanghien.models.Product;
import fit.ktpm_tuan_06_20036311_tranquanghien.models.ProductReceive;
import fit.ktpm_tuan_06_20036311_tranquanghien.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private JmsTemplate jmsTemplate;
    private final ConvertObjectWithGson gson = new ConvertObjectWithGson();
    private final CodingEncryption encryption = new CodingEncryption();

    @Test
    void contextLoads() {
        Faker faker = new Faker();
        List<ProductReceive> productReceives = new ArrayList<>();
        String mail = "tqhien27102002@gmail.com";
        int soLuongSanPham = productRepository.findAll().size();
        int soLuongSanPhamCanMua = 10;
        if (soLuongSanPhamCanMua <= soLuongSanPham){
            for (int i = 0; i < soLuongSanPhamCanMua; i++) {
                ProductReceive productReceive = new ProductReceive();
                Product product = null;
                while (true){
                    product = productRepository.findById((long) faker.number().numberBetween(1,soLuongSanPham )).get();
                    boolean checkContain = false;
                    for (ProductReceive receive:productReceives) {
                        if (receive.getProduct() == product){
                            checkContain=true;
                            break;
                        }
                    }
                    if (!checkContain){
                        break;
                    }
                }
                productReceive.setProduct(product);
                productReceive.setEmail(mail);
                productReceive.setQuantity(faker.number().numberBetween(0, 100));
                productReceives.add(productReceive);
                String json = gson.ListObjectToJson(productReceives);
                jmsTemplate.convertAndSend("ABC_Shop", encryption.enCode(json));
            }
        }
    }
}
