package fit.ktpm_tuan_06_20036311_tranquanghien.services;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import fit.ktpm_tuan_06_20036311_tranquanghien.converts.CodingEncryption;
import fit.ktpm_tuan_06_20036311_tranquanghien.converts.ConvertObjectWithGson;
import fit.ktpm_tuan_06_20036311_tranquanghien.models.Product;
import fit.ktpm_tuan_06_20036311_tranquanghien.models.ProductOrder;
import fit.ktpm_tuan_06_20036311_tranquanghien.models.ProductReceive;
import fit.ktpm_tuan_06_20036311_tranquanghien.repositories.ProductOrderRepository;
import fit.ktpm_tuan_06_20036311_tranquanghien.repositories.ProductRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class MessageService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductOrderRepository productOrderRepository;
    @Autowired
    private SendMail sendMail;
    @JmsListener(destination = "ABC_Shop")
    public void receiveMessage(final Message jsonMessage) throws JMSException {
        CodingEncryption encryption = new CodingEncryption();
        ConvertObjectWithGson gson = new ConvertObjectWithGson();
        if (jsonMessage instanceof TextMessage) {
            String messageData = ((TextMessage) jsonMessage).getText();
            String json = encryption.decode(messageData);
            List<ProductReceive> productReceives = gson.JsonToListObject(json);
            List<Product> productList = new ArrayList<>();
            for (ProductReceive productReceive :productReceives) {
                Product product = productRepository.findById(productReceive.getProduct().getId()).get();
                if (productReceive.getQuantity() > product.getQuantity()){
                    productList.add(product);
                }
            }
            String textNotification ="";
            if (productList.isEmpty()){
                LocalDate date = LocalDate.now();
                textNotification+="Đơn hàng đặt lúc: "+date.toString()+"\n ";
                double total = 0;
                for (ProductReceive productReceive:productReceives) {
                    total+=productReceive.getProduct().getPrice()*productReceive.getQuantity();
                    textNotification += "\t\t"+productReceive.getProduct().getName()+":\t"+productReceive.getProduct().getPrice()*productReceive.getQuantity()+"\n";
                    ProductOrder productOrder = new ProductOrder();
                    productOrder.setProduct(productReceive.getProduct());
                    productOrder.setQuantity(productReceive.getQuantity());
                    productOrder.setOrderDate(date);
                    productOrder.setTotal(productReceive.getProduct().getPrice()*productReceive.getQuantity());
                    productOrderRepository.save(productOrder);
                }
                textNotification += "\t\tTotal: \t"+total;

            }else {
                textNotification+="Đơn hàng gặp vấn đề.\n \t\tLý do là các sản phẩm:";
                for (Product product:productList) {
                    textNotification+=" "+product.getName()+",";
                }
                textNotification = textNotification.substring(0,textNotification.length()-1);
                textNotification+=" => không đủ số lượng yêu cầu";
            }
            System.out.println(textNotification);
            sendMail.sendMail(productReceives.get(0).getEmail(),"THÔNG BÁO ĐƠN HÀNG: ",textNotification);
        }
    }
}


