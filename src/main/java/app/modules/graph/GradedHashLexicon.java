/**
 * Created by LPO on 08.09.2018
 */

package app.modules.graph;

import java.util.*;

public class GradedHashLexicon<B, T, G> implements GradedLexicon<B, T, G> {

    class WardMap extends HashMap<T, G> {
        B ward;

        WardMap(B ward) {
            this.ward = ward;
        }

        public WardMap(int initialCapacity, B ward) {
            super(initialCapacity);
            this.ward = ward;
        }
    }

    private Map<B,WardMap> data;
    private WardMap fakeTraits;

    public GradedHashLexicon(){
        data = new HashMap<>();
        fakeTraits = new WardMap(0, null);
    }

    protected final void set(GradedHashLexicon<B, T, G> that){
        this.data = that.data;
    }

    @Override
    public B putBeing(B being){
        WardMap wardMap = data.putIfAbsent(being,new WardMap(being));
        return wardMap == null ? being : wardMap.ward;
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
    public Set<T> getTraits(B being){
        return data.getOrDefault(being,fakeTraits).keySet();
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
        return data.getOrDefault(being, fakeTraits).containsKey(trait);
    }

    @Override
    public void grantTrait(B being, T trait, G grade) {
        putBeing(being);
        data.get(being).put(trait, grade);
    }

    @Override
    public void loseTrait(B being, T trait){
        data.getOrDefault(being,fakeTraits).remove(trait);
    }

    @Override
    public G getGrade(B being, T trait) {
        return data.getOrDefault(being, fakeTraits).get(trait);
    }

    @Override
    public Collection<G> getGrades(B being) {
        return data.getOrDefault(being, fakeTraits).values();
    }
}
