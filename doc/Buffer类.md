# Buffer类

```java
/*@ public normal_behavior
  @ assignable limit, position, mark
  @ ensures limit == \old(position) 
  @ ensures (position == 0) && (mark == -1)
  @ ensures \result == this
  @*/
public Buffer flip();
```

