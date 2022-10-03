package senac.senacfx.model.dao.impl;

public interface ClientesDao {

    void insert(Clientes obj);
    void update(Clientes obj);
    void deleteById(Integer id);
    Clientes findById(Integer id);
    List<Clientes> findAll();
}
