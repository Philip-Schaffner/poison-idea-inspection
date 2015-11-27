## Poison Inspection ##
This plugin provides an IntelliJ IDEA inspection that looks for field access bypassing designated setter and getter methods.

The plugin is called Poison because the initial idea was to add a @Poison() field annotation that would kill any code referencing the poisoned field (cause compilation error), unless the code was made immune.

This idea evolved into an inspection that requires no changes in the code to be useful.

## Why would I want this? ##
Sometimes you have fields that **really** should be accessed by their setters and getters because their behavior is non-trivial.

Any direct access might break your assumptions, but you may have some legacy code that touches your field - for example 2 methods below, in the code you have already forgotten.

Poison Inspection helps you spot such code by reporting a warning for suspicious references.

Do you want to know [how it works](HowDoesItWork.md) ?

There is also a short UseCase.

## Can I get one? ##
The plugin can be downloaded from Downloads section or directly from [IntelliJ site](http://plugins.intellij.net/plugin/?id=3213)