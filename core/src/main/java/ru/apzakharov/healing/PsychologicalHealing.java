package ru.apzakharov.healing;

public class PsychologicalHealing implements Healing {
    private final String healPhrase;

    public PsychologicalHealing(String healPhrase) {
        this.healPhrase = healPhrase;
    }

    @Override
    public String getHeal() {
        return healPhrase;
    }
}
