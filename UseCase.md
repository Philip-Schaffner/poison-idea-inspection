You start a day with a nice class:

```
public class NiceClass {

    // lots of clever code above

    private int knob;

    public void shutDown() {
        knob = 0;
        doStuff();
    }

    public void tweak(int delta) {
        knob += delta;
        doOtherStuff();
    }
}
```

For any reason, you add a compound class that needs to know the current status of the knob:
```
public class NiceClass {

    // lots of clever code above

    private NewClass compound = new NewClass();

    private int knob;
    // compound should be notified of turning the knob
    void setKnob(int position) {
        knob = position;
        compound.setKnob(position);
    }

    public void shutDown() {
        setKnob(0);
        doStuff();
    }

    public void tweak(int delta) {
        knob += delta;
        doOtherStuff();
    }
}
```

Probably you have already spotted what change is missed. But maybe you have not, especially if the `tweak()` method was hiding between other methods.

What Poison Inspection would do for you is highlight the `knob += delta`, as this is an assignment operation that bypasses your new `setKnob()` setter.

While you might forget to look for references to the `knob` before releasing your code, but there is a great chance that you spot the compilation error or warning.