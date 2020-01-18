package app.core.suite;

import app.core.suite.transition.Function;

import java.io.Serializable;

public class Coupon<G> implements Serializable {

    public static<B> Coupon<B> forObjectOf(Class<B> brand){
        return new Coupon<>(Glass.of(brand));
    }

    public static<B> Coupon<B> forObjectOf(Glass<B, B> brand){
        return new Coupon<>(brand);
    }

    public static Coupon<Function> forFun() {
        return new Coupon<>(Glass.of(Function.class));
    }

    private Glass<G, G> glass;

    public Coupon(Glass<G, G> glass) {
        this.glass = glass;
    }

    public Glass<G, G> getGlass() {
        return glass;
    }

    public boolean valid(Object o) {
        return glass.isInstance(o);
    }

    public G deal(Object o) {
        return glass.cast(o);
    }

    @Override
    public String toString() {
        return "Coupon<" + glass.toString() + ">";
    }
}
