/**
 * Created by LPO on 08.09.2018
 */

package app.modules.graph;

import java.util.*;

public class HashLexicon<B,T> implements Lexicon<B,T> {

    class WardSet extends HashSet<T> {
        B ward;

        WardSet(B ward) {
            this.ward = ward;
        }

        public WardSet(int initialCapacity, B ward) {
            super(initialCapacity);
            this.ward = ward;
        }
    }

    private Map<B,WardSet> data;
    private WardSet fakeTraits;

    public HashLexicon(){
        data = new HashMap<>();
        fakeTraits = new WardSet(0, null);
    }

    protected final void set(HashLexicon<B,T> that){
        this.data = that.data;
    }

    @Override
    public B putBeing(B being){
        WardSet wardSet = data.putIfAbsent(being,new WardSet(being));
        return wardSet == null ? being : wardSet.ward;
    }

    @Override
    public void removeBeing(B being){
        data.remove(being);
    }

    @Override
    public Set<B> getBeings(){
        return data.keySet();
    }

    @Override
    public B getBeing(){
        Iterator<B> iterator = data.keySet().iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    @Override
    public int countBeings(){
        return data.size();
    }

    @Override
    public boolean containsBeing(B being){
        return data.containsKey(being);
    }

    @Override
    public Collection<T> getTraits(B being){
        return data.getOrDefault(being,fakeTraits);
    }

    @Override
    public T getTrait(B being) {
        Iterator<T> iterator = getTraits(being).iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    @Override
    public int countTraits(B being){
        return getTraits(being).size();
    }

    @Override
    public boolean hasTrait(B being, T trait){
        return data.getOrDefault(being, fakeTraits).contains(trait);
    }

    @Override
    public void grantTrait(B being, T trait) {
        putBeing(being);
        data.get(being).add(trait);
    }

    @Override
    public void loseTrait(B being, T trait){
        data.getOrDefault(being,fakeTraits).remove(trait);
    }
}
