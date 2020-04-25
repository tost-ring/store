package app.core.suite;

import app.core.fluid.Fluid;
import app.core.fluid.FluidIterator;
import app.core.fluid.FluidSubject;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
class BubbleSubject implements Subject {

    private final Object bubbled;

    BubbleSubject(Object bubbled) {
        this.bubbled = bubbled;
    }

    @Override
    public Subject set(Object element) {
        return Objects.equals(bubbled, element) ? new BubbleSubject(element) :
                new MultiSubject().set(bubbled).set(element, element);
    }

    @Override
    public Subject set(Object key, Object value) {
        return Objects.equals(bubbled, key) ? new CoupleSubject(key, value) :
                new MultiSubject().set(bubbled).set(key, value);
    }

    @Override
    public Subject put(Object element) {
        return Objects.equals(bubbled, element) ? this :
                new MultiSubject().set(bubbled).set(element, element);
    }

    @Override
    public Subject put(Object key, Object value) {
        return Objects.equals(bubbled, key) ? this :
                new MultiSubject().set(bubbled).set(key, value);
    }

    @Override
    public Subject add(Object element) {
        return new MultiSubject().set(bubbled).add(element);
    }

    @Override
    public Subject unset(Object key) {
        return Objects.equals(bubbled, key) ? ZeroSubject.getInstance() : this;
    }

    @Override
    public Subject unset(Object key, Object value) {
        return Objects.equals(bubbled, key) && Objects.equals(bubbled, value) ? ZeroSubject.getInstance() : this;
    }

    @Override
    public Subject key() {
        return this;
    }

    @Override
    public Subject prime() {
        return this;
    }

    @Override
    public Subject recent() {
        return this;
    }

    @Override
    public Subject get(Object key) {
        return Objects.equals(bubbled, key) ? this : ZeroSubject.getInstance();
    }

    @Override
    public Object direct() {
        return bubbled;
    }

    @Override
    public <B> B asExpected() {
        return (B)bubbled;
    }

    @Override
    public <B> B asGiven(Class<B> requestedType) {
        return (B)bubbled;
    }

    @Override
    public <B> B asGiven(Glass<? super B, B> requestedType) {
        return (B)bubbled;
    }

    @Override
    public <B> B asGiven(Class<B> requestedType, B substitute) {
        return requestedType.isInstance(bubbled) ? requestedType.cast(bubbled) : substitute;
    }

    @Override
    public <B> B asGiven(Glass<? super B, B> requestedType, B substitute) {
        return requestedType.isInstance(bubbled) ? requestedType.cast(bubbled) : substitute;
    }

    @Override
    public <B> B orGiven(B substitute) {
        return bubbled == null ? substitute : (B)bubbled;
    }

    @Override
    public <B> B orDo(Supplier<B> supplier) {
        return bubbled == null ? supplier.get() : (B)bubbled;
    }

    @Override
    public boolean assigned(Class<?> type) {
        return type.isInstance(bubbled);
    }

    @Override
    public boolean settled() {
        return true;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public FluidSubject front() {
        return () -> new FluidIterator<>() {
            boolean available = true;

            @Override
            public boolean hasNext() {
                return available;
            }

            @Override
            public Subject next() {
                available = false;
                return BubbleSubject.this;
            }
        };
    }

    @Override
    public FluidSubject reverse() {
        return front();
    }

    @Override
    public String toString() {
        return "[" + bubbled + "]" + bubbled;
    }
}
