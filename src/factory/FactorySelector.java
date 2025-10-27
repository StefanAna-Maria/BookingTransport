package factory;

public class FactorySelector {
    public static UtilizatorFactory getFactory(String rol) {
        switch (rol.toUpperCase()) {
            case "CLIENT":
                return new ClientFactory();
            case "SOFER":
                return new SoferFactory();
            case "ADMIN":
                return new AdminFactory();
            default:
                throw new IllegalArgumentException("Rol necunoscut: " + rol);
        }
    }
}
