package dk.brics.automaton;

import java.util.Comparator;

public class CustomComparator implements Comparator<Transition> {
  @Override
  public int compare(Transition t1, Transition t2) {
    if (t1.to != t2.to) {
      if (t1.to == null)
        return -1;
      else if (t2.to == null)
        return 1;
      else if (t1.to.number < t2.to.number)
        return -1;
      else if (t1.to.number > t2.to.number)
        return 1;
    }
    if (t1.min < t2.min)
      return -1;
    if (t1.min > t2.min)
      return 1;
    if (t1.max > t2.max)
      return -1;
    if (t1.max < t2.max)
      return 1;
    if (t1.to.id > t2.to.id)
      return -1;
    if (t1.to.id < t2.to.id)
      return 1;
    return 0;
  }
}
