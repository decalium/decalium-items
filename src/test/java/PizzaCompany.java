public class PizzaCompany {
    private PizzaDelivery delivery;
    private final PizzaFactory factory;

    public PizzaCompany(PizzaDelivery delivery, PizzaFactory factory) {
        this.delivery = delivery;
        this.factory = factory;

    }

}
