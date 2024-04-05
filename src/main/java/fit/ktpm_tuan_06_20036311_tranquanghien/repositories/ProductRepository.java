package fit.ktpm_tuan_06_20036311_tranquanghien.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import fit.ktpm_tuan_06_20036311_tranquanghien.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}