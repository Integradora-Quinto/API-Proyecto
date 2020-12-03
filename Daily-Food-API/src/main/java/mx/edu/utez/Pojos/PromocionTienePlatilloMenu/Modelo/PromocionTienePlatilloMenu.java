package Pojos.PromocionTienePlatilloMenu.Modelo;

import Pojos.Platillo.Modelo.Platillo;
import Pojos.Promocion.Modelo.Promocion;

public class PromocionTienePlatilloMenu {

    private Promocion idPromocion;
    private Platillo idPlatillo;

    public Promocion getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion(Promocion idPromocion) {
        this.idPromocion = idPromocion;
    }

    public Platillo getIdPlatillo() {
        return idPlatillo;
    }

    public void setIdPlatillo(Platillo idPlatillo) {
        this.idPlatillo = idPlatillo;
    }
}