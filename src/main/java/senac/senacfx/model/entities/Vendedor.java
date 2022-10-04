package senac.senacfx.model.entities;

import java.io.Serializable;
import java.util.Date;

    public class Vendedor implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer id;
        private String name;
        private String cpf;
        private String email;
        private Double comissao;

        public Vendedor() {
        }

        public Vendedor(Integer id, String name, String cpf,String email, Double comissao ) {
            this.id = id;
            this.name = name;
            this.cpf = cpf;
            this.email = email;
            this.comissao = comissao;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCpf() {
            return cpf;
        }

        public void setCpf(String cpf) {
            this.cpf = cpf;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Double getComissao() {
            return comissao;
        }
        public void setComissao(Double comissao) {
            this.comissao = comissao;
        }



        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null)
                return false;
            if (getClass() != o.getClass())
                return false;
            senac.senacfx.model.entities.Vendedor other = (senac.senacfx.model.entities.Vendedor) o;
            if (id == null){
                if (other.id != null)
                    return false;
            } else if (!id.equals(other.id))
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((id == null) ? 0 : id.hashCode());
            return result;
        }

        @Override
        public String toString() {
            return "Vendededor{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", cpf='" + cpf + '\'' +
                    ", email='" + email + '\'' +
                    ", comissao=" + comissao +
                    '}';
        }

        public Clientes getClientesId() {
        }
    }



