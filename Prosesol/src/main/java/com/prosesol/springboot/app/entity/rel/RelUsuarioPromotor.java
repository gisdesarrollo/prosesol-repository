package com.prosesol.springboot.app.entity.rel;

import com.prosesol.springboot.app.entity.Promotor;
import com.prosesol.springboot.app.entity.Usuario;
import com.prosesol.springboot.app.entity.composite.id.RelUsuarioPromotorId;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rel_usuarios_promotores")
@IdClass(RelUsuarioPromotorId.class)
public class RelUsuarioPromotor implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_promotor")
    private Promotor promotor;

    public RelUsuarioPromotor() {
    }

    public RelUsuarioPromotor(Usuario usuario, Promotor promotor) {
        this.usuario = usuario;
        this.promotor = promotor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Promotor getPromotor() {
        return promotor;
    }

    public void setPromotor(Promotor promotor) {
        this.promotor = promotor;
    }
}
