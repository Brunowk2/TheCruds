package senac.senacfx.model.dao;

import senac.senacfx.model.entities.Department;
import senac.senacfx.model.entities.Seller;
import senac.senacfx.model.entities.Vendedor;

import java.util.List;

public interface VendedorDao {

    void insert(Vendedor obj);
    void update(Vendedor obj);


    void deleteById(Integer id);
    Seller findById(Integer id);
    List<Vendedor> findAll();
    List<Vendedor> findByClientes (Clientes department);



}
