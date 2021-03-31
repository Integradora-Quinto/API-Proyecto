package mx.edu.utez.platilloenmenu.model;

import mx.edu.utez.menu.model.Menu;
import mx.edu.utez.platillo.model.Platillo;
import mx.edu.utez.platillo.model.PlatilloCompleto;

public class PlatilloEnMenu {

    private int idPlatilloMenu;
    private int cantidadEstimada;
    private boolean status;
    private Menu idMenu;
    private Platillo idPlatillo;
    private PlatilloCompleto idPlatillo2;

    public PlatilloCompleto getIdPlatillo2() {
        return idPlatillo2;
    }

    public void setIdPlatillo2(PlatilloCompleto idPlatillo2) {
        this.idPlatillo2 = idPlatillo2;
    }

    public int getIdPlatilloMenu() {
        return idPlatilloMenu;
    }

    public void setIdPlatilloMenu(int idMenuPlatillo) {
        this.idPlatilloMenu = idMenuPlatillo;
    }

    public int getCantidadEstimada() {
        return cantidadEstimada;
    }

    public void setCantidadEstimada(int cantidadEstimada) {
        this.cantidadEstimada = cantidadEstimada;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Menu getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Menu idMenu) {
        this.idMenu = idMenu;
    }

    public Platillo getIdPlatillo() {
        return idPlatillo;
    }

    public void setIdPlatillo(Platillo idPlatillo) {
        this.idPlatillo = idPlatillo;
    }

}
