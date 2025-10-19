package com.asdeveloper.bloodmate.Models;

public class UserHealth {
    public UserHealth() {
    }

    public UserHealth(boolean hasDiabetes, boolean hasHypertension, boolean hasAsthma, boolean hasThyroid, boolean hasHeartDisease, Integer weight, boolean anyRecentSurgery, boolean willDonate) {
        this.hasDiabetes = hasDiabetes;
        this.hasHypertension = hasHypertension;
        this.hasAsthma = hasAsthma;
        this.hasThyroid = hasThyroid;
        this.hasHeartDisease = hasHeartDisease;
        this.weight = weight;
        this.anyRecentSurgery = anyRecentSurgery;
        this.willDonate = willDonate;
    }

    private Long id;

        private User user;

        private boolean hasDiabetes;

        private boolean hasHypertension;

        private boolean hasAsthma;

        private boolean hasThyroid;

        private boolean hasHeartDisease;

        private Integer weight;

        private boolean anyRecentSurgery;

        private boolean willDonate;

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isHasDiabetes() {
        return hasDiabetes;
    }

    public void setHasDiabetes(boolean hasDiabetes) {
        this.hasDiabetes = hasDiabetes;
    }

    public boolean isHasHypertension() {
        return hasHypertension;
    }

    public void setHasHypertension(boolean hasHypertension) {
        this.hasHypertension = hasHypertension;
    }

    public boolean isHasAsthma() {
        return hasAsthma;
    }

    public void setHasAsthma(boolean hasAsthma) {
        this.hasAsthma = hasAsthma;
    }

    public boolean isHasThyroid() {
        return hasThyroid;
    }

    public void setHasThyroid(boolean hasThyroid) {
        this.hasThyroid = hasThyroid;
    }

    public boolean isHasHeartDisease() {
        return hasHeartDisease;
    }

    public void setHasHeartDisease(boolean hasHeartDisease) {
        this.hasHeartDisease = hasHeartDisease;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public boolean isAnyRecentSurgery() {
        return anyRecentSurgery;
    }

    public void setAnyRecentSurgery(boolean anyRecentSurgery) {
        this.anyRecentSurgery = anyRecentSurgery;
    }

    public boolean isWillDonate() {
        return willDonate;
    }

    public void setWillDonate(boolean willDonate) {
        this.willDonate = willDonate;
    }
}
