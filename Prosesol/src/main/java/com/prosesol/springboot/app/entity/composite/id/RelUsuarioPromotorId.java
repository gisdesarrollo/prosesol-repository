package com.prosesol.springboot.app.entity.composite.id;

import com.prosesol.springboot.app.entity.Promotor;
import com.prosesol.springboot.app.entity.Usuario;

import java.io.Serializable;

public class RelUsuarioPromotorId implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Usuario usuario;

    private Promotor promotor;

    public RelUsuarioPromotorId() {
    }

    public RelUsuarioPromotorId(Usuario usuario, Promotor promotor) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
        result = prime * result + ((promotor == null) ? 0 : promotor.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RelUsuarioPromotorId other = (RelUsuarioPromotorId) obj;
        if (usuario == null) {
            if (other.usuario != null)
                return false;
        } else if (!usuario.equals(other.usuario))
            return false;
        if (promotor == null) {
            if (other.promotor != null)
                return false;
        } else if (!promotor.equals(other.promotor))
            return false;
        return true;
    }
}
