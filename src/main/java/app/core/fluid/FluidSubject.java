package app.core.fluid;

import app.core.suite.Subject;

public interface FluidSubject extends Fluid<Subject> {

    default Fluid<Object> keys() {
        return () -> new FluidIterator<>() {
            final FluidIterator<Subject> subIt = iterator();

            @Override
            public boolean hasNext() {
                return subIt.hasNext();
            }

            @Override
            public Object next() {
                return subIt.next().key().direct();
            }
        };
    }

    default Fluid<Object> values() {
        return () -> new FluidIterator<>() {
            final FluidIterator<Subject> subIt = iterator();

            @Override
            public boolean hasNext() {
                return subIt.hasNext();
            }

            @Override
            public Object next() {
                return subIt.next().direct();
            }
        };
    }

    static FluidSubject empty() {
        return FluidIterator::empty;
    }
}
