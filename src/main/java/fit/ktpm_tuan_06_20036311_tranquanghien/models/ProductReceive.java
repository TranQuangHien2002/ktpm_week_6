package fit.ktpm_tuan_06_20036311_tranquanghien.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductReceive {
    private Product product;
    private String email;
    private int quantity;
}
