package com.prosesol.springboot.app.entity.composite.id;

import java.io.Serializable;

public class RelUsuarioPerfilId implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long usuario;

    private Long perfil;

    public RelUsuarioPerfilId() {
    }

    public RelUsuarioPerfilId(Long usuario, Long perfil) {
        this.usuario = usuario;
        this.perfil = perfil;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
        result = prime * result + ((perfil == null) ? 0 : perfil.hashCode());
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
        RelUsuarioPerfilId other = (RelUsuarioPerfilId) obj;
        if (usuario == null) {
            if (other.usuario != null)
                return false;
        } else if (!usuario.equals(other.usuario))
            return false;
        if (perfil == null) {
            if (other.perfil != null)
                return false;
        } else if (!perfil.equals(other.perfil))
            return false;
        return true;
    }
}
