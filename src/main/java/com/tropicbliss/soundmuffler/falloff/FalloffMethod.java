package com.tropicbliss.soundmuffler.falloff;

public enum FalloffMethod {
    LINEAR(new LinearFalloffMethod()),
    EXPONENTIAL(new ExponentialFalloffMethod()),
    LOGARITHMIC(new LogarithmicFalloffMethod()),
    INVERSE_SQUARED(new InverseSquaredFalloffMethod());

    private final IFalloffMethod falloffMethod;

    FalloffMethod(IFalloffMethod falloffMethod) {
        this.falloffMethod = falloffMethod;
    }

    public IFalloffMethod getFalloffMethod() {
        return falloffMethod;
    }
}