package com.example.georgios.plans.model;

import android.app.Application;

/**
 * Created by IkosidekasDesktop on 24/11/2017.
 */

public class GlobalClass extends Application {

    private UsuarioEntity user = new UsuarioEntity();
    private PlanEntity plan = new PlanEntity();

    public UsuarioEntity getUser() {
        return user;
    }

    public PlanEntity getPlan() {
        return plan;
    }

    public void setUser(UsuarioEntity user) {
        this.user = user;
    }

    public void setPlan(PlanEntity plan) {
        this.plan = plan;
    }
}
