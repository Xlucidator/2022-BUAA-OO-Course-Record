public interface IntSet {
    //@ public instance model int[] ia;
    //@ public invariant (\ int i, j; 0 <= i && i < j && j < ia.length; ia[i] < ia[j]);


    //@ ensures \result == (\exists int i; 0 <= i && i < ia.length; ia[i] == x);
    public /*@ pure @*/ Boolean contains(int x);

    /*@ public normal_behavior
      @ assignable \nothing
      @ requires 0 <= x && x < ia.length;
      @ ensures \result == ia[x];
      @ also
      @ public exceptional_behavior
      @ signals (IndexOutOfBoundsException e) (x < 0 || x >= ia.length);
      @*/
    public int getNum(int x) throws IndexOutOfBoundsException;

    /*@ public normal_behavior
      @ assignable ia;
      @ requires !contains(x);
      @ ensures contains(x);
      @ ensures (\forall int i; 0 <= i && i < \old(ia.length); contains(\old(ia[i])));
      @ ensures (\forall int i; 0 <= i && i < ia.length && ia[i] != x; \old(contains(ia[i])));
      @*/
    public void insert(int x);

    /*@ public normal_behavior
      @ assignable ia;
      @ requires contains(x);
      @ ensures !contains(x);
      @ ensures (\forall int i; 0 <= i && i < ia.length; \old(contains(ia[i])));
      @ ensures (\forall int i; 0 <= i && i < \old(ia.length) && \old(ia[i]) != x; contains(\old(ia[i])));
      @*/
    public void delete(int x);

    //@ ensures \result == ia.length;
    public /*@ pure @*/ int size();

    /*@ public normal_behavior
      @ assignable ia, a.ia;
      @ ensures (\forall int i; 0 <= i && i < ia.length; ia[i] == \old(a.ia[i]));
      @ ensures (\forall int i; 0 <= i && i < a.ia.length; a.ia[i] == \old(a[i]));
      @ ensures ia.length == \old(a.ia.length);
      @ ensures a.ia.length == \old(ia.length);
      @*/
    public void elementSwap(IntSet a);

    /*@ public normal_behavior
      @ assignable \nothing;
      @ requires a != null;
      @ ensures (\forall int i; 0 <= i && i < ia.length; !a.contains(ia[i]) <==> \result.contains[i]);
      @ ensures (\forall int i; 0 <= i && i < a.ia.length; !contains(a.ia[i] <==> \result.contains[i]));
      @ also
      @ public exceptional_behavior
      @ requires a == null;
      @ signals_only NullPointerException;
      @*/
    public /*@ pure @*/ IntSet symmetricDifference(IntSet a) throws NullPointerException;

    //@ ensures \result == (\forall int i, j ; 0 <= i && i < j && j < ia.length; ia[i] != ia[j] && ia[i] < ia[j]);
    public /*@ pure @*/ boolean repOK();
}
